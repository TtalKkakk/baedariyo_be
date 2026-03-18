package com.house.biet.user.command.domain.aggregate;

import com.house.biet.common.domain.enums.UserRole;
import com.house.biet.common.domain.vo.Address;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.member.command.domain.vo.*;
import com.house.biet.store.command.domain.vo.GeoLocation;
import com.house.biet.user.command.domain.entity.UserAddress;
import com.house.biet.user.command.domain.vo.AddressAlias;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    String givenEmail = "abc@xyz.com";
    String givenPassword = UUID.randomUUID().toString().substring(1, 30);

    String givenRealNameValue = "<REAL_NAME>";
    String givenNickNameValue = "<NICK_NAME>";
    String givenPhoneNumberValue = "010-1111-1111";

    Address givenAddress;
    GeoLocation givenGeoLocation;
    AddressAlias givenAlias = new AddressAlias("집");

    Account account;

    @BeforeEach
    void setup() {
        account = Account.signup(
                new Email(givenEmail),
                Password.encrypt(givenPassword, ENCODER),
                UserRole.USER
        );

        givenAddress = new Address(
                "서울특별시 마포구 동교로 34",
                "서울특별시 마포구 서교동",
                "101호"
        );

        givenGeoLocation = new GeoLocation(
                37.5563,
                126.9220
        );
    }

    @Test
    @DisplayName("성공 - 유저 생성 시 기본 배송지 포함")
    void CreateUser_Success() {
        // when
        User user = User.create(
                account,
                givenRealNameValue,
                givenNickNameValue,
                givenPhoneNumberValue,
                givenAddress,
                givenGeoLocation,
                givenAlias
        );

        // then
        assertThat(user.getRealName().getValue()).isEqualTo(givenRealNameValue);
        assertThat(user.getNickname().getValue()).isEqualTo(givenNickNameValue);
        assertThat(user.getPhoneNumber().getValue()).isEqualTo(givenPhoneNumberValue);

        // 🔥 핵심 검증
        assertThat(user.getAddresses()).hasSize(1);

        UserAddress address = user.getAddresses().get(0);

        assertThat(address.getAddress()).isEqualTo(givenAddress);
        assertThat(address.getGeoLocation()).isEqualTo(givenGeoLocation);
        assertThat(address.getAlias()).isEqualTo(givenAlias);
        assertThat(address.isDefault()).isTrue();
    }

    @Test
    @DisplayName("성공 - 유저 닉네임 변경 성공")
    void changeNickname_Success() {
        // given
        User user = User.create(
                account,
                givenRealNameValue,
                givenNickNameValue,
                givenPhoneNumberValue,
                givenAddress,
                givenGeoLocation,
                givenAlias
        );

        Nickname newNickname = new Nickname("<NEW_NICKNAME>");

        // when
        user.changeNickname(newNickname);

        // then
        assertThat(user.getNickname()).isEqualTo(newNickname);
    }

    @Test
    @DisplayName("성공 - 유저 휴대전화번호 변경 성공")
    void changePhoneNumber_Success() {
        // given
        User user = User.create(
                account,
                givenRealNameValue,
                givenNickNameValue,
                givenPhoneNumberValue,
                givenAddress,
                givenGeoLocation,
                givenAlias
        );

        PhoneNumber newPhoneNumber = new PhoneNumber("010-1111-1112");

        // when
        user.changePhoneNumber(newPhoneNumber);

        // then
        assertThat(user.getPhoneNumber()).isEqualTo(newPhoneNumber);
    }

    @Test
    @DisplayName("성공 - 주소 추가 시 기본 배송지 해제 및 새 주소 default 설정")
    void addAddress_Success_DefaultSwitch() {
        // given
        User user = User.create(
                account,
                givenRealNameValue,
                givenNickNameValue,
                givenPhoneNumberValue,
                givenAddress,
                givenGeoLocation,
                givenAlias
        );

        // when
        user.addAddress(
                new Address("새 도로", "새 지번", "새 상세"),
                givenGeoLocation,
                new AddressAlias("회사"),
                true
        );

        // then
        assertThat(user.getAddresses()).hasSize(2);

        assertThat(user.getAddresses())
                .filteredOn(UserAddress::isDefault)
                .hasSize(1)
                .first()
                .extracting(addr -> addr.getAlias().getValue())
                .isEqualTo("회사");
    }

    @Test
    @DisplayName("실패 - 주소 alias 중복")
    void addAddress_Error_DuplicateAlias() {
        // given
        User user = User.create(
                account,
                givenRealNameValue,
                givenNickNameValue,
                givenPhoneNumberValue,
                givenAddress,
                givenGeoLocation,
                givenAlias
        );

        // when & then
        assertThatThrownBy(() ->
                user.addAddress(
                        new Address("다른 주소", "지번", "상세"),
                        givenGeoLocation,
                        new AddressAlias("집"), // 이미 있음
                        false
                )
        )
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.DUPLICATE_ADDRESS_ALIAS.getMessage());
    }

    @Test
    @DisplayName("성공 - 배송지 별명 변경")
    void changeAddressAlias_Success() {
        // given
        User user = User.create(
                account,
                givenRealNameValue,
                givenNickNameValue,
                givenPhoneNumberValue,
                givenAddress,
                givenGeoLocation,
                givenAlias
        );

        user.addAddress(
                new Address("회사 주소", "지번", "상세"),
                givenGeoLocation,
                new AddressAlias("회사"),
                false
        );

        // when
        user.changeAddressAlias("회사", "회사2");

        // then
        assertThat(user.getAddresses())
                .extracting(addr -> addr.getAlias().getValue())
                .contains("회사2")
                .doesNotContain("회사");
    }

    @Test
    @DisplayName("실패 - 변경할 배송지 별명 중복")
    void changeAddressAlias_Error_DuplicateAlias() {
        // given
        User user = User.create(
                account,
                givenRealNameValue,
                givenNickNameValue,
                givenPhoneNumberValue,
                givenAddress,
                givenGeoLocation,
                givenAlias
        );

        user.addAddress(
                new Address("회사 주소", "지번", "상세"),
                givenGeoLocation,
                new AddressAlias("회사"),
                false
        );

        // when & then
        assertThatThrownBy(() ->
                user.changeAddressAlias("회사", "집") // 이미 존재
        ).isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("성공 - 기본 배송지 변경")
    void changeDefaultAddress_Success() {
        // given
        User user = User.create(
                account,
                givenRealNameValue,
                givenNickNameValue,
                givenPhoneNumberValue,
                givenAddress,
                givenGeoLocation,
                givenAlias
        );

        user.addAddress(
                new Address("회사 주소", "지번", "상세"),
                givenGeoLocation,
                new AddressAlias("회사"),
                false
        );

        // when
        user.changeDefaultAddress("회사");

        // then
        assertThat(user.getAddresses())
                .filteredOn(UserAddress::isDefault)
                .hasSize(1)
                .first()
                .extracting(addr -> addr.getAlias().getValue())
                .isEqualTo("회사");
    }
}