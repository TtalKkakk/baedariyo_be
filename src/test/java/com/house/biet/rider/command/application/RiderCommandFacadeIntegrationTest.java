package com.house.biet.rider.command.application;

import com.house.biet.auth.command.application.AuthService;
import com.house.biet.common.domain.enums.RiderWorkingStatus;
import com.house.biet.common.domain.enums.UserRole;
import com.house.biet.common.domain.enums.VehicleType;
import com.house.biet.fixtures.RiderFixtures;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.rider.command.RiderRepository;
import com.house.biet.rider.command.domain.entity.Rider;
import com.house.biet.support.config.ServiceIntegrationTest;
import jakarta.persistence.EntityManager;
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
        String newNickname = "newNick";

        // when
        facade.changeNicknameByRiderId(accountId, newNickname);

        // then
        Rider updated = riderRepository.findById(rider.getId()).orElseThrow();
        assertThat(updated.getNickname().getValue()).isEqualTo(newNickname);
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
    @DisplayName("성공 - 근무 상태 변경")
    void changeWorkingStatus_Success() {
        // given
        Long accountId = rider.getAccount().getId();

        // when
        facade.changeRiderWorkingStatus(accountId, RiderWorkingStatus.ONLINE);

        // then
        Rider updated = riderRepository.findById(rider.getId()).orElseThrow();
        assertThat(updated.getRiderWorkingStatus()).isEqualTo(RiderWorkingStatus.ONLINE);
    }

    @Test
    @DisplayName("성공 - 오프라인이면 온라인으로 변경")
    void markOnlineIfOffline_Success() {
        // given
        Long accountId = rider.getAccount().getId();
        rider.changeRiderWorkingStatus(RiderWorkingStatus.OFFLINE);

        // when
        facade.markOnlineIfOffline(accountId);

        // then
        Rider updated = riderRepository.findById(rider.getId()).orElseThrow();
        assertThat(updated.getRiderWorkingStatus()).isEqualTo(RiderWorkingStatus.ONLINE);
    }
}
