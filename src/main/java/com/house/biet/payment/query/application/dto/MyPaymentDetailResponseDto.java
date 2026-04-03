package com.house.biet.payment.query.application.dto;

import com.house.biet.common.domain.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;

public record MyPaymentDetailResponseDto(

        Long paymentId,
        String storePublicId,
        Long orderId,

        String storeName,
        PaymentStatus paymentStatus,
        Integer rating,
        List<OrderMenuResponse> orderMenus,
        String storeReviewComment,
        List<String> storeImages,
        Integer amount,
        LocalDateTime createdAt

) {

    public record OrderMenuResponse(
            String menuName,
            int quantity,
            int price
    ) {}
}