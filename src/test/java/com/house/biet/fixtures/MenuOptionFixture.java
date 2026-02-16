package com.house.biet.fixtures;

import com.house.biet.common.domain.vo.Money;
import com.house.biet.store.command.domain.vo.MenuOption;

public class MenuOptionFixture {

    private String name = "기본 옵션";
    private Money optionPrice = new Money(0);

    public static MenuOptionFixture aMenuOption() {
        return new MenuOptionFixture();
    }

    public MenuOptionFixture withName(String name) {
        this.name = name;
        return this;
    }

    public MenuOptionFixture withPrice(int price) {
        this.optionPrice = new Money(price);
        return this;
    }

    public MenuOption build() {
        return new MenuOption(name, optionPrice);
    }
}