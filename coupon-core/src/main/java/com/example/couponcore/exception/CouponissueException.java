package com.example.couponcore.exception;

import lombok.Getter;

@Getter
public class CouponissueException extends RuntimeException {

    private final ErrorCode errorCode;

    private final String message;


    public CouponissueException(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return "[%s] %s".formatted(errorCode,message);
    }
}
