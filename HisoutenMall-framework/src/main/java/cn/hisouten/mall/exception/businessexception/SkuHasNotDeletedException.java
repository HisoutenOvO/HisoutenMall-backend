package cn.hisouten.mall.exception.businessexception;

import cn.hisouten.mall.exception.BaseException;

public class SkuHasNotDeletedException extends BaseException {
    public SkuHasNotDeletedException(String message) {
        super(message);
    }
    public SkuHasNotDeletedException(){}
}
