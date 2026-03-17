package com.house.biet.user.command.application;

import com.house.biet.common.domain.enums.UserRole;
import com.house.biet.global.geocoding.application.GeocodingService;
import com.house.biet.global.geocoding.dto.GeoPoint;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.member.command.domain.vo.*;
import com.house.biet.user.command.UserRepository;
import com.house.biet.user.command.domain.aggregate.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GeocodingService geocodingService;

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    Account account;

    @BeforeEach
    void setup() {
        String email = "abc@xyz.com";
        String password = UUID.randomUUID().toString().substring(1, 30);

        account = Account.signup(
                new Email(email),
                Password.encrypt(password, ENCODER),
                UserRole.USER
        );
    }

    @Test
    @DisplayName("성공 - user 저장 (주소 → 좌표 변환 포함)")
    void saveUser_Success() {
        // given
        String road = "서울특별시 마포구 동교로 34";
        String jibun = "서울특별시 마포구 서교동";
        String detail = "101호";
        String alias = "집";

        GeoPoint mockPoint = new GeoPoint(37.5563, 126.9220);

        given(geocodingService.geocode(anyString()))
                .willReturn(mockPoint);

        given(userRepository.save(any(User.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when
        User savedUser = userService.save(
                account,
                "홍길동",
                "nickname",
                "010-1111-1111",
                road,
                jibun,
                detail,
                alias
        );

        // then
        then(geocodingService).should().geocode(road);
        then(userRepository).should().save(any(User.class));

        assertThat(savedUser.getAddresses()).hasSize(1);

        var address = savedUser.getAddresses().get(0);

        assertThat(address.getAddress().getRoadAddress()).isEqualTo(road);
        assertThat(address.getGeoLocation().getLatitude()).isEqualTo(37.5563);
        assertThat(address.getGeoLocation().getLongitude()).isEqualTo(126.9220);
        assertThat(address.getAlias()).isEqualTo(alias);
        assertThat(address.isDefault()).isTrue();
    }

    @Test
    @DisplayName("실패 - Geocoding 실패 시 예외 발생")
    void saveUser_Error_GeocodingFail() {
        // given
        given(geocodingService.geocode(anyString()))
                .willThrow(new RuntimeException());

        // when & then
        assertThatThrownBy(() -> userService.save(
                account,
                "홍길동",
                "nickname",
                "010",
                "잘못된주소",
                "지번",
                "상세",
                "집"
        )).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("성공 - nickname으로 user nickname 변경")
    void changeNickname_Success() {
        // given
        User user = mock(User.class);

        given(userRepository.findByNickname(any(Nickname.class)))
                .willReturn(Optional.of(user));

        // when
        userService.changeNickname("newNick");

        // then
        then(user).should().changeNickname(any(Nickname.class));
    }

    @Test
    @DisplayName("실패 - 존재하지 않는 nickname")
    void changeNickname_Error_NotFound() {
        // given
        given(userRepository.findByNickname(any()))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.changeNickname("newNicknameValue"))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("성공 - id로 nickname 변경")
    void changeNicknameByUserId_Success() {
        // given
        User user = mock(User.class);

        given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(user));

        // when
        userService.changeNicknameByUserId(1L, "newNick");

        // then
        then(user).should().changeNickname(any(Nickname.class));
    }

    @Test
    @DisplayName("실패 - 존재하지 않는 id로 nickname 변경")
    void changeNicknameByUserId_Error() {
        // given
        given(userRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.changeNicknameByUserId(1L, "fail"))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("성공 - id로 phone number 변경")
    void changePhoneNumberByUserId_Success() {
        // given
        User user = mock(User.class);

        given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(user));

        // when
        userService.changePhoneNumberByUserId(1L, "010-0000-0002");

        // then
        then(user).should().changePhoneNumber(any(PhoneNumber.class));
    }

    @Test
    @DisplayName("실패 - 존재하지 않는 id로 phone number 변경")
    void changePhoneNumberByUserId_Error() {
        // given
        given(userRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.changePhoneNumberByUserId(1L, "fail"))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.USER_NOT_FOUND.getMessage());
    }
}