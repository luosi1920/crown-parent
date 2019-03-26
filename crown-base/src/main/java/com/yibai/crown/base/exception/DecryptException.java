package com.yibai.crown.base.exception;

/**
 * 2017/10/31.
 */
public class DecryptException extends RuntimeException {

    public DecryptException(String message) {
        super(message);
    }

    public DecryptException(String message, Throwable cause) {
        super(message, cause);
    }

}
