package com.house.biet.order.command.domain.application;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.order.command.OrderRepository;
import com.house.biet.order.command.domain.aggregate.Order;
import com.house.biet.order.command.domain.vo.OrderMenu;
import com.house.biet.order.command.domain.vo.PaymentMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 주문 관련 서비스
 *
 * <p>
 * - 주문 생성, 수정, 상태 변경, 조회 등의 기능 제공
 * - Order 도메인 객체를 통해 모든 비즈니스 로직 수행
 * </p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    /* -------------------- 생성자 -------------------- */

    /**
     * 새로운 주문 생성
     *
     * <p>
     * - Order 엔티티를 생성하고 DB에 저장
     * - 주문 메뉴, 결제 방법, 배달 주소 등 필수 값 체크는 Order 엔티티 내부에서 처리
     * </p>
     *
     * @param storeId 주문 대상 가게 ID
     * @param userId 주문자 ID
     * @param riderId 배정 라이더 ID
     * @param menus 주문 메뉴 리스트
     * @param storeRequest 가게 요청 사항
     * @param riderRequest 라이더 요청 사항
     * @param deliveryAddress 배달 주소
     * @param paymentMethod 결제 방법
     * @param estimatedTime 예상 배달 시간
     * @return 생성된 Order 엔티티
     * @throws CustomException Order 엔티티에서 발생하는 모든 유효성 예외 전파
     */
    public Order create(Long storeId,
                             Long userId,
                             Long riderId,
                             List<OrderMenu> menus,
                             String storeRequest,
                             String riderRequest,
                             String deliveryAddress,
                             PaymentMethod paymentMethod,
                             LocalDateTime estimatedTime
    ) {
        Order order = new Order(
                storeId,
                userId,
                riderId,
                menus,
                storeRequest,
                riderRequest,
                deliveryAddress,
                paymentMethod,
                estimatedTime
        );

        return orderRepository.save(order);
    }

    /**
     * 주문 단건 조회
     *
     * <p>
     * - 주문 ID로 주문을 조회한다
     * - 주문이 존재하지 않을 경우 {@link CustomException} (ORDER_NOT_FOUND)을 발생시킨다
     * - 조회 목적의 메서드로, 상태 변경이나 락을 사용하지 않는다
     * </p>
     *
     * @param orderId 조회할 주문 ID
     * @return 조회된 Order 엔티티
     * @throws CustomException 주문이 존재하지 않을 경우
     */
    public Order getOrder(Long orderId) {
        return findOrderOrThrow(orderId);
    }


    /* -------------------- 주문 메뉴 관리 -------------------- */

    /**
     * 주문 메뉴 추가
     *
     * @param orderId 주문 ID
     * @param menu 추가할 메뉴
     * @throws CustomException 주문이 존재하지 않거나 메뉴 추가 조건 위반 시
     */
    public void addMenu(Long orderId, OrderMenu menu) {
        Order order = findOrderOrThrow(orderId);
        order.addMenu(menu);
    }

    /**
     * 주문 메뉴 제거
     *
     * @param orderId 주문 ID
     * @param menu 제거할 메뉴
     * @throws CustomException 주문이 존재하지 않거나 메뉴가 없을 경우
     */
    public void removeMenu(Long orderId, OrderMenu menu) {
        Order order = findOrderOrThrow(orderId);
        order.removeMenu(menu);
    }

    /**
     * 주문 메뉴 수량 업데이트
     *
     * @param orderId 주문 ID
     * @param menuId 메뉴 ID
     * @param quantity 새로운 수량
     * @throws CustomException 주문이 존재하지 않거나 메뉴가 없거나 수량이 유효하지 않을 경우
     */
    public void updateMenuQuantity(Long orderId, Long menuId, int quantity) {
        Order order = findOrderOrThrow(orderId);
        order.updateMenuQuantity(menuId, quantity);
    }

    /* -------------------- 주문 상태 관리 -------------------- */

    /**
     * 주문 취소
     *
     * @param orderId 주문 ID
     * @throws CustomException 주문이 존재하지 않거나 취소 불가능한 상태일 경우
     */
    public void cancelOrder(Long orderId) {
        Order order = findOrderOrThrow(orderId);
        order.cancel();
    }

    /**
     * 주문 결제 완료 처리
     *
     * @param orderId 주문 ID
     * @throws CustomException 주문이 존재하지 않거나 결제 불가능 상태일 경우
     */
    public void markPaid(Long orderId) {
        Order order = findOrderOrThrow(orderId);
        order.markPaid();
    }

    /**
     * 주문 배달 중 처리
     *
     * @param orderId 주문 ID
     * @throws CustomException 주문이 존재하지 않거나 배달 불가능 상태일 경우
     */
    public void markDelivering(Long orderId) {
        Order order = findOrderOrThrow(orderId);
        order.markDelivering();
    }

    /**
     * 주문 배달 완료 처리
     *
     * @param orderId 주문 ID
     * @throws CustomException 주문이 존재하지 않거나 완료 불가능 상태일 경우
     */
    public void markDelivered(Long orderId) {
        Order order = findOrderOrThrow(orderId);
        order.markDelivered();
    }

    /* -------------------- 예상 배달 시간 -------------------- */

    /**
     * 예상 배달 시간 갱신
     *
     * @param orderId 주문 ID
     * @param newTime 갱신할 예상 배달 시간
     * @throws CustomException 주문이 존재하지 않을 경우
     */
    public void updateEstimatedTime(Long orderId, LocalDateTime newTime) {
        Order order = findOrderOrThrow(orderId);
        order.updateEstimatedTime(newTime);
    }

    /* -------------------- 조회 -------------------- */

    /**
     * 주문 조회 (비관적 락)
     *
     * <p>
     * - DB에서 PESSIMISTIC_WRITE 락을 걸고 조회
     * - 동시성 문제가 있는 상황에서 사용
     * </p>
     *
     * @param orderId 조회할 주문 ID
     * @return Order 엔티티
     * @throws CustomException 주문이 존재하지 않을 경우
     */
    @Transactional
    public Order findOrderOrThrowForUpdate(Long orderId) {
        return orderRepository.findByIdForUpdate(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
    }

    /* -------------------- private -------------------- */

    /**
     * 주문 조회
     *
     * <p>
     * - 주문이 존재하지 않으면 CustomException 발생 (ORDER_NOT_FOUND)
     * </p>
     *
     * @param orderId 조회할 주문 ID
     * @return 조회된 Order 엔티티
     * @throws CustomException 주문이 존재하지 않을 경우
     */
    @Transactional(readOnly = true)
    private Order findOrderOrThrow(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
    }
}
