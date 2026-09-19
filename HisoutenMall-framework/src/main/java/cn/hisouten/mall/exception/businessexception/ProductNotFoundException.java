package cn.hisouten.mall.exception.businessexception;

import cn.hisouten.mall.exception.BaseException;

public class ProductNotFoundException extends BaseException {
    public ProductNotFoundException(String message) {
        super(message);
    }
    public ProductNotFoundException(){}
}
