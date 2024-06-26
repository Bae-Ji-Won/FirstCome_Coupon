package com.example.couponcore.repository.redis.dto;

import com.example.couponcore.exception.CouponissueException;
import com.example.couponcore.exception.ErrorCode;
import com.example.couponcore.model.Coupon;
import com.example.couponcore.model.CouponType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

import static com.example.couponcore.exception.ErrorCode.INVALID_COUPON_ISSUE_DATE;

public record CouponRedisEntity(
        Long id,
        CouponType couponType,
        Integer totalQuantity,

        // @JsonSerialize: Java 객체를 JSON 문자열로 변환할 때 사용되는 어노테이션입니다.
        // @JsonDeserialize: JSON 문자열을 Java 객체로 변환할 때 사용되는 어노테이션입니다.
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        LocalDateTime dateIssueStart,

        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        LocalDateTime dateIssueEnd
) {

    public CouponRedisEntity(Coupon coupon){
        this(
                coupon.getId(),
                coupon.getCouponType(),
                coupon.getTotalQuantity(),
                coupon.getDateIssueStart(),
                coupon.getDateIssueEnd()
        );
    }

    // 쿠폰 시간 만료 검사
    private boolean availableIssueDate(){
        LocalDateTime now = LocalDateTime.now();
        return dateIssueStart.isBefore(now) && dateIssueEnd.isAfter(now);
    }

    public void checkIssuableCoupon(){
        if(!availableIssueDate()){
            throw new CouponissueException(INVALID_COUPON_ISSUE_DATE, "발급 가능한 일자가 아닙니다 coupon_id :%s".formatted(id));
        }
    }
}
