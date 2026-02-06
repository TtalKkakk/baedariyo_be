package com.house.biet.order.command;

import com.house.biet.fixtures.AccountFixtures;
import com.house.biet.fixtures.OrderFixtures;
import com.house.biet.fixtures.RiderFixtures;
import com.house.biet.fixtures.UserFixtures;
import com.house.biet.global.vo.UserRole;
import com.house.biet.member.command.AccountRepository;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.member.command.infrastructure.AccountRepositoryJpaAdapter;
import com.house.biet.order.command.domain.aggregate.Order;
import com.house.biet.order.command.infrastructure.OrderRepositoryJpaAdapter;
import com.house.biet.rider.command.RiderRepository;
import com.house.biet.rider.command.domain.entity.Rider;
import com.house.biet.rider.command.infrastructure.RiderRepositoryJpaAdapter;
import com.house.biet.user.command.UserRepository;
import com.house.biet.user.command.domain.entity.User;
import com.house.biet.user.command.infrastructure.UserRepositoryJpaAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({
        OrderRepositoryJpaAdapter.class,
        UserRepositoryJpaAdapter.class,
        RiderRepositoryJpaAdapter.class,
        AccountRepositoryJpaAdapter.class,
})
@ActiveProfiles("test")
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RiderRepository riderRepository;

    @Autowired
    AccountRepository accountRepository;

    Order order;

    @BeforeEach
    void setup() {
        Account userAccount = AccountFixtures.account(UserRole.USER);
        Account riderAccount = AccountFixtures.account(UserRole.RIDER);

        accountRepository.save(userAccount);
        accountRepository.save(riderAccount);

        User user = userRepository.save(UserFixtures.user(userAccount));
        userRepository.save(user);

        Rider rider = riderRepository.save(RiderFixtures.rider(riderAccount));
        riderRepository.save(rider);

        order = OrderFixtures.order(user, rider);
        orderRepository.save(order);
    }

    @Test
    @DisplayName("성공 - 주문 ID로 조회 시 연관 엔티티까지 로딩됨")
    void findById_WithRelations() {
        // when
        Order found = orderRepository.findById(order.getId()).orElseThrow();

        // then
        assertThat(found.getUser()).isNotNull();
        assertThat(found.getRider()).isNotNull();
        assertThat(found.getUser().getId()).isEqualTo(order.getUser().getId());
        assertThat(found.getRider().getId()).isEqualTo(order.getRider().getId());
    }

    @Test
    @DisplayName("성공 - 주문 존재 여부 확인")
    void existsById_Success() {
        // when
        boolean exists = orderRepository.existsById(order.getId());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("실패 - 존재하지 않는 주문 ID")
    void existsById_False() {
        // when
        boolean exists = orderRepository.existsById(9999L);

        // then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("성공 - 주문 저장 및 조회")
    void saveAndFind_Success() {
        // when
        Optional<Order> found = orderRepository.findById(order.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(order.getId());
    }

    @Test
    @DisplayName("성공 - 비관적 락 조회 가능")
    void findByIdForUpdate_Success() {
        // when
        Optional<Order> found = orderRepository.findByIdForUpdate(order.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(order.getId());
    }
}