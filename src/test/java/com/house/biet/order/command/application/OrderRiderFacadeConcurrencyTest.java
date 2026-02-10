package com.house.biet.order.command.application;

import com.house.biet.fixtures.OrderFixtures;
import com.house.biet.global.vo.UserRole;
import com.house.biet.member.command.AccountRepository;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.member.command.domain.vo.*;
import com.house.biet.order.command.OrderRepository;
import com.house.biet.order.command.application.OrderRiderFacade;
import com.house.biet.order.command.domain.aggregate.Order;
import com.house.biet.rider.command.RiderRepository;
import com.house.biet.rider.command.domain.entity.Rider;
import com.house.biet.rider.command.domain.vo.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class OrderRiderFacadeConcurrencyTest {

    @Autowired
    private OrderRiderFacade orderRiderFacade;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RiderRepository riderRepository;

    @Autowired
    private OrderRepository orderRepository;

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    List<Account> riderAccounts;
    List<Rider> riders;
    Order order;

    private static final int RIDER_COUNT = 5;


    @BeforeEach
    void setup() {
        riderAccounts = new ArrayList<>();
        riders = new ArrayList<>();

        for (int i = 0; i < RIDER_COUNT; i++) {
            Account account = createAndSaveRiderAccount("rider" + i);
            Rider rider = createAndSaveRider(account, i);

            riderAccounts.add(account);
            riders.add(rider);
        }

        // 주문은 1개
        order = OrderFixtures.order(1L);
        order.markPaid();
        orderRepository.save(order);
    }
    
    private Account createAndSaveRiderAccount(String prefix) {
        Account account = Account.signup(
                new Email(prefix + "-" + UUID.randomUUID() + "@test.com"),
                Password.encrypt(UUID.randomUUID().toString().substring(1, 30), ENCODER),
                UserRole.RIDER
        );
        return accountRepository.save(account);
    }

    private Rider createAndSaveRider(Account account, int index) {
        Rider rider = Rider.create(
                account,
                "테스트라이더" + index,
                "riderNick" + index + "-" + UUID.randomUUID().toString().substring(0, 5),
                "010-" +
                        String.format("%04d", (int) (Math.random() * 10000)) + "-" +
                        String.format("%04d", (int) (Math.random() * 10000)),
                VehicleType.MOTORCYCLE
        );
        return riderRepository.save(rider);
    }

    @Test
    @DisplayName("동시성 - 여러 라이더(n명)가 동시에 주문 수락 시 한 명만 배정")
    void assignRider_Concurrent_N_Riders() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(RIDER_COUNT);
        CountDownLatch latch = new CountDownLatch(RIDER_COUNT);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        for (Account riderAccount : riderAccounts) {
            executor.submit(() -> {
                try {
                    orderRiderFacade.assignRider(riderAccount.getId(), order.getId());
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        assertThat(successCount.get()).isEqualTo(1);
        assertThat(failCount.get()).isEqualTo(RIDER_COUNT - 1);
    }
}