package cn.hisouten.mall.exception.businessexception;

import cn.hisouten.mall.exception.BaseException;

public class ContactPhoneAlreadyExistException extends BaseException {
    public ContactPhoneAlreadyExistException(String message) {
        super(message);
    }
    public ContactPhoneAlreadyExistException(){}
}
