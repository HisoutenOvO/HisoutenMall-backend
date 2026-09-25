package cn.hisouten.mall.exception.businessexception;

import cn.hisouten.mall.exception.BaseException;

public class UserNotMatchException extends BaseException {
    public UserNotMatchException(String message) {
        super(message);
    }
    public UserNotMatchException(){}
}
