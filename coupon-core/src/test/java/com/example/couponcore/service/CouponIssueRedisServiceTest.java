package com.example.couponcore.service;

import com.example.couponcore.TestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import java.util.Collection;
import java.util.stream.IntStream;

import static com.example.couponcore.util.CouponRedisUtils.getIssueRequestKey;

class CouponIssueRedisServiceTest extends TestConfig {

    @Autowired
    CouponIssueRedisService sut;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    // Redis 초기화
    @BeforeEach
    void clear(){
        Collection<String> redisKeys = redisTemplate.keys("*");
        redisTemplate.delete(redisKeys);
    }

    @Test
    @DisplayName("쿠폰 수량 검증 - 발급 가능 수량이 존재하면 true를 반환")
    void availableTotalIssueQuantity_1(){
        // given
        int totalIssueQuantity = 10;
        long couponId = 1;
        // when
        boolean result = sut.availableTotalIssueQuantity(totalIssueQuantity,couponId);
        // then
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("쿠폰 수량 검증 - 발급 가능 수량이 없다면 false를 반환")
    void availableTotalIssueQuantity_2(){
        // given
        int totalIssueQuantity = 10;
        long couponId = 1;
        // Redis에 값 10개 미리 채워놓기
        IntStream.range(0,totalIssueQuantity).forEach(userId -> {
            redisTemplate.opsForSet().add(getIssueRequestKey(couponId), String.valueOf(userId));
        });
        // when
        boolean result = sut.availableTotalIssueQuantity(totalIssueQuantity,couponId);
        // then
        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("쿠폰 중복 발급 검증 - 발급된 내역에 유저가 존재하지 않으면 true를 반환한다")
    void availableTotalIssueQuantity_3() {
        // given
        long couponId = 1;
        long userId = 1;
        // when
        boolean result = sut.availableUserIssueQuantity(couponId,userId);
        // then
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("쿠폰 중복 발급 검증 - 발급된 내역에 유저가 존재한다면 false를 반환한다")
    void availableTotalIssueQuantity_4() {
        // given
        long couponId = 1;
        long userId = 1;
        redisTemplate.opsForSet().add(getIssueRequestKey(couponId),String.valueOf(userId));
        // when
        boolean result = sut.availableUserIssueQuantity(couponId,userId);
        // then
        Assertions.assertFalse(result);
    }
}