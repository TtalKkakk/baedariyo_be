package com.house.biet.fixtures;

import com.house.biet.common.domain.vo.Money;
import com.house.biet.store.command.domain.aggregate.Store;
import com.house.biet.store.command.domain.entity.Menu;
import com.house.biet.store.command.domain.entity.MenuOptionGroup;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

public class MenuFixture {

    private Store store;
    private Money price = new Money(10000);
    private String menuName = "기본 메뉴";
    private String menuDescription = "기본 메뉴 설명";
    private List<MenuOptionGroup>  optionGroups = new ArrayList<>();

    public static MenuFixture aMenu() {
        return new MenuFixture();
    }

    public MenuFixture withStore(Store store) {
        this.store = store;
        return this;
    }

    public MenuFixture withPrice(int price) {
        this.price = new Money(price);
        return this;
    }

    public MenuFixture withMenuName(String name) {
        this.menuName = name;
        return this;
    }

    public MenuFixture withMenuDescription(String description) {
        this.menuDescription = description;
        return this;
    }

    public MenuFixture withOptionGroup(MenuOptionGroup group) {
        this.optionGroups.add(group);
        return this;
    }

    public Menu build() {
        Menu menu = new Menu();

        ReflectionTestUtils.setField(menu, "store", store);
        ReflectionTestUtils.setField(menu, "price", price);
        ReflectionTestUtils.setField(menu, "menuName", menuName);
        ReflectionTestUtils.setField(menu, "menuDescription", menuDescription);
        ReflectionTestUtils.setField(menu, "menuOptionGroups", new ArrayList<>());

        for (MenuOptionGroup group : optionGroups) {
            menu.addOptionGroup(group);
        }

        return menu;
    }
}
