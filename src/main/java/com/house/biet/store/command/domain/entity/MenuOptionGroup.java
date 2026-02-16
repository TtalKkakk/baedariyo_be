package com.house.biet.store.command.domain.entity;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.store.command.domain.vo.MenuOption;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menu_option_groups")
@Getter
public class MenuOptionGroup {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Column(nullable = false)
    private String groupName;

    @Column(nullable = false)
    private int maxSelectableCount;

    @ElementCollection
    @CollectionTable(
            name = "menu_option_group_options",
            joinColumns = @JoinColumn(name = "menu_option_group_id")
    )
    private List<MenuOption> options = new ArrayList<>();

    /* ===== 도메인 로직 ===== */

    void assignMenu(Menu menu) {
        this.menu = menu;
    }

    public void validateSelectableCount(int selectedCount) {
        if (selectedCount > maxSelectableCount)
            throw new CustomException(ErrorCode.EXCEED_MAX_OPTION_SELECTABLE_COUNT);
    }
}
