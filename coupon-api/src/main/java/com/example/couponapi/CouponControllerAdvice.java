package com.example.couponapi;

import com.example.couponapi.dto.CouponIssueResponseDto;
import com.example.couponcore.exception.CouponissueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CouponControllerAdvice {

    @ExceptionHandler(CouponissueException.class)
    public CouponIssueResponseDto couponIssueExceptionHandler(CouponissueException e) {
        return new CouponIssueResponseDto(false,e.getErrorCode().message);
    }
}
