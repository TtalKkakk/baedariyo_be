package com.house.biet.store.command.domain.vo;

import com.house.biet.common.domain.vo.Money;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class MenuOption {

    @Column(nullable = false)
    private String name;

    @Embedded
    private Money optionPrice;

    public MenuOption(String name, Money optionPrice) {
        if (name == null || name.isBlank())
            throw new CustomException(ErrorCode.INVALID_MENU_OPTION_NAME_FORMAT);

        this.name = name;
        this.optionPrice = optionPrice;
    }
}
