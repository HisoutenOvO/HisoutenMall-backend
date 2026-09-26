package cn.hisouten.mall.exception.businessexception;

import cn.hisouten.mall.exception.BaseException;

public class SkuNotFoundException extends BaseException {
    public SkuNotFoundException(String message) {
        super(message);
    }
    public SkuNotFoundException(){}
}
