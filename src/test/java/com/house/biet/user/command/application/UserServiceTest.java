package com.house.biet.user.command.application;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.common.domain.enums.UserRole;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.member.command.domain.vo.Email;
import com.house.biet.member.command.domain.vo.Nickname;
import com.house.biet.member.command.domain.vo.Password;
import com.house.biet.user.command.UserRepository;
import com.house.biet.user.command.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    Account account;
    User user;

    @BeforeEach
    void setup() {
        String givenEmail = "abc@xyz.com";
        String givenPassword = UUID.randomUUID().toString().substring(1, 30);

        account = Account.signup(
                new Email(givenEmail),
                Password.encrypt(givenPassword, ENCODER),
                UserRole.USER
        );

        String givenRealName = "<REAL-NAME>";
        String givenNickname = "<NICKNAME>";
        String givenPhoneNumber = "010-1111-1111";

        user = User.create(
                account,
                givenRealName,
                givenNickname,
                givenPhoneNumber
        );
    }


    @Test
    @DisplayName("성공 - user 저장")
    void saveUser_Success() {
        // given
        given(userRepository.save(any(User.class)))
                .willReturn(user);

        // when
        userService.save(
                account,
                user.getRealName().getValue(),
                user.getNickname().getValue(),
                user.getPhoneNumber().getValue()
        );

        // then
        then(userRepository).should().save(any(User.class));
    }

    @Test
    @DisplayName("성공 - nickname 을 이용하여 user nickname 변경")
    void changeNickname_Success() {
        // given
        String newNicknameValue = "<NEW_NICKNAME>";

        given(userRepository.findByNickname(any(Nickname.class)))
                .willReturn(Optional.ofNullable(user));

        // when
        userService.changeNickname(newNicknameValue);

        // then
        assertThat(user.getNickname().getValue()).isEqualTo(newNicknameValue);
    }

    @Test
    @DisplayName("에러 - 없는 user nickname 으로 검색")
    void changeNickname_Error_NotFoundUserNickname() {
        // given
        String notExistNickname = "<NOT-EXIST-NICKNAME>";

        given(userRepository.findByNickname(new Nickname(notExistNickname)))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.changeNickname(notExistNickname))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("성공 - id를 이용하여 user nickname 변경")
    void changeNicknameByUserId_Success() {
        // given
        String newNicknameValue = "<NEW-NICKNAME>";

        ReflectionTestUtils.setField(user, "id", 1L);

        given(userRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(user));

        // when
        userService.changeNicknameByUserId(user.getId(), newNicknameValue);

        // then
        assertThat(user.getNickname().getValue()).isEqualTo(newNicknameValue);
    }

    @Test
    @DisplayName("에러 - 없는 유저 id로 검색")
    void changeNicknameByUserId_Error_NotFoundUser() {
        // given
        String newNicknameValue = "<NEW_NICKNAME>";

        given(userRepository.findById(-1L))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.changePhoneNumberByUserId(-1L, newNicknameValue))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("성공 - id를 이용하여 phone number 변경")
    void changePhoneNumberByUserId_Success() {
        String newPhoneNumberValue = "010-1111-1112";

        ReflectionTestUtils.setField(user, "id", 1L);

        given(userRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(user));

        // when
        userService.changePhoneNumberByUserId(user.getId(), newPhoneNumberValue);

        // then
        assertThat(user.getPhoneNumber().getValue()).isEqualTo(newPhoneNumberValue);
    }

    @Test
    @DisplayName("에러 - 없는 유저 id로 검색")
    void changePhoneNumberByUserId_Error_NotFoundUser() {
        // given
        String newPhoneNumberValue = "010-1111-1112";

        given(userRepository.findById(-1L))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.changePhoneNumberByUserId(-1L, newPhoneNumberValue))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.USER_NOT_FOUND.getMessage());
    }
}