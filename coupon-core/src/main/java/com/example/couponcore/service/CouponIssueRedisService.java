package com.example.couponcore.service;

import com.example.couponcore.exception.CouponissueException;
import com.example.couponcore.model.CouponIssue;
import com.example.couponcore.repository.redis.RedisRepository;
import com.example.couponcore.repository.redis.dto.CouponRedisEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.couponcore.exception.ErrorCode.DUPLICATE_COUPON_ISSUE;
import static com.example.couponcore.exception.ErrorCode.INVALID_COUPON_ISSUE_QUANTITY;
import static com.example.couponcore.util.CouponRedisUtils.getIssueRequestKey;

@RequiredArgsConstructor
@Service
public class CouponIssueRedisService {

    private final RedisRepository repository;

    public void checkCouponIssueQuantity(CouponRedisEntity coupon, long userId){
        // 쿠폰을 받은 유저 정보 조회
        if(!availableUserIssueQuantity(coupon.id(),userId)){
            throw new CouponissueException(DUPLICATE_COUPON_ISSUE,"이미 발급 받은 쿠폰입니다. coupon_Id:%s".formatted(coupon.id()));
        }

        if(!availableTotalIssueQuantity(coupon.totalQuantity(),coupon.id())){
            throw new CouponissueException(INVALID_COUPON_ISSUE_QUANTITY,"발급 가능한 수량이 없습니다.");
        }

    }

    // 중복 요청 검증
    public boolean availableUserIssueQuantity(long couponId, long userId) {
        String key = getIssueRequestKey(couponId);
        return !repository.sIsMember(key,String.valueOf(userId));
    }

    // 수량에 대한 검증
    public boolean availableTotalIssueQuantity(Integer totalQuantity, long couponId) {
        if(totalQuantity == null){
            return true;
        }

        String key = getIssueRequestKey(couponId);
        return totalQuantity > repository.sCard(key);
    }
}
