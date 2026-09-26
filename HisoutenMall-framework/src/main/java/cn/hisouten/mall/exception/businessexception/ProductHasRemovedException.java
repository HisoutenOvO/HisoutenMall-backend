package cn.hisouten.mall.exception.businessexception;

import cn.hisouten.mall.exception.BaseException;

public class ProductHasRemovedException extends BaseException {
    public ProductHasRemovedException(String message) {
        super(message);
    }
    public ProductHasRemovedException(){}
}
