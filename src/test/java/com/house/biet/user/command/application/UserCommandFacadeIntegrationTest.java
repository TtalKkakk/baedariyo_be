package com.house.biet.user.command.application;

import com.house.biet.auth.command.application.AuthService;
import com.house.biet.common.domain.enums.UserRole;
import com.house.biet.common.domain.vo.Address;
import com.house.biet.global.geocoding.application.GeocodingService;
import com.house.biet.global.geocoding.dto.GeoPoint;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.support.config.ServiceIntegrationTest;
import com.house.biet.user.command.UserRepository;
import com.house.biet.user.command.domain.aggregate.User;
import com.house.biet.user.command.domain.entity.UserAddress;
import com.house.biet.user.command.domain.vo.AddressAlias;
import com.house.biet.store.command.domain.vo.GeoLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class UserCommandFacadeIntegrationTest extends ServiceIntegrationTest {

    @Autowired
    private UserCommandFacade userCommandFacade;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private GeocodingService geocodingService;

    Account account;
    User user;

    @BeforeEach
    void setup() {
        given(geocodingService.geocode(any()))
                .willReturn(new GeoPoint(37.0, 127.0));

        String email = "test@test.com";
        String password = "<TEST_PASSWORD>";

        account = authService.signup(
                email,
                password,
                UserRole.USER
        );

        String realName = "<REAL_NAME>";
        String nickname = "<NICKNAME>";
        String phoneNumber = "010-1111-1111";
        String roadAddress = "roadAddress";
        String jibunAddress = "jibunAddress";
        String detailAddress = "detailAddress";
        String alias = "집";

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
    @DisplayName("성공 - 주소 추가 및 기본 배송지 설정")
    void addAddressIntegration_Success() {
        // when
        userCommandFacade.addAddress(
                account.getId(),
                "newRoadAddress",
                "newJibunAddress",
                "newDetailAddress",
                "회사",
                true
        );

        // then
        User savedUser = userRepository.findById(user.getId()).orElseThrow();

        assertThat(savedUser.getAddresses()).hasSize(2);
        assertThat(savedUser.getAddresses())
                .anyMatch(addr -> addr.getAlias().getValue().equals("회사") && addr.isDefault());
    }

    @Test
    @DisplayName("성공 - 기본 배송지 변경")
    void changeDefaultAddressIntegration_Success() {
        // given
        String addAliasValue = "회사";
        
        userCommandFacade.addAddress(
                account.getId(),
                "newRoadAddress",
                "newJibunAddress",
                "newDetailAddress",
                addAliasValue,
                false
        );
        
        // when
        userCommandFacade.changeDefaultAddress(account.getId(), addAliasValue);

        // then
        User savedUser = userRepository.findById(user.getId()).orElseThrow();

        assertThat(savedUser.getAddresses())
                .anyMatch(addr -> addr.getAlias().getValue().equals(addAliasValue) && addr.isDefault());

        assertThat(savedUser.getAddresses())
                .anyMatch(addr -> addr.getAlias().getValue().equals("집") && !addr.isDefault());
    }

    @Test
    @DisplayName("성공 - 주소 삭제")
    void removeAddressIntegration_Success() {
        // given
        String removeAliasValue = "회사";

        UserAddress address = user.addAddress(
                new Address("삭제대상", "지번", "상세"),
                new GeoLocation(2.0, 2.0),
                new AddressAlias(removeAliasValue),
                false
        );

        // when
        userCommandFacade.removeAddress(account.getId(), removeAliasValue);

        // then
        User savedUser = userRepository.findById(user.getId()).orElseThrow();

        assertThat(savedUser.getAddresses()).hasSize(1);
        assertThat(savedUser.getAddresses())
                .noneMatch(addr -> addr.getAlias().getValue().equals("회사"));
    }

    @Test
    @DisplayName("성공 - 배송지 별칭 변경")
    void changeAddressAliasIntegration_Success() {
        // given
        String oldAlias = "집";
        String newAlias = "우리집";

        // when
        userCommandFacade.changeAddressAlias(
                account.getId(),
                oldAlias,
                newAlias
        );

        // then
        User savedUser = userRepository.findById(user.getId()).orElseThrow();

        assertThat(savedUser.getAddresses())
                .anyMatch(addr -> addr.getAlias().getValue().equals(newAlias));

        assertThat(savedUser.getAddresses())
                .noneMatch(addr -> addr.getAlias().getValue().equals(oldAlias));
    }
}