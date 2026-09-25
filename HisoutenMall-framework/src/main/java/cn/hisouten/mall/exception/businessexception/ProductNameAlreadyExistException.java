package cn.hisouten.mall.exception.businessexception;

import cn.hisouten.mall.exception.BaseException;

public class ProductNameAlreadyExistException extends BaseException {
    public ProductNameAlreadyExistException(String message) {
        super(message);
    }
    public ProductNameAlreadyExistException(){}
}
