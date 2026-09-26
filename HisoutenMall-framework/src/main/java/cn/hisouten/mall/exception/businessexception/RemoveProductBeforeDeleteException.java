package cn.hisouten.mall.exception.businessexception;

import cn.hisouten.mall.exception.BaseException;

public class RemoveProductBeforeDeleteException extends BaseException {
    public RemoveProductBeforeDeleteException(String message) {
        super(message);
    }
    public RemoveProductBeforeDeleteException(){}
}
