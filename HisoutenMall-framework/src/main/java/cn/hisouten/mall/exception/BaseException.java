package cn.hisouten.mall.exception;

/**
 * 基本的业务异常类
 */
public class BaseException extends RuntimeException {
    public BaseException(String message) {
        super(message);
    }
    public BaseException() {}
}
