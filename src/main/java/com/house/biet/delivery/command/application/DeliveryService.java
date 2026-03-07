package com.house.biet.delivery.command.application;

import com.house.biet.delivery.command.DeliveryRepository;
import com.house.biet.delivery.command.domain.aggregate.Delivery;
import com.house.biet.delivery.infrastructure.redis.DeliveryLocationRedisRepository;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Delivery 도메인의 Application Service.
 *
 * <p>
 * Delivery Aggregate의 생성, 상태 전이 등의
 * 비즈니스 흐름을 조정하는 역할을 담당한다.
 * </p>
 *
 * <p>
 * 도메인 규칙 자체는 Delivery Aggregate 내부에서 처리하며,
 * 본 서비스는 트랜잭션 경계와 Repository 접근을 관리한다.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final DeliveryLocationRedisRepository deliveryLocationRedisRepository;

    /**
     * 새로운 배달을 생성한다.
     *
     * @param orderId 주문 ID
     * @return 생성된 Delivery
     * @throws CustomException 이미 해당 주문에 대한 배달이 존재할 경우
     */
    public Delivery create(Long orderId) {
        deliveryRepository.findByOrderId(orderId)
                .ifPresent(d -> {
                    throw new CustomException(ErrorCode.DELIVERY_ALREADY_EXISTS);
                });

        Delivery delivery = Delivery.create(orderId);

        return deliveryRepository.save(delivery);
    }

    /**
     * 배달에 라이더를 부여한다.
     *
     * @param orderId 주문 ID
     * @param riderId 배달원 ID
     */
    public void assignRider(Long orderId, Long riderId) {
        Delivery delivery = getByOrderId(orderId);

        delivery.assignRider(riderId);
    }

    /**
     * 배달을 시작 상태로 변경한다.
     *
     * @param orderId 주문 ID
     */
    public void pickUp(Long orderId) {
        Delivery delivery = getByOrderId(orderId);

        delivery.pickup();
    }

    /**
     * 배달 상태를 배달 중으로 변경한다.
     *
     * @param orderId 주문 ID
     */
    public void inDelivery(Long orderId) {
        Delivery delivery = getByOrderId(orderId);

        delivery.startDelivery();
    }

    /**
     * 배달을 완료 상태로 변경한다.
     *
     * @param orderId 주문 ID
     */
    public void complete(Long orderId) {
        Delivery delivery = getByOrderId(orderId);

        delivery.complete();

        deliveryRepository.save(delivery);

        publishEvents(delivery);

        // 위치 캐시 삭제
        deliveryLocationRedisRepository.delete(orderId);
    }

    /**
     * 배달을 췻호 상태로 변경한다.
     *
     * @param orderId 주문 ID
     */
    public void cancel(Long orderId) {
        Delivery delivery = getByOrderId(orderId);

        delivery.cancel();
    }

    /**
     * 주문 ID로 Delivery를 조회한다.
     *
     * @param orderId 주문 ID
     * @return Delivery
     * @throws CustomException 존재하지 않을 경우
     */
    @Transactional(readOnly = true)
    public Delivery getByOrderId(Long orderId) {
        return deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.DELIVERY_NOT_FOUND));
    }

    private void publishEvents(Delivery delivery) {
        delivery.getDomainEvents().forEach(eventPublisher::publishEvent);
        delivery.clearDomainEvents();
    }
}