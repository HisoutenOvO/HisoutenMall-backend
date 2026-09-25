package cn.hisouten.mall.exception.businessexception;

import cn.hisouten.mall.exception.BaseException;

public class SpecsAlreadyExistException extends BaseException {
    public SpecsAlreadyExistException(String message) {
        super(message);
    }
    public SpecsAlreadyExistException(){}
}
