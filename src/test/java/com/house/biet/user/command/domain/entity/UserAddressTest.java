package com.house.biet.user.command.domain.entity;

import com.house.biet.common.domain.vo.Address;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.store.command.domain.vo.GeoLocation;
import com.house.biet.user.command.domain.aggregate.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class UserAddressTest {

    private final User user = User.create(null, "<REAL_NAME>", "<NICKNAME>", "010-1234-5678");
    private final Address address = new Address("roadAddress", "jibunAddress", "detailAddress");
    private final GeoLocation geoLocation = new GeoLocation(37.123, 127.123);

    @Test
    @DisplayName("성공 - UserAddress 생성")
    void create_Success() {
        // given
        String alias = "집";
        boolean isDefault = true;

        // when
        UserAddress userAddress =
                UserAddress.create(user, address, geoLocation, alias, isDefault);

        // then
        assertThat(userAddress.getUser()).isEqualTo(user);
        assertThat(userAddress.getAddress()).isEqualTo(address);
        assertThat(userAddress.getGeoLocation()).isEqualTo(geoLocation);
        assertThat(userAddress.getAlias()).isEqualTo(alias);
        assertThat(userAddress.isDefault()).isEqualTo(isDefault);
    }

    @Test
    @DisplayName("실패 - alias가 null이면 생성 실패")
    void create_Error_InvalidAlias_Null() {
        // given
        String alias = null;

        // when & then
        assertThatThrownBy(() -> UserAddress.create(user, address, geoLocation, alias, true))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_ADDRESS_ALIAS_FORMAT.getMessage());
    }

    @Test
    @DisplayName("실패 - alias가 공백이면 생성 실패")
    void create_Error_InvalidAlias_Blank() {
        // given
        String alias = " ";

        // when & then
        assertThatThrownBy(() ->
                UserAddress.create(user, address, geoLocation, alias, true)
        ).isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("성공 - alias 변경")
    void changeAlias_Success() {
        // given
        UserAddress userAddress =
                UserAddress.create(user, address, geoLocation, "집", true);

        String newAlias = "회사";

        // when
        userAddress.changeAlias(newAlias);

        // then
        assertThat(userAddress.getAlias()).isEqualTo(newAlias);
    }

    @Test
    @DisplayName("실패 - alias 변경 시 null")
    void changeAlias_Error_Null() {
        // given
        UserAddress userAddress =
                UserAddress.create(user, address, geoLocation, "집", true);

        // when & then
        assertThatThrownBy(() -> userAddress.changeAlias(null))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_ADDRESS_ALIAS_FORMAT.getMessage());

    }

    @Test
    @DisplayName("성공 - 기본 배송지 설정")
    void setDefault_Success() {
        // given
        UserAddress userAddress =
                UserAddress.create(user, address, geoLocation, "집", false);

        // when
        userAddress.setAsDefault();

        // then
        assertThat(userAddress.isDefault()).isTrue();
    }

    @Test
    @DisplayName("성공 - 기본 배송지 해제")
    void unsetDefault_Success() {
        // given
        UserAddress userAddress =
                UserAddress.create(user, address, geoLocation, "집", true);

        // when
        userAddress.unsetDefault();

        // then
        assertThat(userAddress.isDefault()).isFalse();
    }
}