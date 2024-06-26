package com.example.couponcore.service;

import com.example.couponcore.model.Coupon;
import com.example.couponcore.repository.redis.dto.CouponRedisEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CouponCacheService {

    private final CouponIssueService couponIssueService;

    // 캐쉬 저장 (쿠폰 기본 정보에 대한 값 캐시에 저장 - 쿠폰 id, 쿠폰 총 수량, 쿠폰 발행일, 쿠폰 마감일)
    @Cacheable(cacheNames = "coupon")
    public CouponRedisEntity getCouponCache(long couponId){
        Coupon coupon = couponIssueService.findCoupon(couponId);
        return new CouponRedisEntity(coupon);
    }
}
