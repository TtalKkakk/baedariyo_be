package com.house.biet.store.command.domain.entity;

import com.house.biet.common.domain.vo.Money;
import com.house.biet.store.command.domain.aggregate.Store;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menus")
@Getter
public class Menu {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Embedded
    @AttributeOverride(
            name = "price",
            column = @Column(name = "money", nullable = false)
    )
    private Money price;

    @Column(nullable = false)
    private String menuName;

    @Column
    private String menuDescription;

    @OneToMany(
            mappedBy = "menu",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<MenuOptionGroup> menuOptionGroups = new ArrayList<>();

    /* ===== 도메인 로직 ===== */

    public void addOptionGroup(MenuOptionGroup group) {
        group.assignMenu(this);
        menuOptionGroups.add(group);
    }
}
