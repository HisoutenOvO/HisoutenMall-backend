package cn.hisouten.mall.exception.handler;

import cn.hisouten.mall.exception.BaseException;
import cn.hisouten.mall.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常（BaseException 及其子类）
     */
    @ExceptionHandler(BaseException.class)
    public Result<String> handleBaseException(BaseException e) {
        log.warn("业务异常：{}", e.getMessage());
        return Result.error(e.getMessage());
    }
//
//    /**
//     * 参数校验异常（@Valid 校验 @RequestBody 失败）
//     */
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public Result<String> handleValidException(MethodArgumentNotValidException e) {
//        String message = e.getBindingResult().getFieldErrors().stream()
//                .map(FieldError::getDefaultMessage)
//                .collect(Collectors.joining(", "));
//        log.warn("参数校验失败：{}", message);
//        return Result.error(400, message);
//    }
//
//    /**
//     * 兜底异常（所有未捕获的异常）
//     */
//    @ExceptionHandler(Exception.class)
//    public Result<String> handleException(Exception e) {
//        log.error("系统异常：", e);
//        return Result.error(500, "系统繁忙，请稍后再试");
//    }
}