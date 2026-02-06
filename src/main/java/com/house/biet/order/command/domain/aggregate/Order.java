package com.house.biet.order.command.domain.aggregate;

import com.house.biet.global.jpa.BaseTimeEntity;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.order.command.domain.vo.Money;
import com.house.biet.order.command.domain.vo.OrderMenu;
import com.house.biet.order.command.domain.vo.OrderStatus;
import com.house.biet.order.command.domain.vo.PaymentMethod;
import com.house.biet.rider.command.domain.entity.Rider;
import com.house.biet.user.command.domain.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 주문 엔티티
 *
 * <p>
 * - Aggregate Root
 * - OrderMenu VO들을 포함
 * - 주문 상태 관리
 * </p>
 */
@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 주문 대상 가게 ID */
    @Column(nullable = false)
    private Long storeId;

    /** 주문자 ID */
    @Column(nullable = false)
    private Long userId;

    /** 배정 라이더 ID */
    @Column(nullable = false)
    private Long riderId;

    /** 주문 상태 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    /** 주문 메뉴 */
    @ElementCollection
    @CollectionTable(name = "order_menus", joinColumns = @JoinColumn(name = "order_id"))
    private List<OrderMenu> menus = new ArrayList<>();

    /** 주문 총액 */
    @Embedded
    @AttributeOverride(
            name = "amount",
            column = @Column(name = "money", nullable = false)
    )
    private Money totalPrice;

    /** 가게 요청 사항 */
    private String storeRequest;

    /** 라이더 요청 사항 */
    private String riderRequest;

    /** 배달 주소 */
    @Column(nullable = false)
    private String deliveryAddress;

    /** 결제 방법 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    /** 예상 배달 시간 */
    private LocalDateTime estimatedTime;

    public Order(Long storeId,
                 Long userId,
                 Long riderId,
                 List<OrderMenu> menus,
                 String storeRequest,
                 String riderRequest,
                 String deliveryAddress,
                 PaymentMethod paymentMethod,
                 LocalDateTime estimatedTime) {

        if (menus == null || menus.isEmpty()) {
            throw new CustomException(ErrorCode.EMPTY_ORDER_MENU);
        }
        if (storeId == null || userId == null || riderId == null ||
                deliveryAddress == null || paymentMethod == null) {
            throw new CustomException(ErrorCode.INVALID_ORDER_DATA);
        }

        this.storeId = storeId;
        this.userId = userId;
        this.riderId = riderId;
        this.status = OrderStatus.ORDERED;
        this.storeRequest = storeRequest;
        this.riderRequest = riderRequest;
        this.deliveryAddress = deliveryAddress;
        this.paymentMethod = paymentMethod;
        this.estimatedTime = estimatedTime;

        menus.forEach(this::addMenu);
    }

    /**
     * 주문 메뉴 추가
     *
     * <p>
     * - 같은 메뉴가 이미 존재하면 수량 합산
     * - 없으면 새로 추가
     * - totalPrice 재계산
     * </p>
     */
    public void addMenu(OrderMenu newMenu) {
        if (!newMenu.getStoreId().equals(this.storeId)) {
            throw new CustomException(ErrorCode.INVALID_STORE_ID);
        }

        OrderMenu existing = menus.stream()
                .filter(m -> m.equals(newMenu))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            int updatedQuantity = existing.getQuantity() + newMenu.getQuantity();
            OrderMenu mergedMenu = new OrderMenu(
                    existing.getStoreId(),
                    existing.getMenuId(),
                    existing.getMenuName(),
                    updatedQuantity,
                    existing.getMenuPrice()
            );
            menus.remove(existing);
            menus.add(mergedMenu);
        } else {
            menus.add(newMenu);
        }

        // totalPrice 계산
        this.totalPrice = menus.stream()
                .map(OrderMenu::totalPrice)
                .reduce(new Money(0), Money::add);
    }

    /* -------------------- 상태 변경 -------------------- */

    public void cancel() {
        if (status != OrderStatus.ORDERED && status != OrderStatus.PAID) {
            throw new CustomException(ErrorCode.INVALID_ORDER_CANCEL);
        }
        this.status = OrderStatus.CANCELLED;
    }

    public void markPaid() {
        if (status != OrderStatus.ORDERED) {
            throw new CustomException(ErrorCode.INVALID_ORDER_PAYMENT);
        }
        this.status = OrderStatus.PAID;
    }

    public void markDelivering() {
        if (status != OrderStatus.PAID) {
            throw new CustomException(ErrorCode.INVALID_ORDER_STATUS);
        }
        this.status = OrderStatus.DELIVERING;
    }

    public void markDelivered() {
        if (status != OrderStatus.DELIVERING) {
            throw new CustomException(ErrorCode.INVALID_ORDER_STATUS);
        }
        this.status = OrderStatus.DELIVERED;
    }

    /**
     * 예상 배달 시간 업데이트
     *
     * <p>
     * - 외부 API로부터 받아온 예상 시간을 갱신
     * </p>
     */
    public void updateEstimatedTime(LocalDateTime newTime) {
        this.estimatedTime = newTime;
    }
}