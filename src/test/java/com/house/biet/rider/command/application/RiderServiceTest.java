package com.house.biet.rider.command.application;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.common.domain.enums.UserRole;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.member.command.domain.vo.Email;
import com.house.biet.member.command.domain.vo.Password;
import com.house.biet.rider.command.RiderRepository;
import com.house.biet.rider.command.domain.entity.Rider;
import com.house.biet.common.domain.enums.RiderWorkingStatus;
import com.house.biet.common.domain.enums.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RiderServiceTest {

    @InjectMocks
    RiderService riderService;

    @Mock
    RiderRepository riderRepository;

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    Account account;
    Rider rider;

    @BeforeEach
    void setup() {
        String givenEmail = "abc@xyz.com";
        String givenPassword = UUID.randomUUID().toString().substring(1, 30);

        account = Account.signup(
                new Email(givenEmail),
                Password.encrypt(givenPassword, ENCODER),
                UserRole.RIDER
        );

        rider = Rider.create(
                account,
                "<REAL-NAME>",
                "<NICKNAME>",
                "010-1111-1111",
                VehicleType.MOTORCYCLE
        );
    }

    @Test
    @DisplayName("성공 - rider 저장")
    void save_success() {
        // when
        riderService.save(account, "<REAL-NAME>", "<NICKNAME>", "010-1111-1111", VehicleType.MOTORCYCLE);

        // then
        verify(riderRepository).save(any(Rider.class));
    }

    @Test
    @DisplayName("성공 - 닉네임 변경")
    void changeNickname_success() {
        // given
        Long riderId = 1L;
        given(riderRepository.findById(riderId)).willReturn(Optional.of(rider));

        // when
        riderService.changeNicknameByRiderId(riderId, "<NEW>");

        // then
        assertThat(rider.getNickname().getValue()).isEqualTo("<NEW>");
    }

    @Test
    @DisplayName("성공 - 전화번호 변경")
    void changePhoneNumber_success() {
        // given
        Long riderId = 1L;
        given(riderRepository.findById(riderId)).willReturn(Optional.of(rider));

        // when
        riderService.changePhoneNumberByRiderId(riderId, "010-2222-2222");

        // then
        assertThat(rider.getPhoneNumber().getValue()).isEqualTo("010-2222-2222");
    }

    @Test
    @DisplayName("성공 - 차량 타입 변경")
    void changeVehicleType_success() {
        // given
        Long riderId = 1L;
        given(riderRepository.findById(riderId)).willReturn(Optional.of(rider));

        // when
        riderService.changeVehicleType(riderId, VehicleType.BICYCLE);

        // then
        assertThat(rider.getVehicleType()).isEqualTo(VehicleType.BICYCLE);
    }

    @Test
    @DisplayName("성공 - OFFLINE → ONLINE 전환")
    void goOnline_success() {
        // given
        Long riderId = 1L;
        given(riderRepository.findById(riderId)).willReturn(Optional.of(rider));

        // when
        riderService.goOnline(riderId);

        // then
        assertThat(rider.getRiderWorkingStatus()).isEqualTo(RiderWorkingStatus.ONLINE);
    }

    @Test
    @DisplayName("성공 - ONLINE → WORKING 전환")
    void startDelivery_success() {
        // given
        Long riderId = 1L;
        rider.goOnline();
        given(riderRepository.findById(riderId)).willReturn(Optional.of(rider));

        // when
        riderService.startDelivery(riderId);

        // then
        assertThat(rider.getRiderWorkingStatus()).isEqualTo(RiderWorkingStatus.WORKING);
    }

    @Test
    @DisplayName("성공 - WORKING → ONLINE 전환")
    void completeDelivery_success() {
        // given
        Long riderId = 1L;
        rider.goOnline();
        rider.startDelivery();
        given(riderRepository.findById(riderId)).willReturn(Optional.of(rider));

        // when
        riderService.completeDelivery(riderId);

        // then
        assertThat(rider.getRiderWorkingStatus()).isEqualTo(RiderWorkingStatus.ONLINE);
    }

    @Test
    @DisplayName("성공 - ONLINE → OFFLINE 전환")
    void goOffline_success() {
        // given
        Long riderId = 1L;
        rider.goOnline();
        given(riderRepository.findById(riderId)).willReturn(Optional.of(rider));

        // when
        riderService.goOffline(riderId);

        // then
        assertThat(rider.getRiderWorkingStatus()).isEqualTo(RiderWorkingStatus.OFFLINE);
    }

    @Test
    @DisplayName("실패 - 라이더 없음")
    void getRider_Error_NotFound() {
        // given
        Long riderId = 99L;
        given(riderRepository.findById(riderId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> riderService.goOnline(riderId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.RIDER_NOT_FOUND.getMessage());
    }
}