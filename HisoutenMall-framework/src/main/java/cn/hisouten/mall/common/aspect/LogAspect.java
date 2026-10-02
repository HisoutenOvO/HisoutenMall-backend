package cn.hisouten.mall.common.aspect;

import cn.hisouten.mall.common.annotation.Log;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 日志记录切片
 */
@Aspect
@Component
@Slf4j
public class LogAspect {
    @Around("@annotation(logAnnotation)")
    public Object around(ProceedingJoinPoint pjp, Log logAnnotation) throws Throwable {
        long start = System.currentTimeMillis();
        String method = pjp.getSignature().toShortString();
        Object[] args = pjp.getArgs();
        try {
            Object result = pjp.proceed();
            long cost = System.currentTimeMillis() - start;
            log.info("[{}] {} 耗时 {}ms, 参数: {}",
                    logAnnotation.value(), method, cost, Arrays.toString(args));
            return result;
        } catch (Throwable e) {
            long cost = System.currentTimeMillis() - start;
            log.error("[{}] {} 异常 耗时 {}ms, 错误: {}",
                    logAnnotation.value(), method, cost, e.getMessage());
            throw e;
        }
    }
}