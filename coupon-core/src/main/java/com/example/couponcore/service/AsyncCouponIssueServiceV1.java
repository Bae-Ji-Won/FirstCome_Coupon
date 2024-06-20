package com.example.couponcore.service;

import com.example.couponcore.exception.CouponissueException;
import com.example.couponcore.exception.ErrorCode;
import com.example.couponcore.model.Coupon;
import com.example.couponcore.repository.redis.RedisRepository;
import com.example.couponcore.repository.redis.dto.CouponIssueRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.couponcore.exception.ErrorCode.*;
import static com.example.couponcore.util.CouponRedisUtils.getIssueRequestKey;
import static com.example.couponcore.util.CouponRedisUtils.getIssueRequestQueueKey;


@RequiredArgsConstructor
@Service
public class AsyncCouponIssueServiceV1 {

    private final RedisRepository redisRepository;
    private final CouponIssueRedisService couponIssueRedisService;
    private final CouponIssueService couponIssueService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /* Redis Sorted Set를 통한 관리
     1. Sorted Set에 요청을 추가
        - ZADD의 응답 값 기반 중복 발급 검증
     2. 현재 요청의 순서 조회(ZRANK) 및 발급 성공 여부 응답
     3. 발급에 성공했다면 쿠폰 발급 Queue에 적재
     */
//    public void issue(long couponId, long userId){
//        // 유저의 요청을 sorted set에 적재
//        String key=  "issue.request.sorted_set.couponId=%s".formatted(couponId);
//        redisRepository.zAdd(key,String.valueOf(userId),System.currentTimeMillis());
//    }

    /*
        Redis Sort를 통한 관리
        쿠폰 발급 조건 검증 및 Reids 데이터 저장
     */
    public void issue(long couponId, long userId){
        Coupon coupon = couponIssueService.findCoupon(couponId);
        // 쿠폰 발급 일자 에 대한 조회
        if(!coupon.availableIssueDate()){
            throw new CouponissueException(INVALID_COUPON_ISSUE_DATE,"발급 가능한 일자가 아닙니다. couponId:%s, issueStart:%s,issueEnd:%s".formatted(couponId,coupon.getDateIssueStart(),coupon.getDateIssueEnd()));
        }

        // 쿠폰 총 개수 조회
        if(!couponIssueRedisService.availableTotalIssueQuantity(coupon.getTotalQuantity(),couponId)){
            throw new CouponissueException(INVALID_COUPON_ISSUE_QUANTITY,"발급 가능한 수량이 없습니다. coupon_Id:%s".formatted(couponId));
        }
        
        // 쿠폰을 받은 유저 정보 조회
        if(!couponIssueRedisService.availableUserIssueQuantity(coupon.getTotalQuantity(),userId)){
            throw new CouponissueException(DUPLICATE_COUPON_ISSUE,"이미 발급 받은 쿠폰입니다. coupon_Id:%s".formatted(couponId));
        }
        issueRequest(couponId,userId);
    }

    /* Redis에 데이터를 저장하는 구조
        1. Redis set에 데이터를 저장하여 전체적인 요청과 발급 수량을 체크함
        2. Redis Queue에 데이터를 저장하여 쿠폰 발급 대기열 기능에 사용할 수 있음
     */
    private void issueRequest(long couponId, long userId){
        // Redis Queue에 저장할때 couponId, userId를 같이 저장하기 위해 requestDto 사용
        CouponIssueRequest issueRequest = new CouponIssueRequest(couponId, userId);
        try {
            // 1. 요청과 발급수량에 대해 제어를 하기 위해 사용
            redisRepository.sAdd(getIssueRequestKey(couponId), String.valueOf(userId));

            // 2. 쿠폰 발급 대기열에 사용하기 위해 Queue 사용(위에와 키값이 달라야함)
            // Redis Queue에 list형식으로 데이터를 넣기 위해서는 RequestDto를 직렬화 하여 넣어줘야함
            String value = objectMapper.writeValueAsString(issueRequest);
            redisRepository.rPush(getIssueRequestQueueKey(),value);
        } catch (JsonProcessingException e) {
            throw new CouponissueException(FAIL_COUPON_ISSUE_REQUEST,"input: %s".formatted(issueRequest));
        }
    }
}
