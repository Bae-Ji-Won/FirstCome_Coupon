package com.example.couponconsumer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

// core에서 RedisConfirguration를 인식하지 못하는 경우 방지
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.config.name=application-core")


@SpringBootTest
class CouponConsumerApplicationTests {

    @Test
    void contextLoads() {
    }

}
