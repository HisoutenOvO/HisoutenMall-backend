package cn.hisouten.mall.exception.businessexception;

import cn.hisouten.mall.exception.BaseException;

public class BrandNameAlreadyExist extends BaseException {
    public BrandNameAlreadyExist(String message) {
        super(message);
    }
    public BrandNameAlreadyExist(){}
}
