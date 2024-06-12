package com.example.couponcore.model;

import com.example.couponcore.exception.CouponissueException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.example.couponcore.exception.ErrorCode.INVALID_COUPON_ISSUE_DATE;
import static com.example.couponcore.exception.ErrorCode.INVALID_COUPON_ISSUE_QUANTITY;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "coupons")
public class Coupon extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CouponType couponType;

    private Integer totalQuantity;  // 전체 개수

    @Column(nullable = false)
    private int issuedQuantity;     // 현재 발급된 개수

    @Column(nullable = false)
    private int discountAmount;     // 쿠폰 할인 가격

    @Column(nullable = false)
    private int minAvailableAmount; // 쿠폰 최소 사용 금액

    @Column(nullable = false)
    private LocalDateTime dateIssueStart;

    @Column(nullable = false)
    private LocalDateTime dateIssueEnd;

    // 쿠폰 개수 확인
    public boolean availableIssueQuantity(){
        if(totalQuantity == null){
            return true;
        }

        return totalQuantity > issuedQuantity;
    }

    // 쿠폰 발급 기한 확인
    public boolean availableIssueDate(){
        LocalDateTime now = LocalDateTime.now();
        return dateIssueStart.isBefore(now) && dateIssueEnd.isAfter(now);
    }
    
    // 이슈 발생시 쿠폰 발급 개수 증가
    public void issue(){
        if(!availableIssueQuantity()){
            throw new CouponissueException(INVALID_COUPON_ISSUE_QUANTITY, "Not enough coupons. total : %s, issued : %s".formatted(totalQuantity,issuedQuantity));
        }
        if(!availableIssueDate()){
            throw new CouponissueException(INVALID_COUPON_ISSUE_DATE, "Date not available. request : %s, issueStart : %s, issueEnd : %s".formatted(LocalDateTime.now(),dateIssueStart,dateIssueEnd));
        }
        issuedQuantity++;
    }
}
