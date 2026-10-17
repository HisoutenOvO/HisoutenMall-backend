package cn.hisouten.mall.common.aspect;

import cn.dev33.satoken.stp.StpUtil;
import cn.hisouten.mall.common.annotation.RepeatSubmit;
import cn.hisouten.mall.exception.BizException;
import cn.hutool.crypto.digest.DigestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static cn.hisouten.mall.constant.RedisConstant.REPEAT_SUBMIT;

/**
 * 防重复提交切片
 */
@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
@Order(1) //设置优先级
public class RepeatSubmitAspect {
    private final StringRedisTemplate redisTemplate;

    /**
     * 通过redis实现防重复提交
     * @param pjp 正在执行的方法
     * @param repeatSubmit repeatSubmit注解对象
     * @return 返回值
     */
    @Around("@annotation(repeatSubmit)")
    public Object around(ProceedingJoinPoint pjp, RepeatSubmit repeatSubmit) throws Throwable{
        Long userId = StpUtil.getLoginIdAsLong();

        //拼接RedisKey：防重:用户ID:方法名:参数Hash
        String methodName = pjp.getSignature().toShortString();
        String argsHash = DigestUtil.md5Hex(Arrays.toString(pjp.getArgs()));
        String key = REPEAT_SUBMIT + userId + ":" + methodName + ":" + argsHash;

        //尝试SETNX写入
        Boolean absent = redisTemplate.opsForValue()
                .setIfAbsent(key, "1", repeatSubmit.interval(), TimeUnit.SECONDS);
        if (Boolean.FALSE.equals(absent)) {
            log.warn("用户 {} 触发防重复提交: {}", userId, methodName);
            throw new BizException("请勿重复点击或提交过快");
        }

        //放行方法
        try {
            return pjp.proceed();
        } catch (Throwable e) {
            //如果业务失败，允许重试
            redisTemplate.delete(key);
            throw e;
        }
    }
}
