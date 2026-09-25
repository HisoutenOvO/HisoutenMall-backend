package cn.hisouten.mall.exception.businessexception;

import cn.hisouten.mall.exception.BaseException;

public class ShopNameAlreadyExistException extends BaseException {
    public ShopNameAlreadyExistException(String message) {
        super(message);
    }
    public ShopNameAlreadyExistException(){}
}
