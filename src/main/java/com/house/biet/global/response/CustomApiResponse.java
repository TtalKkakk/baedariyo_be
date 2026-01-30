package com.house.biet.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomApiResponse<T> {

    private final int code;
    private final String message;
    private final T data;

    public static <T> CustomApiResponse<T> success(SuccessCode successCode, T data) {
        return new CustomApiResponse<>(
                successCode.getCode(),
                successCode.getMessage(),
                data
        );
    }

    public static CustomApiResponse<Void> success(SuccessCode successCode) {
        return new CustomApiResponse<>(
                successCode.getCode(),
                successCode.getMessage(),
                null
        );
    }
}
