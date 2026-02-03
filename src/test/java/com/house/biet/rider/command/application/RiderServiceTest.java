package com.house.biet.rider.command.application;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.rider.command.RiderRepository;
import com.house.biet.rider.command.domain.entity.Rider;
import com.house.biet.rider.command.domain.vo.RiderWorkingStatus;
import com.house.biet.rider.command.domain.vo.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RiderServiceTest {

    @InjectMocks
    RiderService riderService;

    @Mock
    RiderRepository riderRepository;

    Rider rider;

    @BeforeEach
    void setup() {
        String givenRealName = "<REAL-NAME>";
        String givenNickname = "<NICKNAME>";
        String givenPhoneNumber = "010-1111-1111";
        VehicleType givenVehicleType = VehicleType.MOTORCYCLE;

        rider = Rider.create(givenRealName, givenNickname, givenPhoneNumber, givenVehicleType);
    }

    @Test
    @DisplayName("성공 - rider 저장")
    void save_success() {
        // Given
        String realName = "<REAL-NAME>";
        String nickname = "<NICKNAME>";
        String phoneNumber = "010-1111-1111";
        VehicleType vehicleType = VehicleType.MOTORCYCLE;

        // When
        riderService.save(realName, nickname, phoneNumber, vehicleType);

        // Then
        verify(riderRepository, times(1)).save(any(Rider.class));
    }

    @Test
    @DisplayName("성공 - rider 닉네임 변경")
    void changeNickname_success() {
        // Given
        Long riderId = 1L;
        String newNickname = "<NEW-NICKNAME>";
        given(riderRepository.findById(riderId)).willReturn(Optional.of(rider));

        // When
        riderService.changeNicknameByRiderId(riderId, newNickname);

        // Then
        assertThat(rider.getNickname().getValue()).isEqualTo(newNickname);
    }

    @Test
    @DisplayName("성공 - rider 전화번호 변경")
    void changePhoneNumber_success() {
        // Given
        Long riderId = 1L;
        String newPhoneNumber = "010-2222-2222";
        given(riderRepository.findById(riderId)).willReturn(Optional.of(rider));

        // When
        riderService.changePhoneNumberByRiderId(riderId, newPhoneNumber);

        // Then
        assertThat(rider.getPhoneNumber().getValue()).isEqualTo(newPhoneNumber);
    }

    @Test
    @DisplayName("성공 - rider 이동 수단 변경")
    void changeVehicleType_success() {
        // Given
        Long riderId = 1L;
        VehicleType newVehicleType = VehicleType.BICYCLE;
        given(riderRepository.findById(riderId)).willReturn(Optional.of(rider));

        // When
        riderService.changeVehicleType(riderId, newVehicleType);

        // Then
        assertThat(rider.getVehicleType()).isEqualTo(newVehicleType);
    }

    @Test
    @DisplayName("성공 - rider 근무 상태 변경")
    void changeRiderWorkingStatus_success() {
        // Given
        Long riderId = 1L;
        RiderWorkingStatus newStatus = RiderWorkingStatus.WORKING;
        given(riderRepository.findById(riderId)).willReturn(Optional.of(rider));

        // When
        riderService.changeRiderWorkingStatus(riderId, newStatus);

        // Then
        assertThat(rider.getRiderWorkingStatus()).isEqualTo(newStatus);
    }

    @Test
    @DisplayName("에러 - rider 미존재 시 예외 발생")
    void changeNickname_fail_notFound() {
        // Given
        Long riderId = 99L;
        given(riderRepository.findById(riderId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> riderService.changeNicknameByRiderId(riderId, "<NEW-NICKNAME>"))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.RIDER_NOT_FOUND.getMessage());
    }
}
