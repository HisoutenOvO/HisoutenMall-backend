package cn.hisouten.mall.exception.businessexception;

import cn.hisouten.mall.exception.BaseException;

public class UserAlreadyExist extends BaseException {
    public UserAlreadyExist(String message) {
        super(message);
    }
    public UserAlreadyExist(){}
}
