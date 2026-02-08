package com.house.biet.order.command.domain.aggregate;

import com.house.biet.global.jpa.BaseTimeEntity;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.order.command.domain.vo.Money;
import com.house.biet.order.command.domain.vo.OrderMenu;
import com.house.biet.order.command.domain.vo.OrderStatus;
import com.house.biet.order.command.domain.vo.PaymentMethod;
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
    @Column(nullable = true)
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
                 List<OrderMenu> menus,
                 String storeRequest,
                 String riderRequest,
                 String deliveryAddress,
                 PaymentMethod paymentMethod,
                 LocalDateTime estimatedTime) {

        if (menus == null || menus.isEmpty()) {
            throw new CustomException(ErrorCode.EMPTY_ORDER_MENU);
        }
        if (storeId == null || userId == null || deliveryAddress == null || paymentMethod == null) {
            throw new CustomException(ErrorCode.INVALID_ORDER_DATA);
        }

        this.storeId = storeId;
        this.userId = userId;
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

        recalculateTotalPrice();
    }

    /**
     * 주문 메뉴 제거
     *
     * <p>
     * - 주문에 존재하는 메뉴를 제거
     * - 제거된 메뉴가 없으면 CustomException 발생 (ORDER_MENU_NOT_FOUND)
     * - 제거 후 총액(totalPrice) 재계산
     * </p>
     *
     * @param menuToRemove 제거할 메뉴
     * @throws CustomException 메뉴가 주문에 존재하지 않을 경우
     */
    public void removeMenu(OrderMenu menuToRemove) {
        boolean removed = menus.removeIf(m -> m.equals(menuToRemove));
        if (!removed) throw new CustomException(ErrorCode.ORDER_MENU_NOT_FOUND);

        recalculateTotalPrice();
    }

    /**
     * 주문 메뉴 수량 수정
     *
     * <p>
     * - 메뉴 ID로 주문 내 메뉴를 찾고 수량을 갱신
     * - 수량이 0 이하일 경우 CustomException 발생 (INVALID_MENU_QUANTITY)
     * - 메뉴가 존재하지 않으면 CustomException 발생 (ORDER_MENU_NOT_FOUND)
     * - 수정 후 총액(totalPrice) 재계산
     * </p>
     *
     * @param menuId 수정할 메뉴 ID
     * @param newQuantity 새로운 수량
     * @throws CustomException 메뉴가 존재하지 않거나 수량이 유효하지 않은 경우
     */
    public void updateMenuQuantity(Long menuId, int newQuantity) {
        if (newQuantity <= 0) throw new CustomException(ErrorCode.INVALID_MENU_QUANTITY);

        OrderMenu existing = menus.stream()
                .filter(m -> m.getMenuId().equals(menuId))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_MENU_NOT_FOUND));

        OrderMenu updatedMenu = new OrderMenu(
                existing.getStoreId(),
                existing.getMenuId(),
                existing.getMenuName(),
                newQuantity,
                existing.getMenuPrice()
        );

        menus.remove(existing);
        menus.add(updatedMenu);
        recalculateTotalPrice();
    }

    /**
     * 총액 재계산
     *
     * <p>
     * - 현재 주문 메뉴들의 금액을 합산하여 totalPrice 갱신
     * </p>
     */
    private void recalculateTotalPrice() {
        this.totalPrice = menus.stream()
                .map(OrderMenu::totalPrice)
                .reduce(new Money(0), Money::add);
    }

    /**
     * 주문 총액 조회
     *
     * @return 주문 총액 (Money.value)
     */
    public int getTotalAmount() {
        return totalPrice.value();
    }

    /* -------------------- 라이더 결정 ------------------ */

    /**
     * 라이더를 주문에 배정한다.
     *
     * <p>
     * 도메인 규칙:
     * <ul>
     *     <li>라이더 ID는 null일 수 없다.</li>
     *     <li>주문 상태가 {@link OrderStatus#PAID} 인 경우에만 배정 가능하다.</li>
     *     <li>이미 라이더가 배정된 주문에는 다시 배정할 수 없다.</li>
     * </ul>
     * </p>
     *
     * @param riderId 배정할 라이더 ID
     *
     * @throws CustomException
     * <ul>
     *     <li>{@link ErrorCode#INVALID_RIDER_ID} 라이더 ID가 null인 경우</li>
     *     <li>{@link ErrorCode#INVALID_ORDER_STATUS} 주문 상태가 배정 불가능한 경우</li>
     *     <li>{@link ErrorCode#ALREADY_ASSIGN_RIDER} 이미 라이더가 배정된 경우</li>
     * </ul>
     */
    public void assignRider(Long riderId) {
        if (riderId == null)
            throw new CustomException(ErrorCode.INVALID_RIDER_ID);
        if (this.status != OrderStatus.PAID)
            throw new CustomException(ErrorCode.INVALID_ORDER_STATUS);
        if (this.riderId != null)
            throw new CustomException(ErrorCode.ALREADY_ASSIGN_RIDER);

        this.riderId = riderId;
    }

    /* -------------------- 상태 변경 -------------------- */

    /**
     * 주문 취소
     *
     * <p>
     * - 상태가 ORDERED 또는 PAID일 때만 취소 가능
     * - 잘못된 상태에서는 CustomException 발생 (INVALID_ORDER_CANCEL)
     * </p>
     *
     * @throws CustomException 취소 불가능한 상태일 경우
     */
    public void cancel() {
        if (status != OrderStatus.ORDERED && status != OrderStatus.PAID) {
            throw new CustomException(ErrorCode.INVALID_ORDER_CANCEL);
        }
        this.status = OrderStatus.CANCELLED;
    }

    /**
     * 주문 결제 완료 처리
     *
     * <p>
     * - 상태가 ORDERED인 경우만 결제 완료 처리 가능
     * - 잘못된 상태에서는 CustomException 발생 (INVALID_ORDER_PAYMENT)
     * </p>
     *
     * @throws CustomException 결제 불가능한 상태일 경우
     */
    public void markPaid() {
        if (status != OrderStatus.ORDERED) {
            throw new CustomException(ErrorCode.INVALID_ORDER_PAYMENT);
        }
        this.status = OrderStatus.PAID;
    }

    /**
     * 주문 배달 중 처리
     *
     * <p>
     * - 상태가 PAID인 경우만 배달 중 처리 가능
     * - 잘못된 상태에서는 CustomException 발생 (INVALID_ORDER_STATUS)
     * </p>
     *
     * @throws CustomException 배달 상태로 전환할 수 없는 경우
     */
    public void markDelivering() {
        if (status != OrderStatus.PAID) {
            throw new CustomException(ErrorCode.INVALID_ORDER_STATUS);
        }
        this.status = OrderStatus.DELIVERING;
    }

    /**
     * 주문 배달 완료 처리
     *
     * <p>
     * - 상태가 DELIVERING인 경우만 완료 처리 가능
     * - 잘못된 상태에서는 CustomException 발생 (INVALID_ORDER_STATUS)
     * </p>
     *
     * @throws CustomException 배달 완료 상태로 전환할 수 없는 경우
     */
    public void markDelivered() {
        if (status != OrderStatus.DELIVERING) {
            throw new CustomException(ErrorCode.INVALID_ORDER_STATUS);
        }
        this.status = OrderStatus.DELIVERED;
    }

    /* ---------------- 상태 조회 ---------------- */

    /**
     * 결제 완료 여부
     *
     * @return true if 상태가 PAID
     */
    public boolean isPaid() { return status == OrderStatus.PAID; }

    /**
     * 배달 완료 여부
     *
     * @return true if 상태가 DELIVERED
     */
    public boolean isDelivered() { return status == OrderStatus.DELIVERED; }

    /**
     * 주문 취소 여부
     *
     * @return true if 상태가 CANCELLED
     */
    public boolean isCancelled() { return status == OrderStatus.CANCELLED; }

    /* ---------------- 기타 ---------------- */

    /**
     * 예상 배달 시간 갱신
     *
     * @param newTime 갱신할 예상 배달 시간
     */
    public void updateEstimatedTime(LocalDateTime newTime) {
        this.estimatedTime = newTime;
    }

    /**
     * 주문 요약 문자열 생성
     *
     * @return 주문 ID, 상태, 총액, 메뉴 목록을 포함한 요약 문자열
     */
    public String summary() {
        StringBuilder sb = new StringBuilder();
        sb.append("주문ID: ").append(id)
                .append(", 상태: ").append(status)
                .append(", 총액: ").append(totalPrice.value())
                .append(", 메뉴: ");
        menus.forEach(m -> sb.append(m.getMenuName().value())
                .append("(").append(m.getQuantity()).append("개) "));
        return sb.toString();
    }
}