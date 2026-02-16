package com.house.biet.order.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 메뉴 이름 값 객체
 *
 * <p>
 * - 주문 시점의 메뉴 이름 스냅샷
 * - 유효성 검증을 생성 시점에 수행
 * </p>
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class OrderMenuName {

    private String value;

    public OrderMenuName(String value) {
        if (value == null || value.isBlank())
            throw new CustomException(ErrorCode.INVALID_ORDER_MENU_NAME_FORMAT);
        if (value.length() > 50)
            throw new CustomException(ErrorCode.INVALID_ORDER_MENU_NAME_FORMAT);

        this.value = value;
    }

    public String value() {
        return value;
    }
}
