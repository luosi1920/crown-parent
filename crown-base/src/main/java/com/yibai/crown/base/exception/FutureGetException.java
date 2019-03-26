package com.yibai.crown.base.exception;

/**
 * Created by wangjichao on 2017/11/23.
 */
public class FutureGetException extends RuntimeException {

    private static final long serialVersionUID = -9065285608437968038L;

    public FutureGetException(Throwable cause) {
        super(cause);
    }

    public FutureGetException(String message) {
        super(message);
    }

    public FutureGetException(String message, Throwable cause) {
        super(message, cause);
    }

}
