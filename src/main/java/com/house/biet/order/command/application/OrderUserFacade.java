package com.house.biet.order.command.application;

import com.house.biet.common.domain.vo.Money;
import com.house.biet.order.command.domain.aggregate.Order;
import com.house.biet.order.command.domain.dto.OrderCreateRequestDto;
import com.house.biet.order.command.domain.vo.*;
import com.house.biet.user.query.UserQueryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderUserFacade {

    private final OrderService orderService;
    private final UserQueryService userQueryService;

    @Transactional
    public Order createOrderAndNotify(Long accountId, OrderCreateRequestDto requestDto) {

        Long storeId = requestDto.storeId();
        Long userId = userQueryService.getUserIdByAccountId(accountId);
        List<OrderMenu> menus = requestDto.menus().stream()
                .map(orderMenuRequestDto -> new OrderMenu(
                        storeId,
                        orderMenuRequestDto.menuId(),
                        new MenuName(orderMenuRequestDto.menuName()),
                        orderMenuRequestDto.quantity(),
                        new Money(orderMenuRequestDto.menuPrice())          // Money VO
                ))
                .toList();

        Address address = new Address(
                requestDto.deliveryAddress().detailAddress(),
                requestDto.deliveryAddress().jibunAddress(),
                requestDto.deliveryAddress().roadAddress()
        );

        // TODO: 추후 api 끌어올것!
        double createdOrderLatitude = 34.2;
        double createdOrderLongitude = 128.2;
        String region = "d";

        DeliveryLocation deliveryLocation = new DeliveryLocation(
                createdOrderLatitude,
                createdOrderLongitude,
                region
        );

        Order createdOrder = orderService.create(
                storeId,
                userId,
                menus,
                requestDto.storeRequest(),
                requestDto.storeRequest(),
                address,
                deliveryLocation,
                requestDto.paymentMethod(),
                LocalDateTime.now()
        );
        
        // TODO: 결제 로직
        
        // createdOrder.markPaid(); // 결제 완료
        
        // TODO: 주변 라이더들에게 알림 메시지

        return createdOrder;
    }
}
