package cn.hisouten.mall.exception.businessexception;

import cn.hisouten.mall.exception.BaseException;

public class LevelOverflowException extends BaseException {
    public LevelOverflowException(String message) {
        super(message);
    }
    public LevelOverflowException(){}
}
