package cn.hisouten.mall.exception.businessexception;

import cn.hisouten.mall.exception.BaseException;

public class ProductHasNotDeletedException extends BaseException {
    public ProductHasNotDeletedException(String message) {
        super(message);
    }
    public ProductHasNotDeletedException(){}
}
