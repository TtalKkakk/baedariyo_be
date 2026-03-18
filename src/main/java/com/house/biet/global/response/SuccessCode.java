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
    PASSWORD_CHANGE_SUCCESS(21004, "비밀번호가 변경되었습니다."),

    // User
    USER_UPDATE_SUCCESS(22000, "회원 정보가 수정되었습니다."),
    USER_NICKNAME_CHANGE_SUCCESS(22001, "닉네임이 변경되었습니다."),
    USER_PHONE_NUMBER_CHANGE_SUCCESS(22002, "전화번호가 변경되었습니다."),
    USER_ADDRESS_ADD_SUCCESS(22003, "배송지가 추가되었습니다."),
    USER_DEFAULT_ADDRESS_CHANGE_SUCCESS(22004, "기본 배송지가 변경되었습니다."),
    USER_ADDRESS_REMOVE_SUCCESS(22005, "배송지가 삭제되었습니다."),
    USER_ADDRESS_LIST_SUCCESS(22006, "모든 배송지가 조회되었습니다."),
    USER_ADDRESS_ALIAS_CHANGE_SUCCESS(22007, "배송지 별명이 변경되었습니다."),

    // Order
    ORDER_CREATE_SUCCESS(20300, "주문이 생성되었습니다."),
    ORDER_RIDER_ASSIGN_SUCCESS(20301, "주문에 라이더가 배정되었습니다."),

    // Store
    STORE_CREATE_SUCCESS(20400, "가게가 생성되었습니다."),
    STORE_UPDATE_SUCCESS(20401, "가게 정보가 수정되었습니다."),
    STORE_DELETE_SUCCESS(20402, "가게가 삭제되었습니다."),
    STORE_GET_LIST_SUCCESS(20403, "가게 목록 조회에 성공했습니다."),

    // Store Review
    STORE_REVIEW_CREATE_SUCCESS(20500, "리뷰가 생성되었습니다."),
    STORE_REVIEW_DELETE_SUCCESS(20501, "리뷰가 삭제되었습니다."),

    // Payment
    PAYMENT_CREATE_SUCCESS(20600, "결제가 생성되었습니다."),
    PAYMENT_APPROVE_SUCCESS(20601, "결제가 승인되었습니다."),
    PAYMENT_FAIL_SUCCESS(20602, "결제가 실패되었습니다."),
    PAYMENT_CANCEL_SUCCESS(20603, "결제가 취소되었습니다."),
    PAYMENT_GET_SUCCESS(20604, "결제 단건 조회 성공하였습니다."),
    PAYMENT_GET_LIST_SUCCESS(20605, "결제 목록 조회 성공하였습니다."),

    // Delivery
    DELIVERY_CREATE_SUCCESS(20700, "배달이 생성되었습니다."),
    DELIVERY_RIDER_ASSIGN_SUCCESS(20701, "라이더가 배정되었습니다."),
    DELIVERY_PICKUP_SUCCESS(20702, "픽업이 완료되었습니다."),
    DELIVERY_START_SUCCESS(20703, "배달이 시작되었습니다."),
    DELIVERY_COMPLETE_SUCCESS(20704, "배달이 완료되었습니다."),
    DELIVERY_GET_SUCCESS(20705, "배달 조회에 성공했습니다."),
    DELIVERY_GET_LIST_SUCCESS(20706, "배달 목록 조회에 성공했습니다."),

    // Store Search
    SEARCH_AUTOCOMPLETE_SUCCESS(20800, "자동완성 검색에 성공했습니다."),
    SEARCH_POPULAR_SUCCESS(20801, "인기 검색어 조회에 성공했습니다."),
    SEARCH_RECENT_SUCCESS(20802, "최근 검색어 조회에 성공했습니다."),
    SEARCH_RECENT_DELETE_SUCCESS(20803, "최근 검색어 삭제에 성공했습니다."),
    SEARCH_RECENT_DELETE_ALL_SUCCESS(20804, "최근 검색어 전체 삭제에 성공했습니다.");

    private final int code;
    private final String message;
}
