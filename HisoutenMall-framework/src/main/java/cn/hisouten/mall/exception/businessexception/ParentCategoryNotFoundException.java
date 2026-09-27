package cn.hisouten.mall.exception.businessexception;

import cn.hisouten.mall.exception.BaseException;

public class ParentCategoryNotFoundException extends BaseException {
    public ParentCategoryNotFoundException(String message) {
        super(message);
    }
    public ParentCategoryNotFoundException(){}
}