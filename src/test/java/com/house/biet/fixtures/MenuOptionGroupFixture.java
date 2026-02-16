package com.house.biet.fixtures;

import com.house.biet.store.command.domain.entity.Menu;
import com.house.biet.store.command.domain.entity.MenuOptionGroup;
import com.house.biet.store.command.domain.vo.MenuOption;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

public class MenuOptionGroupFixture {

    private String groupName = "기본 옵션 그룹";
    private int maxSelectableCount = 1;
    private Menu menu = null;
    private List<MenuOption> options = new ArrayList<>();

    public static MenuOptionGroupFixture aMenuOptionGroup() {
        return new MenuOptionGroupFixture();
    }

    public MenuOptionGroupFixture withGroupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public MenuOptionGroupFixture withMaxSelectableCount(int count) {
        this.maxSelectableCount = count;
        return this;
    }

    public MenuOptionGroupFixture withMenu(Menu menu) {
        this.menu = menu;
        return this;
    }

    public MenuOptionGroupFixture withOption(MenuOption option) {
        this.options.add(option);
        return this;
    }

    public MenuOptionGroupFixture withOptions(List<MenuOption> options) {
        this.options = options;
        return this;
    }

    public MenuOptionGroup build() {
        MenuOptionGroup group = new MenuOptionGroup();

        ReflectionTestUtils.setField(group, "groupName", groupName);
        ReflectionTestUtils.setField(group, "maxSelectableCount", maxSelectableCount);
        ReflectionTestUtils.setField(group, "menu", menu);
        ReflectionTestUtils.setField(group, "options", options);

        return group;
    }
}
