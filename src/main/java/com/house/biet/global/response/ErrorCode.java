package com.house.biet.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 400 Bad Request
    INVALID_EMAIL_FORMAT(40101, HttpStatus.BAD_REQUEST, "잘못된 이메일 형식입니다,"),
    INVALID_PASSWORD_FORMAT(40102, HttpStatus.BAD_REQUEST, "잘못된 비밀번호 형식입니다."),
    NOT_CORRECT_PASSWORD(40103, HttpStatus.BAD_REQUEST, "비밀번호가 올바르지 않습니다."),
    ALREADY_EXIST_EMAIL_AND_ROLE(40104, HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다"),
    INVALID_REAL_NAME_FORMAT(40105, HttpStatus.BAD_REQUEST, "잘못된 사용자 이름 형식입니다."),
    INVALID_NICK_NAME_FORMAT(40106, HttpStatus.BAD_REQUEST, "잘못된 사용자 닉네임 형식입니다."),
    INVALID_PHONE_NUMBER_FORMAT(40107, HttpStatus.BAD_REQUEST, "잘못된 휴대전화번호 형식입니다."),
    ALREADY_WITHDRAWN_ACCOUNT(40108, HttpStatus.BAD_REQUEST, "이미 삭제된 계정입니다."),
    INVALID_MONEY_AMOUNT(40109, HttpStatus.BAD_REQUEST, "금액은 음수일 수 없습니다."),
    INVALID_MENU_QUANTITY(40110, HttpStatus.BAD_REQUEST, "메뉴 수량은 1 이상이어야 합니다."),
    INVALID_MENU_NAME_FORMAT(40111, HttpStatus.BAD_REQUEST, "잘못된 메뉴 이름 형식입니다."),
    EMPTY_ORDER_MENU(40112, HttpStatus.BAD_REQUEST, "주문할 메뉴가 존재하지 않습니다."),
    INVALID_STORE_ID(40113, HttpStatus.BAD_REQUEST, "올바르지 않은 가게입니다."),
    INVALID_ORDER_STATUS(40114, HttpStatus.BAD_REQUEST, "잘못된 주문 배달 상태입니다."),
    INVALID_ORDER_PAYMENT(40115, HttpStatus.BAD_REQUEST, "주문 취소를 할 수 없습니다."),
    INVALID_ORDER_CANCEL(40116, HttpStatus.BAD_REQUEST, "결제를 할 수 없습니다."),
    INVALID_ORDER_DATA(40117, HttpStatus.BAD_REQUEST, "결제 데이터가 올바르지 않습니다."),
    BAD_REQUEST(40100, HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    // 401 Unauthorized

    UNAUTHORIZED(40100, HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),

    // 403 Forbidden

    FORBIDDEN(40300, HttpStatus.FORBIDDEN, "권한이 없습니다."),

    // 404 Not Found
    ACCOUNT_NOT_FOUND(40401, HttpStatus.NOT_FOUND, "계정을 찾을 수 없습니다."),
    USER_NOT_FOUND(40402, HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    RIDER_NOT_FOUND(40403, HttpStatus.NOT_FOUND, "라이더를 찾을 수 없습니다."),
    NOT_FOUND(40400, HttpStatus.NOT_FOUND, "찾을 수 없습니다."),

    // 409 Conflict

    DUPLICATE_USER(40901, HttpStatus.CONFLICT, "이미 존재하는 유저입니다."),

    // 500 Internal Server Error

    INTERNAL_SERVER_ERROR(50000, HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러가 발생하였습니다.");

    private final int code;
    private final HttpStatus httpStatus;
    private final String message;
}
