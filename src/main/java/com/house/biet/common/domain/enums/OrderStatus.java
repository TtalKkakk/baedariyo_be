package com.house.biet.common.domain.enums;

public enum OrderStatus {
    ORDERED,    // 주문 접수
    PAID,       // 결제 완료
    CANCELLED,  // 주문 취소
    DELIVERING, // 배달 중
    DELIVERED   // 배달 완료
}
