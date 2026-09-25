package cn.hisouten.mall.exception.businessexception;

import cn.hisouten.mall.exception.BaseException;

public class UserStatusErrorException extends BaseException {
    public UserStatusErrorException(String message) {
        super(message);
    }
    public UserStatusErrorException(){}
}
