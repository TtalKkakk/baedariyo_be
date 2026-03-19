package com.house.biet.rider.command.application;

import com.house.biet.common.domain.enums.RiderWorkingStatus;
import com.house.biet.common.domain.enums.VehicleType;
import com.house.biet.rider.query.RiderQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class RiderCommandFacadeTest {

    @Mock
    private RiderService riderService;

    @Mock
    private RiderQueryService riderQueryService;

    @InjectMocks
    private RiderCommandFacade facade;

    @Test
    @DisplayName("성공 - 닉네임 변경")
    void changeNicknameByRiderId_Success() {
        // given
        Long accountId = 1L;
        Long riderId = 10L;
        String newNickname = "newNick";

        given(riderQueryService.getRiderIdByAccountId(accountId)).willReturn(riderId);

        // when
        facade.changeNicknameByRiderId(accountId, newNickname);

        // then
        then(riderQueryService).should().getRiderIdByAccountId(accountId);
        then(riderService).should().changeNicknameByRiderId(riderId, newNickname);
    }

    @Test
    @DisplayName("성공 - 전화번호 변경")
    void changePhoneNumberByRiderId_Success() {
        // given
        Long accountId = 1L;
        Long riderId = 10L;
        String phone = "01012345678";

        given(riderQueryService.getRiderIdByAccountId(accountId)).willReturn(riderId);

        // when
        facade.changePhoneNumberByRiderId(accountId, phone);

        // then
        then(riderService).should().changePhoneNumberByRiderId(riderId, phone);
    }

    @Test
    @DisplayName("성공 - 차량 타입 변경")
    void changeVehicleType_Success() {
        // given
        Long accountId = 1L;
        Long riderId = 10L;
        VehicleType type = VehicleType.MOTORCYCLE;

        given(riderQueryService.getRiderIdByAccountId(accountId)).willReturn(riderId);

        // when
        facade.changeVehicleType(accountId, type);

        // then
        then(riderService).should().changeVehicleType(riderId, type);
    }

    @Test
    @DisplayName("성공 - 근무 상태 변경")
    void changeRiderWorkingStatus_Success() {
        // given
        Long accountId = 1L;
        Long riderId = 10L;
        RiderWorkingStatus status = RiderWorkingStatus.ONLINE;

        given(riderQueryService.getRiderIdByAccountId(accountId)).willReturn(riderId);

        // when
        facade.changeRiderWorkingStatus(accountId, status);

        // then
        then(riderService).should().changeRiderWorkingStatus(riderId, status);
    }

    @Test
    @DisplayName("성공 - 오프라인이면 온라인으로 변경")
    void markOnlineIfOffline_Success() {
        // given
        Long accountId = 1L;
        Long riderId = 10L;

        given(riderQueryService.getRiderIdByAccountId(accountId)).willReturn(riderId);

        // when
        facade.markOnlineIfOffline(accountId);

        // then
        then(riderService).should().markOnlineIfOffline(riderId);
    }
}