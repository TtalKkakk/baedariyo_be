package com.house.biet.user.command.domain.entity;

import com.house.biet.common.domain.vo.Address;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.store.command.domain.vo.GeoLocation;
import com.house.biet.user.command.domain.aggregate.User;
import com.house.biet.user.command.domain.vo.AddressAlias;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserAddressTest {

    private final User user = User.create(null, "<REAL_NAME>", "<NICKNAME>", "010-1234-5678", null, null, null);
    private final Address address = new Address("roadAddress", "jibunAddress", "detailAddress");
    private final GeoLocation geoLocation = new GeoLocation(37.123, 127.123);
    private final AddressAlias alias = new AddressAlias("집");

    @Test
    @DisplayName("성공 - UserAddress 생성")
    void create_Success() {
        // given
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
    @DisplayName("성공 - alias 변경")
    void changeAlias_Success() {
        // given
        UserAddress userAddress =
                UserAddress.create(user, address, geoLocation, alias, true);

        AddressAlias newAlias = new AddressAlias("회사");

        // when
        userAddress.changeAlias(newAlias);

        // then
        assertThat(userAddress.getAlias()).isEqualTo(newAlias);
    }

    @Test
    @DisplayName("성공 - 기본 배송지 설정")
    void setDefault_Success() {
        // given
        UserAddress userAddress =
                UserAddress.create(user, address, geoLocation, alias, false);

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
                UserAddress.create(user, address, geoLocation, alias, true);

        // when
        userAddress.unsetDefault();

        // then
        assertThat(userAddress.isDefault()).isFalse();
    }
}