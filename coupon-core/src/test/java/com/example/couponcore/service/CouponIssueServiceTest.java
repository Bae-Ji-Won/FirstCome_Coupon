package com.example.couponcore.service;

import com.example.couponcore.TestConfig;
import com.example.couponcore.exception.CouponissueException;
import com.example.couponcore.exception.ErrorCode;
import com.example.couponcore.model.Coupon;
import com.example.couponcore.model.CouponIssue;
import com.example.couponcore.model.CouponType;
import com.example.couponcore.repository.mysql.CouponIssueJpaRepository;
import com.example.couponcore.repository.mysql.CouponIssueRepository;
import com.example.couponcore.repository.mysql.CouponJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static com.example.couponcore.exception.ErrorCode.*;


class CouponIssueServiceTest extends TestConfig {

    @Autowired
    CouponIssueService service;

    @Autowired
    CouponIssueJpaRepository couponIssueJpaRepository;

    @Autowired
    CouponIssueRepository  couponIssueRepository;

    @Autowired
    CouponJpaRepository couponJpaRepository;

    @BeforeEach
    void clean(){
        // 데이터베이스에서 일괄적으로 모든 쿠폰 데이터를 삭제하는 기능
        // Batch 처리는 여러 개의 SQL 쿼리를 한 번에 실행하여 성능을 향상시킵니다.
        couponJpaRepository.deleteAllInBatch();
        couponIssueJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("쿠폰 발급 내역이 존재하면 예외를 반환한다.")
    void saveCouponIssue(){
        // given
        CouponIssue couponIssue = CouponIssue.builder()
                .couponId(1L)
                .userId(1L)
                .build();
        couponIssueJpaRepository.save(couponIssue);
        // when
        CouponissueException exception = Assertions.assertThrows(CouponissueException.class, () ->{
            service.saveCouponIssue(couponIssue.getCouponId(), couponIssue.getUserId());
        });
        // then
        Assertions.assertEquals(exception.getErrorCode(), DUPLICATE_COUPON_ISSUE);
    }

    @Test
    @DisplayName("쿠폰 발급 내역이 존재하지 않는다면 쿠폰을 발급한다.")
    void saveCouponIssue_2(){
        // given
        long couponId = 1L;
        long userId = 1L;
        // when
        CouponIssue result = service.saveCouponIssue(couponId,userId);
        // then
        Assertions.assertTrue(couponIssueJpaRepository.findById(result.getId()).isPresent());
    }

    @Test
    @DisplayName("발급 수량, 기한, 중복 발급 문제가 없다면 쿠폰을 발급한다")
    void issue_1(){
        // given
        long userId = 1L;
        Coupon coupon = Coupon.builder()
                .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                .title("선착순 테스트 쿠폰")
                .totalQuantity(100)
                .issuedQuantity(0)
                .dateIssueStart(LocalDateTime.now().minusDays(1))
                .dateIssueEnd(LocalDateTime.now().plusDays(1))
                .build();
        couponJpaRepository.save(coupon);

        // when
        service.issue(coupon.getId(),userId);
        // then
        // 현재 쿠폰 발급 개수 체크(처음으로 발급 해줬기 때문에 1개)
        Coupon couponResult = couponJpaRepository.findById(coupon.getId()).get();
        Assertions.assertEquals(couponResult.getIssuedQuantity(), 1);

        // 쿠폰을 이미 발급 받은 사람인지 체크(위에서 발급 해줬기 때문에 null이면 안됨)
        CouponIssue couponIssueResult = couponIssueRepository.findFirstCouponIssue(coupon.getId(),userId);
        Assertions.assertNotNull(couponIssueResult);
    }

    @Test
    @DisplayName("발급 수량에 문제가 있다면 예외를 반환")
    void issue_2(){
        // given
        long userId = 1L;
        Coupon coupon = Coupon.builder()
                .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                .title("선착순 테스트 쿠폰")
                .totalQuantity(100)
                .issuedQuantity(100)
                .dateIssueStart(LocalDateTime.now().minusDays(1))
                .dateIssueEnd(LocalDateTime.now().plusDays(1))
                .build();
        couponJpaRepository.save(coupon);

        // when & then
        CouponissueException exception = Assertions.assertThrows(CouponissueException.class, () ->{
            service.issue(coupon.getId(),userId);
        });
        Assertions.assertEquals(exception.getErrorCode(), INVALID_COUPON_ISSUE_QUANTITY);
    }

    @Test
    @DisplayName("발급 기한에 문제가 있다면 예외를 반환")
    void issue_3(){
        // given
        long userId = 1L;
        Coupon coupon = Coupon.builder()
                .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                .title("선착순 테스트 쿠폰")
                .totalQuantity(100)
                .issuedQuantity(0)
                .dateIssueStart(LocalDateTime.now().minusDays(2))
                .dateIssueEnd(LocalDateTime.now().minusDays(1))
                .build();
        couponJpaRepository.save(coupon);

        // when & then
        CouponissueException exception = Assertions.assertThrows(CouponissueException.class, () ->{
            service.issue(coupon.getId(),userId);
        });
        Assertions.assertEquals(exception.getErrorCode(), INVALID_COUPON_ISSUE_DATE);
    }

    @Test
    @DisplayName("중복 발급 검증에 문제가 있다면 예외를 반환")
    void issue_4(){
        // given
        // 쿠폰 생성
        long userId = 1L;
        Coupon coupon = Coupon.builder()
                .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                .title("선착순 테스트 쿠폰")
                .totalQuantity(100)
                .issuedQuantity(0)
                .dateIssueStart(LocalDateTime.now().minusDays(2))
                .dateIssueEnd(LocalDateTime.now().plusDays(1))
                .build();
        couponJpaRepository.save(coupon);
        
        // 해당 유저 쿠폰 발급 받았다고 저장
        CouponIssue couponIssue = CouponIssue.builder()
                .couponId(coupon.getId())
                .userId(userId)
                .build();
        couponIssueJpaRepository.save(couponIssue);

        // when & then
        CouponissueException exception = Assertions.assertThrows(CouponissueException.class, () ->{
            service.issue(coupon.getId(),userId);
        });
        Assertions.assertEquals(exception.getErrorCode(), DUPLICATE_COUPON_ISSUE);
    }

    @Test
    @DisplayName("쿠폰이 존재하지 않는다면 예외를 반환")
    void issue_5(){
        // given
        long userId = 1L;
        long couponId = 1L;

        // when & then
        CouponissueException exception = Assertions.assertThrows(CouponissueException.class, () ->{
            service.issue(couponId,userId);
        });
        Assertions.assertEquals(exception.getErrorCode(),COUPON_ALREADY_EXISTS);
    }
}