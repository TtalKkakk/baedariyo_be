package com.house.biet.payment.query.repository;

import com.house.biet.common.domain.enums.PaymentStatus;
import com.house.biet.order.command.domain.aggregate.QOrder;
import com.house.biet.order.command.domain.vo.QOrderMenu;
import com.house.biet.payment.command.domain.aggregate.QPayment;
import com.house.biet.payment.query.application.dto.MyPaymentDetailResponseDto;
import com.house.biet.payment.query.application.dto.MyPaymentSearchCondition;
import com.house.biet.store.command.domain.aggregate.QStore;
import com.house.biet.store.command.domain.entity.QStoreReview;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PaymentQueryRepositoryQuerydsl {

    private final JPAQueryFactory jpaQueryFactory;

    public List<MyPaymentDetailResponseDto> findMyPaymentDetailList(
            MyPaymentSearchCondition condition
    ) {

        QPayment payment = QPayment.payment;
        QOrder order = QOrder.order;
        QStore store = QStore.store;
        QStoreReview review = QStoreReview.storeReview;
        QOrderMenu orderMenu = QOrderMenu.orderMenu;

        // 메인 조회
        List<Tuple> paymentTuples = jpaQueryFactory
                .select(
                        payment.id,
                        store.storeName.value,
                        payment.status,
                        payment.money.amount,
                        payment.createdAt,
                        order.id
                )
                .from(payment)
                .join(order).on(payment.orderId.eq(order.id))
                .join(store).on(order.storeId.eq(store.id))
                .where(
                        payment.userId.eq(condition.userId()),
                        statusEq(condition.paymentStatus())
                )
                .orderBy(payment.createdAt.desc())
                .fetch();

        return paymentTuples.stream()
                .map(tuple -> {

                    Long orderId = tuple.get(order.id);

                    // 주문 메뉴 조회
                    List<MyPaymentDetailResponseDto.OrderMenuResponse> menus =
                            jpaQueryFactory
                                    .select(Projections.constructor(
                                            MyPaymentDetailResponseDto.OrderMenuResponse.class,
                                            orderMenu.orderMenuName.value,
                                            orderMenu.quantity,
                                            orderMenu.menuPrice.amount
                                    ))
                                    .from(order)
                                    .join(order.menus, orderMenu)
                                    .where(order.id.eq(orderId))
                                    .fetch();

                    // 리뷰 단건 조회
                    Tuple reviewTuple = jpaQueryFactory
                            .select(
                                    review.rating,
                                    review.storeReviewComment.comment
                            )
                            .from(review)
                            .where(review.orderId.eq(orderId))
                            .fetchOne();

                    Integer rating = reviewTuple != null
                            ? reviewTuple.get(review.rating)
                            : null;

                    String reviewComment = reviewTuple != null
                            ? reviewTuple.get(review.storeReviewComment.comment)
                            : null;

                    // 리뷰 이미지 조회
                    List<String> images = jpaQueryFactory
                            .select(review.storeReviewImages.images)
                            .from(review)
                            .where(review.orderId.eq(orderId))
                            .fetchOne();

                    if (images == null) {
                        images = Collections.emptyList();
                    }

                    return new MyPaymentDetailResponseDto(
                            tuple.get(store.storeName.value),
                            tuple.get(payment.status),
                            rating,
                            menus,
                            reviewComment,
                            images,
                            tuple.get(payment.money.amount),
                            tuple.get(payment.createdAt)
                    );
                })
                .toList();
    }

    private BooleanExpression statusEq(PaymentStatus status) {
        return status != null
                ? QPayment.payment.status.eq(status)
                : null;
    }
}