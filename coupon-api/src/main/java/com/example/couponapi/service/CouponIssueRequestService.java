package com.example.couponapi.service;


import com.example.couponapi.dto.CouponIssueRequestDto;
import com.example.couponcore.component.DistributeLockExecutor;
import com.example.couponcore.service.AsyncCouponIssueServiceV1;
import com.example.couponcore.service.CouponIssueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class CouponIssueRequestService {

    private final CouponIssueService couponIssueService;
    private final DistributeLockExecutor distributeLockExecutor;
    private final AsyncCouponIssueServiceV1 asyncCouponIssueServiceV1;

    public void issueRequestV1(CouponIssueRequestDto requestDto) {
        couponIssueService.issue(requestDto.couponId(), requestDto.userId());


        // Redisson를 활용한 Lock
//        distributeLockExecutor.execute("lock_" + requestDto.couponId(),10000,10000,() -> {
//            couponIssueService.issue(requestDto.couponId(), requestDto.userId());
//        });

        // synchronized를 활용한 Lock
        synchronized (this) {
            couponIssueService.issue(requestDto.couponId(), requestDto.userId());
        }

        // Mysql를 통한 Lock(CouponJpaRepository에서 Lock구현)
//        couponIssueService.issue(requestDto.couponId(), requestDto.userId());
        log.info("쿠폰 발급 완료. couponId: %s, userId: %s".formatted(requestDto.couponId(), requestDto.userId()));
    }


    public void asyncIssueRequestV1 (CouponIssueRequestDto requestDto){
        asyncCouponIssueServiceV1.issue(requestDto.couponId(), requestDto.userId());
    }
}
