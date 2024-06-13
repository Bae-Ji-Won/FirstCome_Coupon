package com.example.couponcore.service;

import com.example.couponcore.exception.CouponissueException;
import com.example.couponcore.model.Coupon;
import com.example.couponcore.model.CouponIssue;
import com.example.couponcore.repository.mysql.CouponIssueJpaRepository;
import com.example.couponcore.repository.mysql.CouponIssueRepository;
import com.example.couponcore.repository.mysql.CouponJpaRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.couponcore.exception.ErrorCode.COUPON_ALREADY_EXISTS;
import static com.example.couponcore.exception.ErrorCode.DUPLICATE_COUPON_ISSUE;

@RequiredArgsConstructor
@Service
public class CouponIssueService {

    private final CouponJpaRepository couponJpaRepository;
    private final CouponIssueJpaRepository couponIssueJpaRepository;
    private final CouponIssueRepository couponIssueRepository;

    // 쿠폰 이슈 발생
    @Transactional
    public void issue(long couponId, long userId){
        Coupon coupon = findCoupon(couponId);
        coupon.issue();

        saveCouponIssue(couponId,userId);
    }
    
    // 현재 쿠폰의 존재 여부 확인
    @Transactional(readOnly = true)
    public Coupon findCoupon(long couponId){
        return couponJpaRepository.findById(couponId).orElseThrow(() -> {
            throw  new CouponissueException(COUPON_ALREADY_EXISTS, "쿠폰 정책이 존재하지 않습니다. %s".formatted(couponId));
        });
    }

    // 쿠폰 발급 받은 유저 정보 저장
    @Transactional
    public CouponIssue saveCouponIssue(long couponId, long userId){
        checkAlreadyIssuance(couponId,userId);
        CouponIssue issue = CouponIssue.builder()
                .couponId(couponId)
                .userId(userId)
                .build();

        return couponIssueJpaRepository.save(issue);
    }
    
    // 이미 쿠폰을 발급 받았는지 확인
    private void checkAlreadyIssuance(long couponId, long userId){
       CouponIssue issue = couponIssueRepository.findFirstCouponIssue(couponId,userId);
       // 이미 발급한 상태
       if(issue != null){
           throw new CouponissueException(DUPLICATE_COUPON_ISSUE,"이미 발급된 쿠폰입니다. userId : %s, coupon_id : %s".formatted(userId,couponId));
       }
    }
}
