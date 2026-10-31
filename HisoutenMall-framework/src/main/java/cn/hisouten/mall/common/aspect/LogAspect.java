package cn.hisouten.mall.common.aspect;

import cn.hisouten.mall.common.annotation.Log;
import cn.hisouten.mall.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 日志记录切片
 */
@Aspect
@Component
@Slf4j
@Order(2)
public class LogAspect {

    /**
     * 记录方法执行的日志
     * @param pjp 正在被执行的目标方法
     * @param logAnnotation 注解对象
     * @return 返回值
     * @throws Throwable 可抛出异常
     */
    @Around("@annotation(logAnnotation)")
    public Object around(ProceedingJoinPoint pjp, Log logAnnotation) throws Throwable {
        long start = System.currentTimeMillis();
        String method = pjp.getSignature().toShortString(); //获取方法名称
        Object[] args = pjp.getArgs(); //获取方法参数
        try {
            Object result = pjp.proceed(); //放行代码
            long cost = System.currentTimeMillis() - start;
            log.info("[{}] {} 耗时 {}ms, 参数: {}",
                    logAnnotation.value(), method, cost, Arrays.toString(args));
            return result;
        } catch (BizException e) {
            // 业务异常，只打一行warn不打印堆栈
            log.warn("[{}] 业务拦截: {}", method, e.getMessage());
            throw e;
        } catch (Throwable e) {
            // 真正的系统异常才打error和堆栈
            log.error("[{}] 系统异常！", method, e);
            throw e;
        }
    }
}