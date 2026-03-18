package com.house.biet.user.command.application;

import com.house.biet.common.domain.enums.UserRole;
import com.house.biet.common.domain.vo.Address;
import com.house.biet.global.geocoding.application.GeocodingService;
import com.house.biet.global.geocoding.dto.GeoPoint;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.member.command.domain.vo.*;
import com.house.biet.store.command.domain.vo.GeoLocation;
import com.house.biet.user.command.UserRepository;
import com.house.biet.user.command.domain.aggregate.User;
import com.house.biet.user.command.domain.vo.AddressAlias;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

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
        assertThat(address.getAlias().getValue()).isEqualTo(alias);
        assertThat(address.isDefault()).isTrue();
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

    @Test
    @DisplayName("성공 - 사용자 주소 추가")
    void addAddress_Success() {
        // given
        User user = User.create(
                account,
                "홍길동",
                "<NICKNAME>",
                "010-1111-1111",
                new Address("roadAddress", "jibunAddress", "detailAddress"),
                new GeoLocation(1.0, 1.0),
                new AddressAlias("집")
        );

        given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(user));

        given(geocodingService.geocode(anyString()))
                .willReturn(new GeoPoint(37.1, 127.1));

        String newRoadAddress = "newRoadAddress";
        String newJibunAddress = "newJibunAddress";
        String newDetailAddress = "newDetailAddress";

        // when
        userService.addAddress(
                1L,
                newRoadAddress,
                newJibunAddress,
                newDetailAddress,
                "회사",
                false
        );

        // then
        assertThat(user.getAddresses()).hasSize(2);

        var added = user.getAddresses().get(1);

        assertThat(added.getAddress().getRoadAddress()).isEqualTo(newRoadAddress);
        assertThat(added.getAddress().getJibunAddress()).isEqualTo(newJibunAddress);
        assertThat(added.getAddress().getDetailAddress()).isEqualTo(newDetailAddress);
        assertThat(added.getGeoLocation().getLatitude()).isEqualTo(37.1);
        assertThat(added.getAlias().getValue()).isEqualTo("회사");
    }

    @Test
    @DisplayName("실패 - 존재하지 않는 사용자 주소 추가")
    void addAddress_Error_UserNotFound() {
        // given
        given(userRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.addAddress(
                1L,
                "roadAddress",
                "jibunAddress",
                "detailAddress",
                "집",
                false
        ))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("성공 - 주소 추가 시 기본 배송지로 설정")
    void addAddress_Success_SetDefault() {
        // given
        User user = User.create(
                account,
                "홍길동",
                "<NICKNAME>",
                "010-1111-1111",
                new Address("roadAddress", "jibunAddress", "detailAddress"),
                new GeoLocation(37.0, 127.0),
                new AddressAlias("집")
        );

        given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(user));

        given(geocodingService.geocode(anyString()))
                .willReturn(new GeoPoint(37.0, 127.0));

        // when
        userService.addAddress(
                1L,
                "새도로",
                "새지번",
                "새상세",
                "회사",
                true
        );

        // then
        long defaultCount = user.getAddresses().stream()
                .filter(addr -> addr.isDefault())
                .count();

        assertThat(defaultCount).isEqualTo(1);
    }

    @Test
    @DisplayName("성공 - 기본 배송지 변경")
    void changeDefaultAddress_Success() {
        // given
        User user = User.create(
                account,
                "홍길동",
                "<NICKNAME>",
                "010-1111-1111",
                new Address("roadAddress", "jibunAddress", "detailAddress"),
                new GeoLocation(37.0, 127.0),
                new AddressAlias("집")
        );

        var newAddress = user.addAddress(
                new Address("새", "지번", "상세"),
                new GeoLocation(2.0, 2.0),
                new AddressAlias("회사"),
                false
        );

        given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(user));

        ReflectionTestUtils.setField(user.getAddresses().get(0), "id", 1L); // 기존 기본 주소
        ReflectionTestUtils.setField(newAddress, "id", 2L);

        // when
        userService.changeDefaultAddress(1L, newAddress.getId());

        // then
        assertThat(newAddress.isDefault()).isTrue();
    }

    @Test
    @DisplayName("성공 - 주소 삭제")
    void removeAddress_Success() {
        // given
        User user = User.create(
                account,
                "홍길동",
                "<NICKNAME>",
                "010-1111-1111",
                new Address("roadAddress", "jibunAddress", "detailAddress"),
                new GeoLocation(37.0, 127.0),
                new AddressAlias("집")
        );

        var address = user.addAddress(
                new Address("삭제대상", "지번", "상세"),
                new GeoLocation(2.0, 2.0),
                new AddressAlias("회사"),
                false
        );

        ReflectionTestUtils.setField(user.getAddresses().get(0), "id", 100L);
        ReflectionTestUtils.setField(address, "id", 1L);

        given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(user));

        // when
        userService.removeAddress(1L, 1L);

        // then
        assertThat(user.getAddresses()).hasSize(1);

        assertThat(user.getAddresses())
                .noneMatch(addr -> "회사".equals(addr.getAlias().getValue()));
    }
}