package com.house.biet.common.domain.enums;

import lombok.Getter;

@Getter
public enum StoreCategory {
    CAFE_DESSERT("카페 ∙ 디저트"),
    WESTERN("양식"),
    CHINESE("중식"),
    KOREAN("한식"),
    JAPANESE("일식"),
    SNACK("분식"),
    FAST_FOOD("패스트 푸드"),
    PIZZA("피자"),
    CHICKEN("치킨"),
    PORK_FISH("돈까스 ∙ 회"),
    MEAT("고기");

    private final String displayName;

    StoreCategory(String displayName) {
        this.displayName = displayName;
    }
}
