package cn.hisouten.mall.exception.businessexception;

import cn.hisouten.mall.exception.BaseException;

public class NoPermissionException extends BaseException {
    public NoPermissionException(String message) {
        super(message);
    }
    public NoPermissionException(){}
}
