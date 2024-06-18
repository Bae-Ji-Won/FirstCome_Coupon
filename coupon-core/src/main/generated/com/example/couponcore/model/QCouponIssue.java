package com.example.couponcore.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCouponIssue is a Querydsl query type for CouponIssue
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCouponIssue extends EntityPathBase<CouponIssue> {

    private static final long serialVersionUID = -960330154L;

    public static final QCouponIssue couponIssue = new QCouponIssue("couponIssue");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    public final NumberPath<Long> couponId = createNumber("couponId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> dateIssued = createDateTime("dateIssued", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> dateUsed = createDateTime("dateUsed", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QCouponIssue(String variable) {
        super(CouponIssue.class, forVariable(variable));
    }

    public QCouponIssue(Path<? extends CouponIssue> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCouponIssue(PathMetadata metadata) {
        super(CouponIssue.class, metadata);
    }

}

