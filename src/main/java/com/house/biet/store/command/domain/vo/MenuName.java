package com.house.biet.store.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 가게 메뉴 이름 값 객체
 *
 * <p>
 * - 가게에서 관리되는 "현재 메뉴 이름"
 * - 메뉴 수정 시 변경될 수 있음
 * - 주문(MenuSnapshot)과는 분리된 개념
 * </p>
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class MenuName {

    private static final int MAX_LENGTH = 50;

    private String value;

    public MenuName(String value) {
        if (value == null || value.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_MENU_NAME_FORMAT);
        }
        if (value.length() > MAX_LENGTH) {
            throw new CustomException(ErrorCode.INVALID_MENU_NAME_FORMAT);
        }

        this.value = value;
    }
}
