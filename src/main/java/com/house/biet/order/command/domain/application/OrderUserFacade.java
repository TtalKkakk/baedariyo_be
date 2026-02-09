package com.house.biet.order.command.domain.application;

import com.house.biet.order.command.domain.aggregate.Order;
import com.house.biet.order.command.domain.dto.OrderCreateRequestDto;
import com.house.biet.order.command.domain.vo.MenuName;
import com.house.biet.order.command.domain.vo.Money;
import com.house.biet.order.command.domain.vo.OrderMenu;
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

        Order createdOrder = orderService.create(
                storeId,
                userId,
                menus,
                requestDto.storeRequest(),
                requestDto.storeRequest(),
                requestDto.deliveryAddress(),
                requestDto.paymentMethod(),
                LocalDateTime.now()
        );
        
        // TODO: 결제 로직
        
        // createdOrder.markPaid(); // 결제 완료
        
        // TODO: 주변 라이더들에게 알림 메시지

        return createdOrder;
    }
}
