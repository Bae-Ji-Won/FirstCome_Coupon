package com.example.couponapi;

import com.example.couponcore.CouponCoreConfiguration;
import com.example.couponcore.model.Coupon;
import com.example.couponcore.model.CouponType;
import com.example.couponcore.repository.mysql.CouponJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;

@Import(CouponCoreConfiguration.class)
@SpringBootApplication
@RequiredArgsConstructor
public class CouponApiApplication implements CommandLineRunner {

    private final CouponJpaRepository couponJpaRepository;

    public static void main(String[] args) {
        System.setProperty("spring.config.name", "application-core,application-api");
        SpringApplication.run(CouponApiApplication.class, args);
    }
    
    // 기본 데이터 삽입
    @Override
    public void run(String... args) throws Exception {
        if(couponJpaRepository.count() == 0) {
            Coupon coupon = Coupon.builder()
                    .title("네고왕 선착순")
                    .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                    .totalQuantity(10)
                    .issuedQuantity(0)
                    .discountAmount(100000)
                    .minAvailableAmount(110000)
                    .dateIssueStart(LocalDateTime.now().minusDays(1))
                    .dateIssueEnd(LocalDateTime.now().plusDays(2))
                    .build();

            couponJpaRepository.save(coupon);
        }
    }
}

