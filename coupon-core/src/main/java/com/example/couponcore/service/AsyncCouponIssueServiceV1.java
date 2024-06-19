package com.example.couponcore.service;

import com.example.couponcore.repository.redis.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class AsyncCouponIssueServiceV1 {

    private final RedisRepository redisRepository;


    /* Redis
     1. Sorted Set에 요청을 추가
        - ZADD의 응답 값 기반 중복 발급 검증
     2. 현재 요청의 순서 조회(ZRANK) 및 발급 성공 여부 응답
     3. 발급에 성공했다면 쿠폰 발급 Queue에 적재
     */
    public void issue(long couponId, long userId){
        // 1. 유저의 요청을 sorted set에 적재
        String key=  "issue.request.sorted_set.couponId=%s".formatted(couponId);
        redisRepository.zAdd(key,String.valueOf(userId),System.currentTimeMillis());

        // 2. 유저의 요청의 순서를 조회
        // 3. 조회 결과를 선착순 조건과 비교
        // 4. 쿠폰 발급 queue에 적재

    }
}
