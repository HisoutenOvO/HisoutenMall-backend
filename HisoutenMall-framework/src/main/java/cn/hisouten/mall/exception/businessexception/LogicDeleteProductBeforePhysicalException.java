package cn.hisouten.mall.exception.businessexception;

import cn.hisouten.mall.exception.BaseException;

public class LogicDeleteProductBeforePhysicalException extends BaseException {
    public LogicDeleteProductBeforePhysicalException(String message) {
        super(message);
    }
    public LogicDeleteProductBeforePhysicalException(){}
}
