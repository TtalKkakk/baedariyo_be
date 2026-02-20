package com.house.biet.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {

    // 공통
    OK(20000, "요청이 성공적으로 처리되었습니다."),

    // Auth
    SIGNUP_SUCCESS(21000, "회원가입이 완료되었습니다."),
    LOGIN_SUCCESS(21001, "로그인에 성공했습니다."),
    LOGOUT_SUCCESS(21002, "로그아웃 되었습니다."),
    WITHDRAW_SUCCESS(21003, "회원탈퇴가 완료되었습니다."),

    // User
    USER_UPDATE_SUCCESS(22000, "회원 정보가 수정되었습니다."),

    // Order
    ORDER_CREATE_SUCCESS(20300, "주문이 생성되었습니다."),
    ORDER_RIDER_ASSIGN_SUCCESS(20301, "주문에 라이더가 배정되었습니다."),

    // Store
    STORE_CREATE_SUCCESS(20400, "가게가 생성되었습니다."),
    STORE_UPDATE_SUCCESS(20401, "가게 정보가 수정되었습니다."),
    STORE_DELETE_SUCCESS(20402, "가게가 삭제되었습니다."),

    // Store Review
    STORE_REVIEW_CREATE_SUCCESS(20500, "리뷰가 생성되었습니다."),
    STORE_REVIEW_DELETE_SUCCESS(20501, "리뷰가 삭제되었습니다.");

    private final int code;
    private final String message;
}
