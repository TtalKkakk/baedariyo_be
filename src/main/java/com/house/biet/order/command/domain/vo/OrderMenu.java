package com.house.biet.order.command.domain.vo;

import com.house.biet.common.domain.vo.Money;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 주문 메뉴 값 객체
 *
 * <p>
 * - 한 주문에 포함된 단일 메뉴를 표현
 * - 메뉴 가격, 옵션, 수량은 주문 시점 기준으로 고정
 * - Order Aggregate에 완전히 종속됨
 * </p>
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {
        "storeId",
        "menuId",
        "orderMenuName",
        "menuPrice",
})
public class OrderMenu {

    /**
     * 가게 ID (한 주문 = 한 가게 정책 검증용)
     */
    private Long storeId;

    /**
     * 메뉴 ID
     */
    private Long menuId;

    /**
     * 메뉴 이름
     */
    @AttributeOverride(
            name = "value",
            column = @Column(name = "menu_name", nullable = false)
    )
    private OrderMenuName orderMenuName;

    /**
     * 주문 수량
     */
    private int quantity;

    /**
     * 메뉴 기본 가격
     */
    @Embedded
    @AttributeOverride(
            name = "amount",
            column = @Column(name = "menu_price", nullable = false)
    )
    private Money menuPrice;

    public OrderMenu(Long storeId, Long menuId, OrderMenuName orderMenuName, int quantity, Money menuPrice) {
        if (quantity <= 0)
            throw new CustomException(ErrorCode.INVALID_ORDER_MENU_QUANTITY);

        this.storeId = storeId;
        this.menuId = menuId;
        this.orderMenuName = orderMenuName;
        this.quantity = quantity;
        this.menuPrice = menuPrice;
    }

    /**
     * 주문 메뉴의 총 가격 계산
     *
     * <p>
     * 기본 가격 × 수량
     * </p>
     *
     * @return 계산된 총 금액
     */
    public Money totalPrice() {
        return menuPrice.multiply(quantity);
    }
}
