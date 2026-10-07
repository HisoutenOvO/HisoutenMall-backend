package cn.hisouten.mall.util;

import cn.hisouten.mall.pojo.RedisData;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.*;
import java.util.function.Function;

import static cn.hisouten.mall.constant.RedisConstant.CACHE_NULL_TTL;

/**
 * 封装自定义的缓存数据操作的工具类
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class CacheClientUtils{
    private final StringRedisTemplate stringRedisTemplate;

    //手动创建线程池，用于防治缓存击穿方法使用
    private static final ExecutorService CACHE_REBUILD_EXECUTOR = new ThreadPoolExecutor(
            10,                                  // 核心线程数
            20,                                  // 最大线程数
            60L, TimeUnit.SECONDS,               // 空闲存活时间
            new ArrayBlockingQueue<>(200),       // 有界队列
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.CallerRunsPolicy()   // 拒绝策略：调用者执行
    );

    /**
     * 存入缓存数据的普通set方法，拥有逻辑过期时间
     * @param key 键名
     * @param value 值
     * @param time 过期时间
     * @param unit 时间单位
     */
    public void set(String key, Object value, Long time, TimeUnit unit){
        //预防雪崩，加随机值
        long baseSeconds = unit.toSeconds(time);
        long randomSeconds = ThreadLocalRandom.current().nextLong(0, 300);
        long totalSeconds = baseSeconds + randomSeconds;

        stringRedisTemplate.opsForValue().set(
                key,
                JSONUtil.toJsonStr(value),
                totalSeconds,
                TimeUnit.SECONDS
        );
    }

    /**
     * 逻辑过期的set方法，只用于热点key
     * @param key 键
     * @param value 值
     * @param time 过期时间
     * @param unit 时间单位
     * @param <T> 传入的对象类型
     */
    public <T> void setWithLogic(String key, T value, Long time, TimeUnit unit){
        //设置逻辑过期时间
        RedisData<T> redisData = new RedisData<>();
        redisData.setData(value);
        redisData.setExpire(LocalDateTime.now().plusSeconds(unit.toSeconds(time)));

        //写入Redis
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }


    /**
     * 防止缓存穿透的查询缓存或数据库数据
     * @param keyPrefix 键名前缀
     * @param id 对象id
     * @param type 对象类型，用于在toBean里获取.class
     * @param dbFallBack 当缓存未命中时的查询数据库方法
     * @param time 设置过期时间
     * @param unit 时间单位
     * @return 返回查询到的对象
     * @param <T> 传入的对象类型
     */
    public <T> T queryWithPassThrough(String keyPrefix, Long id, Class<T> type, Function<Long,T> dbFallBack, Long time, TimeUnit unit){
        //拼接key名
        String key = keyPrefix + id;

        //从redis查询缓存
        String json = stringRedisTemplate.opsForValue().get(key);

        //判断是否存在
        if(StrUtil.isNotBlank(json)){
            //若缓存命中，则直接返回
            return JSONUtil.toBean(json,type);
        }

        //若未命中，则判断命中的是否是空值
        //如果能走到这一条判断，说明未命中数据，可能是命中空值或者null，如果json不是null，那就是命中空值了
        if(json != null){
            //若命中了空值，返回null
            return null;
        }

        //若缓存不存在，则直接查询数据库
        T object = dbFallBack.apply(id);

        //若数据库也不存在，则为了防止穿透将空值写入redis并设置过期时间
        if(object == null){
            //将空值写入redis，防止穿透
            stringRedisTemplate.opsForValue().set(key,"",CACHE_NULL_TTL,TimeUnit.MINUTES);
            //返回空
            return null;
        }

        //若数据库存在，则写入缓存
        this.set(key,object,time,unit);

        return object;
    }

    /**
     * 基于逻辑过期防止击穿的查询
     * @param keyPrefix 键名前缀
     * @param lockPrefix 锁名前缀
     * @param id 对象id
     * @param type 对象类型，用于.class
     * @param dbFallBack 查询数据库方法
     * @param time 过期时间
     * @param unit 时间单位
     * @return 返回对象
     * @param <T> 对象类型
     */
    public <T> T queryWithLogicExpire(String keyPrefix,String lockPrefix, Long id, Class<T> type, Function<Long,T> dbFallBack, Long time, TimeUnit unit){
        String key = keyPrefix + id;
        //从redis查询缓存
        String json = stringRedisTemplate.opsForValue().get(key);

        //判断是否存在
        if(StrUtil.isBlank(json)){
            //不存在，直接返回，不去进一步查数据库
            return null;
        }

        //命中，先反序列化为对象
        RedisData redisData = JSONUtil.toBean(json,RedisData.class);
        //如果对象获取出现问题
        if (redisData == null || redisData.getExpire() == null || redisData.getData() == null) {
            // 缓存数据异常，直接返回 null（或者删掉这个坏 key）
            stringRedisTemplate.delete(key);
            return null;
        }
        T object = JSONUtil.toBean((JSONObject) redisData.getData(),type);

        //判断是否过期
        if(redisData.getExpire().isAfter(LocalDateTime.now())){
            //未过期，直接返回对象
            return object;
        }

        //已过期，需要缓存重建
        //获取互斥锁并判断是否成功
        String lockKey = lockPrefix + id;
        boolean isLock = tryLock(lockKey);
        if(isLock){
            //获取锁成功，开始另辟线程重建缓存，不影响现在的缓存
            CACHE_REBUILD_EXECUTOR.submit(() -> {
                try{
                    //查询数据库
                    T cacheObject = dbFallBack.apply(id);
                    //如果返回的是null
                    if (cacheObject == null) {
                        // 数据已被删除，直接删掉缓存
                        stringRedisTemplate.delete(key);
                    } else {
                        //写入redis
                        this.setWithLogic(key, cacheObject, time, unit);
                    }
                }catch (Exception e){
                    log.error("缓存重建失败，key: {}", key, e);
                }finally {
                    unlock(lockKey);
                }
            });
        }
        return object;
    }

    /**
     * 缓存删除
     * @param keyPrefix 键名前缀
     * @param id 对象id
     */
    public void delete(String keyPrefix, Long id) {
        stringRedisTemplate.delete(keyPrefix + id);
    }


    private boolean tryLock(String key){
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key,"1",10,TimeUnit.SECONDS); //第二个参数是value，意义不大，第三个是保底ttl，第四个是时间单位
        return BooleanUtil.isTrue(flag);
    }

    private void unlock(String key){
        stringRedisTemplate.delete(key);
    }

}