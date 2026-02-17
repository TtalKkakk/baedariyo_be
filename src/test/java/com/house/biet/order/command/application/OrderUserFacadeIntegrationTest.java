package com.house.biet.order.command.application;

import com.house.biet.fixtures.AccountFixtures;
import com.house.biet.fixtures.OrderCreateRequestDtoFixtures;
import com.house.biet.fixtures.UserFixtures;
import com.house.biet.common.domain.enums.UserRole;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.order.command.domain.aggregate.Order;
import com.house.biet.order.command.domain.dto.OrderCreateRequestDto;
import com.house.biet.common.domain.enums.OrderStatus;
import com.house.biet.support.config.ServiceIntegrationTest;
import com.house.biet.user.command.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderUserFacadeIntegrationTest extends ServiceIntegrationTest {
    @Autowired
    private OrderUserFacade orderUserFacade;

    @Autowired
    private com.house.biet.member.command.AccountRepository accountRepository;

    @Autowired
    private com.house.biet.user.command.UserRepository userRepository;

    @Autowired
    private com.house.biet.order.command.OrderRepository orderRepository;

    @Test
    @DisplayName("성공 - 주문 생성 통합 테스트")
    void createOrderAndNotify_Success() {
        // given
        Account userAccount = AccountFixtures.account(UserRole.USER);
        accountRepository.save(userAccount);

        User user = UserFixtures.user(userAccount);
        userRepository.save(user);

        OrderCreateRequestDto requestDto =
                OrderCreateRequestDtoFixtures.sample(1L);

        // when
        Order createdOrder =
                orderUserFacade.createOrderAndNotify(userAccount.getId(), requestDto);

        // then
        Order savedOrder = orderRepository.findById(createdOrder.getId())
                .orElseThrow();

        assertThat(savedOrder.getUserId()).isEqualTo(user.getId());
        assertThat(savedOrder.getStoreId()).isEqualTo(requestDto.storeId());
        assertThat(savedOrder.getMenus()).hasSize(1);
        assertThat(savedOrder.getStatus()).isEqualTo(OrderStatus.ORDERED);
    }
}
