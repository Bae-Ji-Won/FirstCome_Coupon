package com.example.couponcore.service;

import com.example.couponcore.TestConfig;
import com.example.couponcore.exception.CouponissueException;
import com.example.couponcore.exception.ErrorCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;

import static com.example.couponcore.exception.ErrorCode.COUPON_ALREADY_EXISTS;
import static org.junit.jupiter.api.Assertions.*;

class AsyncCouponIssueServiceV1Test extends TestConfig {

    @Autowired
    AsyncCouponIssueServiceV1 sut;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @BeforeEach
    void clear(){
        Collection<String> redisKeys = redisTemplate.keys("coupon:*");
        redisTemplate.delete(redisKeys);
    }

    @Test
    @DisplayName("쿠폰 발급 - 쿠폰이 존재하지 않는다면 예외르 반환한다.")
    void test() {
        // given
         long couponId = 1;
         long userId = 1;
        // when
        CouponissueException exception = Assertions.assertThrows(CouponissueException.class,() -> {
            sut.issue(couponId,userId);
        });
        // then
        Assertions.assertEquals(exception.getErrorCode(), COUPON_ALREADY_EXISTS);
    }
    
}