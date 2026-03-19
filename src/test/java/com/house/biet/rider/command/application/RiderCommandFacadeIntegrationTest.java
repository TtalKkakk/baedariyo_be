package com.house.biet.rider.command.application;

import com.house.biet.auth.command.application.AuthService;
import com.house.biet.common.domain.enums.UserRole;
import com.house.biet.common.domain.enums.VehicleType;
import com.house.biet.fixtures.RiderFixtures;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.rider.command.RiderRepository;
import com.house.biet.rider.command.domain.entity.Rider;
import com.house.biet.support.config.ServiceIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class RiderCommandFacadeIntegrationTest extends ServiceIntegrationTest {

    @Autowired
    private RiderCommandFacade facade;

    @Autowired
    private AuthService authService;

    @Autowired
    private RiderRepository riderRepository;

    Rider rider;

    @BeforeEach
    void setUp() {
        Account account = authService.signup(
                "test@test.com",
                "<123PASSWORD321>",
                UserRole.RIDER
        );

        rider = RiderFixtures.rider(account);
        riderRepository.save(rider);
    }

    @Test
    @DisplayName("성공 - 닉네임 변경")
    void changeNickname_Success() {
        // given
        Long accountId = rider.getAccount().getId();

        // when
        facade.changeNicknameByRiderId(accountId, "newNick");

        // then
        Rider updated = riderRepository.findById(rider.getId()).orElseThrow();
        assertThat(updated.getNickname().getValue()).isEqualTo("newNick");
    }

    @Test
    @DisplayName("성공 - 차량 타입 변경")
    void changeVehicleType_Success() {
        // given
        Long accountId = rider.getAccount().getId();

        // when
        facade.changeVehicleType(accountId, VehicleType.CAR);

        // then
        Rider updated = riderRepository.findById(rider.getId()).orElseThrow();
        assertThat(updated.getVehicleType()).isEqualTo(VehicleType.CAR);
    }

    @Test
    @DisplayName("성공 - OFFLINE → ONLINE 전환")
    void goOnline_Success() {
        // given
        Long accountId = rider.getAccount().getId();

        // when
        facade.goOnline(accountId);

        // then
        Rider updated = riderRepository.findById(rider.getId()).orElseThrow();
        assertThat(updated.getRiderWorkingStatus().name()).isEqualTo("ONLINE");
    }

    @Test
    @DisplayName("성공 - ONLINE → WORKING → ONLINE 전체 시나리오")
    void deliveryFlow_Success() {
        // given
        Long accountId = rider.getAccount().getId();

        // when
        facade.goOnline(accountId);
        facade.startDelivery(accountId);
        facade.completeDelivery(accountId);

        // then
        Rider updated = riderRepository.findById(rider.getId()).orElseThrow();
        assertThat(updated.getRiderWorkingStatus().name()).isEqualTo("ONLINE");
    }

    @Test
    @DisplayName("성공 - ONLINE → OFFLINE 전환")
    void goOffline_Success() {
        // given
        Long accountId = rider.getAccount().getId();
        facade.goOnline(accountId);

        // when
        facade.goOffline(accountId);

        // then
        Rider updated = riderRepository.findById(rider.getId()).orElseThrow();
        assertThat(updated.getRiderWorkingStatus().name()).isEqualTo("OFFLINE");
    }
}