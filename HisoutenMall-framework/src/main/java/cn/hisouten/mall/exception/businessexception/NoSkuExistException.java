package cn.hisouten.mall.exception.businessexception;

import cn.hisouten.mall.exception.BaseException;

public class NoSkuExistException extends BaseException {
    public NoSkuExistException(String message) {
        super(message);
    }
    public NoSkuExistException(){}
}
