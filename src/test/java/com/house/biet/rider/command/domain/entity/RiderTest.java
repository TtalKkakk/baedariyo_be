package com.house.biet.rider.command.domain.entity;

import com.house.biet.common.domain.enums.UserRole;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.member.command.domain.vo.Email;
import com.house.biet.member.command.domain.vo.Nickname;
import com.house.biet.member.command.domain.vo.Password;
import com.house.biet.member.command.domain.vo.PhoneNumber;
import com.house.biet.common.domain.enums.RiderWorkingStatus;
import com.house.biet.common.domain.enums.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RiderTest {

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    String givenEmail = "abc@xyz.com";
    String givenPassword = UUID.randomUUID().toString().substring(1, 30);

    String givenRealNameValue = "<REAL_NAME>";
    String givenNickNameValue = "<NICK_NAME>";
    String givenPhoneNumberValue = "010-1111-1111";
    VehicleType givenVehicleType = VehicleType.MOTORCYCLE;

    Account account;

    @BeforeEach
    void setup() {
        account = Account.signup(
                new Email(givenEmail),
                Password.encrypt(givenPassword, ENCODER),
                UserRole.RIDER
        );
    }

    @Test
    @DisplayName("성공 - rider 생성 성공")
    void CreateRider_Success() {
        // when
        Rider rider = Rider.create(
                account,
                givenRealNameValue,
                givenNickNameValue,
                givenPhoneNumberValue,
                givenVehicleType
        );

        // then
        assertThat(rider.getRealName().getValue()).isEqualTo(givenRealNameValue);
        assertThat(rider.getNickname().getValue()).isEqualTo(givenNickNameValue);
        assertThat(rider.getPhoneNumber().getValue()).isEqualTo(givenPhoneNumberValue);
        assertThat(rider.getVehicleType()).isEqualTo(givenVehicleType);
        assertThat(rider.getRiderWorkingStatus()).isEqualTo(RiderWorkingStatus.OFFLINE);
    }

    @Test
    @DisplayName("성공 - 닉네임 변경 성공")
    void ChangeNickname_Success() {
        // given
        Rider rider = Rider.create(
                account,
                givenRealNameValue,
                givenNickNameValue,
                givenPhoneNumberValue,
                givenVehicleType
        );
        Nickname newNickname = new Nickname("NEW_NICKNAME");

        // when
        rider.changeNickname(newNickname);

        // then
        assertThat(rider.getNickname()).isEqualTo(newNickname);
    }

    @Test
    @DisplayName("성공 - 전화번호 변경 성공")
    void ChangePhoneNumber_Success() {
        // given
        Rider rider = Rider.create(
                account,
                givenRealNameValue,
                givenNickNameValue,
                givenPhoneNumberValue,
                givenVehicleType
        );
        PhoneNumber newPhoneNumber = new PhoneNumber("010-2222-2222");

        // when
        rider.changePhoneNumber(newPhoneNumber);

        // then
        assertThat(rider.getPhoneNumber()).isEqualTo(newPhoneNumber);
    }

    @Test
    @DisplayName("성공 - 차량 타입 변경 성공")
    void ChangeVehicleType_Success() {
        // given
        Rider rider = Rider.create(
                account,
                givenRealNameValue,
                givenNickNameValue,
                givenPhoneNumberValue,
                givenVehicleType
        );
        VehicleType newVehicleType = VehicleType.BICYCLE;

        // when
        rider.changeVehicleType(newVehicleType);

        // then
        assertThat(rider.getVehicleType()).isEqualTo(newVehicleType);
    }

    @Test
    @DisplayName("성공 - 오프라인 상태에서 온라인 전환")
    void GoOnline_Success() {
        // given
        Rider rider = Rider.create(
                account,
                givenRealNameValue,
                givenNickNameValue,
                givenPhoneNumberValue,
                givenVehicleType
        );

        // when
        rider.goOnline();

        // then
        assertThat(rider.getRiderWorkingStatus()).isEqualTo(RiderWorkingStatus.ONLINE);
    }

    @Test
    @DisplayName("실패 - 오프라인이 아닌 상태에서 온라인 전환")
    void GoOnline_Error_NotOffline() {
        // given
        Rider rider = Rider.create(
                account,
                givenRealNameValue,
                givenNickNameValue,
                givenPhoneNumberValue,
                givenVehicleType
        );
        rider.goOnline();

        // when & then
        assertThatThrownBy(rider::goOnline)
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.RIDER_MUST_BE_OFFLINE_TO_GO_ONLINE.getMessage());
    }

    @Test
    @DisplayName("성공 - 온라인 상태에서 배달 시작")
    void StartDelivery_Success() {
        // given
        Rider rider = Rider.create(
                account,
                givenRealNameValue,
                givenNickNameValue,
                givenPhoneNumberValue,
                givenVehicleType
        );
        rider.goOnline();

        // when
        rider.startDelivery();

        // then
        assertThat(rider.getRiderWorkingStatus()).isEqualTo(RiderWorkingStatus.WORKING);
    }

    @Test
    @DisplayName("실패 - 오프라인 상태에서 바로 배달 시작 시 예외")
    void StartDelivery_Error_NotOnline() {
        // given
        Rider rider = Rider.create(
                account,
                givenRealNameValue,
                givenNickNameValue,
                givenPhoneNumberValue,
                givenVehicleType
        );

        // when & then
        assertThatThrownBy(rider::startDelivery)
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.RIDER_MUST_BE_ONLINE_TO_START_WORK.getMessage());
    }

    @Test
    @DisplayName("성공 - 배달 중 상태에서 배달 완료")
    void CompleteDelivery_Success() {
        // given
        Rider rider = Rider.create(
                account,
                givenRealNameValue,
                givenNickNameValue,
                givenPhoneNumberValue,
                givenVehicleType
        );
        rider.goOnline();
        rider.startDelivery();

        // when
        rider.completeDelivery();

        // then
        assertThat(rider.getRiderWorkingStatus()).isEqualTo(RiderWorkingStatus.ONLINE);
    }

    @Test
    @DisplayName("실패 - 배달 중이 아닐 때 완료 시 예외")
    void CompleteDelivery_Error_NotWorking() {
        // given
        Rider rider = Rider.create(
                account,
                givenRealNameValue,
                givenNickNameValue,
                givenPhoneNumberValue,
                givenVehicleType
        );
        rider.goOnline();

        // when & then
        assertThatThrownBy(rider::completeDelivery)
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.RIDER_MUST_BE_WORKING_TO_COMPLETE.getMessage());
    }

    @Test
    @DisplayName("성공 - 온라인 상태에서 오프라인 전환")
    void GoOffline_Success() {
        // given
        Rider rider = Rider.create(
                account,
                givenRealNameValue,
                givenNickNameValue,
                givenPhoneNumberValue,
                givenVehicleType
        );
        rider.goOnline();

        // when
        rider.goOffline();

        // then
        assertThat(rider.getRiderWorkingStatus()).isEqualTo(RiderWorkingStatus.OFFLINE);
    }

    @Test
    @DisplayName("실패 - 온라인 상태가 아닐 때 오프라인 전환 시 예외")
    void GoOffline_Error_NotOnline() {
        // given
        Rider rider = Rider.create(
                account,
                givenRealNameValue,
                givenNickNameValue,
                givenPhoneNumberValue,
                givenVehicleType
        );

        // when & then
        assertThatThrownBy(rider::goOffline)
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.RIDER_MUST_BE_ONLINE_TO_GO_OFFLINE.getMessage());
    }
}
