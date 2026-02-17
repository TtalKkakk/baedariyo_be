package com.house.biet.order.command.application;

import com.house.biet.fixtures.AccountFixtures;
import com.house.biet.fixtures.OrderFixtures;
import com.house.biet.fixtures.RiderFixtures;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.common.domain.enums.UserRole;
import com.house.biet.member.command.AccountRepository;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.order.command.OrderRepository;
import com.house.biet.order.command.domain.aggregate.Order;
import com.house.biet.rider.command.RiderRepository;
import com.house.biet.rider.command.domain.entity.Rider;
import com.house.biet.support.config.ServiceIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderRiderFacadeIntegrationTest extends ServiceIntegrationTest {

    @Autowired
    private OrderRiderFacade orderRiderFacade;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RiderRepository riderRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("성공 - AccountID 기반 Rider 조회 후 주문 배정")
    void assignRider_Success() {
        // given
        Account riderAccount = AccountFixtures.account(UserRole.RIDER);
        accountRepository.save(riderAccount);

        Rider rider = RiderFixtures.rider(riderAccount);
        riderRepository.save(rider);

        Order order = OrderFixtures.order(1L);
        order.markPaid();
        orderRepository.save(order);

        // when
        orderRiderFacade.assignRider(riderAccount.getId(), order.getId());

        // then
        Optional<Order> updatedOrder = orderRepository.findById(order.getId());
        assertThat(updatedOrder).isPresent();
        assertThat(updatedOrder.get().getRiderId()).isEqualTo(rider.getId());
    }

    @Test
    @DisplayName("에러 - Rider 없는 AccountId로 배정 시 예외 발생")
    void assignRider_Error_RiderNotFound() {
        // given
        Long notExistsRiderId = -1L;

        Order order = OrderFixtures.order(1L);
        order.markPaid();
        orderRepository.save(order);

        // when & then
        assertThatThrownBy(() -> orderRiderFacade.assignRider(notExistsRiderId, order.getId()))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.RIDER_NOT_FOUND.getMessage());
    }
    
    @Test
    @DisplayName("에러 - 결제가 되지 않은 주문에 라이더 배정 시도")
    void assignRider_Error_NotPaidOrder() {
        Account riderAccount = AccountFixtures.account(UserRole.RIDER);
        accountRepository.save(riderAccount);

        Rider rider = RiderFixtures.rider(riderAccount);
        riderRepository.save(rider);

        Order order = OrderFixtures.order(1L);
        // order.markPaid();  // 결제 안됨
        orderRepository.save(order);

        // when & then
        assertThatThrownBy(() -> orderRiderFacade.assignRider(riderAccount.getId(), order.getId()))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_ORDER_STATUS.getMessage());
    }
}
