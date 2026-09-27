package cn.hisouten.mall.exception.businessexception;

import cn.hisouten.mall.exception.BaseException;

public class CategoryNotFoundException extends BaseException {
    public CategoryNotFoundException(String message) {
        super(message);
    }
    public CategoryNotFoundException(){}
}
