package cn.hisouten.mall.exception.businessexception;

import cn.hisouten.mall.exception.BaseException;

public class LogicDeleteSkuBeforePhysicalException extends BaseException {
    public LogicDeleteSkuBeforePhysicalException(String message) {
        super(message);
    }
    public  LogicDeleteSkuBeforePhysicalException(){}
}
