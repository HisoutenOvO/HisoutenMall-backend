package cn.hisouten.mall.server;

import cn.hisouten.mall.mapper.ProductMapper;
import cn.hisouten.mall.pojo.entity.Product;
import cn.hisouten.mall.util.CacheClientUtils;
import cn.hutool.core.lang.UUID;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

//@SpringBootTest
//public class RedisTest {
//
//    @Autowired
//    private CacheClientUtils cacheClientUtils;
//
//    @Autowired
//    private ProductMapper productMapper;
//
//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    static class User {
//        private String name;
//        private Integer age;
//    }
//
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//
//    @Test
//    void testTemplate() throws JsonProcessingException {
//
//
//        Product product = cacheClientUtils.queryWithPassThrough("product",1L,Product.class,(id) -> {
//            return productMapper.selectById(1L);},10L, TimeUnit.MINUTES);
//
//        System.out.println(product);
//    }
//}

@SpringBootTest
@RequiredArgsConstructor
public class SimpleRedisLock {

    private final StringRedisTemplate stringRedisTemplate;

    private static final String KEY_PREFIX = "lock:";

    private static final String ID_PREFIX = UUID.randomUUID().toString(true);

    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT; //定义脚本类

    //在静态代码块预加载脚本文件
    static{
        UNLOCK_SCRIPT = new DefaultRedisScript<>();
        UNLOCK_SCRIPT.setLocation(new ClassPathResource("unlock.lua")); //把文件加载进类
        UNLOCK_SCRIPT.setResultType(long.class); //定义返回值
    }

    //加锁
    public boolean tryLock(long timeoutSec,String name){
        //获取线程标识
        String threadId = ID_PREFIX + Thread.currentThread().threadId();
        //获取锁
        Boolean success = stringRedisTemplate.opsForValue().setIfAbsent(
                KEY_PREFIX + name,
                threadId,
                timeoutSec,
                TimeUnit.SECONDS
        );
        return Boolean.TRUE.equals(success); //防止拆箱时出现空指针，false或null都返回false，true返回true
    }

    //释放锁
    public void unlock(String name){
        //调用Lua脚本
        stringRedisTemplate.execute(
                UNLOCK_SCRIPT, //脚本
                Collections.singletonList(KEY_PREFIX + name), //KEYS[]，这里需要传list，调用这个方法将string变成单一list传入
                ID_PREFIX + Thread.currentThread().threadId() //ARGV[]，value的值，这里就是指线程标识
        );
    }
}
