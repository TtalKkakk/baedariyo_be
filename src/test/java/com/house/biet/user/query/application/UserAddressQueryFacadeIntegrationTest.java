package com.house.biet.user.query.application;

import com.house.biet.auth.command.application.AuthService;
import com.house.biet.common.domain.enums.UserRole;
import com.house.biet.global.geocoding.application.GeocodingService;
import com.house.biet.global.geocoding.dto.GeoPoint;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.support.config.ServiceIntegrationTest;
import com.house.biet.user.command.application.UserService;
import com.house.biet.user.command.domain.aggregate.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;

class UserAddressQueryFacadeIntegrationTest extends ServiceIntegrationTest {

    @Autowired
    private UserAddressQueryFacade userAddressQueryFacade;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @MockitoBean
    private GeocodingService geocodingService;

    Account account;
    User user;

    String email = "test@test.com";
    String password = "<TEST_PASSWORD>";
    String realName = "<REAL_NAME>";
    String nickname = "<NICKNAME>";
    String phoneNumber = "010-1111-1111";
    String roadAddress = "roadAddress";
    String jibunAddress = "jibunAddress";
    String detailAddress = "detailAddress";
    String alias = "집";

    @BeforeEach
    void setup() {
        // Geocoding Mock
        given(geocodingService.geocode(any()))
                .willReturn(new GeoPoint(37.0, 127.0));

        account = authService.signup(
                email,
                password,
                UserRole.USER
        );

        user = userService.save(
                account,
                realName,
                nickname,
                phoneNumber,
                roadAddress,
                jibunAddress,
                detailAddress,
                alias
        );
    }

    @Test
    @DisplayName("성공 - 사용자 배송지 조회 Integration")
    void getAllAddressesIntegration_Success() {
        // when
        List<com.house.biet.user.command.dto.AddressResponseDto> addresses =
                userAddressQueryFacade.getAllAddresses(account.getId());

        // then
        assertThat(addresses).hasSize(1);
        assertThat(addresses.get(0).roadAddress()).isEqualTo(roadAddress);
        assertThat(addresses.get(0).jibunAddress()).isEqualTo(jibunAddress);
        assertThat(addresses.get(0).detailAddress()).isEqualTo(detailAddress);
        assertThat(addresses.get(0).alias()).isEqualTo(alias);
    }
}