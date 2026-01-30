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

    // User
    USER_UPDATE_SUCCESS(22000, "회원 정보가 수정되었습니다.");

    private final int code;
    private final String message;
}
