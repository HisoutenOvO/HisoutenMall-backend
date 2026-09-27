package cn.hisouten.mall.exception.businessexception;

import cn.hisouten.mall.exception.BaseException;

public class CategoryNameAlreadyExistException extends BaseException {
    public CategoryNameAlreadyExistException(String message) {
        super(message);
    }
    public CategoryNameAlreadyExistException(){}
}
