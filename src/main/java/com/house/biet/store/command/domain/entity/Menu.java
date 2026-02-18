package com.house.biet.store.command.domain.entity;

import com.house.biet.common.domain.vo.Money;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.store.command.domain.aggregate.Store;
import com.house.biet.store.command.domain.vo.MenuName;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menus")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Embedded
    @AttributeOverride(
            name = "amount",
            column = @Column(name = "price", nullable = false)
    )
    private Money price;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "menu_name", nullable = false)
    )
    private MenuName menuName;

    @Column
    private String menuDescription;

    @OneToMany(
            mappedBy = "menu",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<MenuOptionGroup> menuOptionGroups = new ArrayList<>();

    /* ===== 생성 로직 ===== */

    private Menu(Store store,
                 MenuName menuName,
                 Money price,
                 String menuDescription) {

        if (store == null)
            throw new CustomException(ErrorCode.INVALID_STORE_ID);
        if (menuName == null)
            throw new CustomException(ErrorCode.INVALID_MENU_NAME_FORMAT);
        if (price == null)
            throw new CustomException(ErrorCode.INVALID_MENU_PRICE);

        this.store = store;
        this.menuName = menuName;
        this.price = price;
        this.menuDescription = menuDescription;
    }

    public static Menu create(Store store,
                              MenuName menuName,
                              Money price,
                              String menuDescription) {
        return new Menu(store, menuName, price, menuDescription);
    }

    /* ===== 도메인 로직 ===== */

    public void addOptionGroup(MenuOptionGroup group) {
        group.assignMenu(this);
        menuOptionGroups.add(group);
    }
}
