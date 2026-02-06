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

    /**
     * 주문 대상 가게 ID (한 주문 = 한 가게)
     */
    @Column(nullable = false)
    private Long storeId;

    /**
     * 주문자 정보
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 라이더 정보
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rider_id", nullable = false)
    private Rider rider;

    /**
     * 주문 상태
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    /**
     * 메뉴 전체
     */
    @ElementCollection
    @CollectionTable(name = "order_menus", joinColumns = @JoinColumn(name = "order_id"))
    @Column(nullable = false)
    private List<OrderMenu> menus = new ArrayList<>();

    /**
     * 주문 총액
     */
    @Embedded
    @AttributeOverride(
            name = "amount",
            column = @Column(name = "money", nullable = false)
    )
    private Money totalPrice;

    /**
     * 가게 요청 사항
     */
    @Column(nullable = true)
    private String storeRequest;

    /**
     * 라이더 요청 사항
     */
    @Column(nullable = true)
    private String riderRequest;

    /**
     * 배달 주소
     */
    @Column(nullable = false)
    private String deliveryAddress;

    /**
     * 결제 방법
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    /**
     * 결제 완료 여부
     */
    @Column(nullable = false)
    private boolean paid;

    /**
     * 예상 배달 시간 (외부 API)
     */
    @Column(nullable = true)
    private LocalDateTime estimatedTime;

    /**
     * 주문 생성자
     *
     * @param storeId         가게 ID
     * @param user            주문자
     * @param rider           배정 라이더
     * @param menus           주문 메뉴 리스트
     * @param storeRequest    가게 요청 사항
     * @param riderRequest    라이더 요청 사항
     * @param deliveryAddress 배달 주소
     * @param paymentMethod   결제 방식
     * @param paid            결제 완료 여부
     * @param estimatedTime   예상 배달 시간
     */
    public Order(Long storeId,
                 User user,
                 Rider rider,
                 List<OrderMenu> menus,
                 String storeRequest,
                 String riderRequest,
                 String deliveryAddress,
                 PaymentMethod paymentMethod,
                 boolean paid,
                 LocalDateTime estimatedTime) {

        if (menus == null || menus.isEmpty()) {
            throw new CustomException(ErrorCode.EMPTY_ORDER_MENU);
        }
        if (storeId == null || user == null || rider == null || deliveryAddress == null || paymentMethod == null) {
            throw new CustomException(ErrorCode.INVALID_ORDER_DATA);
        }

        this.storeId = storeId;
        this.user = user;
        this.rider = rider;
        this.status = OrderStatus.ORDERED;
        this.storeRequest = storeRequest;
        this.riderRequest = riderRequest;
        this.deliveryAddress = deliveryAddress;
        this.paymentMethod = paymentMethod;
        this.paid = paid;
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