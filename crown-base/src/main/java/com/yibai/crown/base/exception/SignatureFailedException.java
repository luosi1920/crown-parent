package com.yibai.crown.base.exception;

/**
 * 2017/10/31.
 */
public class SignatureFailedException extends RuntimeException{

    public SignatureFailedException(String message) {
        super(message);
    }

    public SignatureFailedException(String message, Throwable cause) {
        super(message, cause);
    }

}
