package com.house.biet.rider.command.domain.entity;

import com.house.biet.global.vo.UserRole;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.member.command.domain.vo.Email;
import com.house.biet.member.command.domain.vo.Nickname;
import com.house.biet.member.command.domain.vo.Password;
import com.house.biet.member.command.domain.vo.PhoneNumber;
import com.house.biet.rider.command.domain.vo.RiderWorkingStatus;
import com.house.biet.rider.command.domain.vo.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

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
    @DisplayName("성공 - 라이더 근무 상태 변경 성공")
    void ChangeRiderWorkingStatus_Success() {
        // given
        Rider rider = Rider.create(
                account,
                givenRealNameValue,
                givenNickNameValue,
                givenPhoneNumberValue,
                givenVehicleType
        );
        RiderWorkingStatus newStatus = RiderWorkingStatus.WORKING;

        // when
        rider.changeRiderWorkingStatus(newStatus);

        // then
        assertThat(rider.getRiderWorkingStatus()).isEqualTo(newStatus);
    }
}
