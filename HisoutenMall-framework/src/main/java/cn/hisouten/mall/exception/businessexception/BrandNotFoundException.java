package cn.hisouten.mall.exception.businessexception;

import cn.hisouten.mall.exception.BaseException;

public class BrandNotFoundException extends BaseException {
    public BrandNotFoundException(String message) {
        super(message);
    }
    public BrandNotFoundException(){}
}
