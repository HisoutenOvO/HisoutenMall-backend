package cn.hisouten.mall.exception.handler;

import cn.hisouten.mall.exception.BaseException;
import cn.hisouten.mall.pojo.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器配置
 */
@RestControllerAdvice //不显式指定扫描范围，全部扫描
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public Result<String> handleBaseException(BaseException e) {
        return Result.error(e.getMessage());
    }

}