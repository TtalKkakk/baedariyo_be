package com.house.biet.user.command;

import com.house.biet.common.domain.enums.UserRole;
import com.house.biet.member.command.AccountRepository;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.member.command.domain.vo.Email;
import com.house.biet.member.command.domain.vo.Nickname;
import com.house.biet.member.command.domain.vo.Password;
import com.house.biet.member.command.infrastructure.AccountRepositoryJpaAdapter;
import com.house.biet.user.command.domain.entity.User;
import com.house.biet.user.command.infrastructure.UserRepositoryJpaAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({
        UserRepositoryJpaAdapter.class,
        AccountRepositoryJpaAdapter.class
})
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    String givenEmail = "abc@xyz.com";
    String givenPassword = UUID.randomUUID().toString().substring(1, 30);
    String givenRealName = "<REAL_NAME>";
    String givenNickname = "<NICKNAME>";
    String givenPhoneNumber = "010-1111-1111";

    User user;
    Account savedAccount;

    @BeforeEach
    void setup() {
        savedAccount = accountRepository.save(Account.signup(
                new Email(givenEmail),
                Password.encrypt(givenPassword, ENCODER),
                UserRole.USER
        ));

        user = User.create(savedAccount, givenRealName, givenNickname, givenPhoneNumber);

    }

    @Test
    @DisplayName("성공 - User 데이터 저장 성공")
    void saveUser_Success() {
        // when
        User savedUser = userRepository.save(user);

        // then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getRealName().getValue()).isEqualTo(givenRealName);
        assertThat(savedUser.getNickname().getValue()).isEqualTo(givenNickname);
        assertThat(savedUser.getPhoneNumber().getValue()).isEqualTo(givenPhoneNumber);
    }

    @Test
    @DisplayName("성공 - UserId로 유저 찾기")
    void findByUserId_Success() {
        // given
        User savedUser = userRepository.save(user);

        // when
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        // then
        assertThat(foundUser).isNotEmpty();
        assertThat(foundUser.get().getRealName().getValue()).isEqualTo(givenRealName);
        assertThat(foundUser.get().getNickname().getValue()).isEqualTo(givenNickname);
        assertThat(foundUser.get().getPhoneNumber().getValue()).isEqualTo(givenPhoneNumber);
    }

    @Test
    @DisplayName("성공 - 없는 Id로 찾을 경우 비어있는지 확인")
    void findByNotExistUserId_Success() {
        // given
        userRepository.save(user);
        Long notExistId = -1L;

        // when
        Optional<User> foundUser = userRepository.findById(notExistId);

        // then
        assertThat(foundUser).isEmpty();
    }

    @Test
    @DisplayName("성공 - Nickname 으로 유저 찾기")
    void findByNickname_Success() {
        // given
        User savedUser = userRepository.save(user);

        // when
        Optional<User> foundUser = userRepository.findByNickname(savedUser.getNickname());

        // then
        assertThat(foundUser).isNotEmpty();
        assertThat(foundUser.get().getRealName().getValue()).isEqualTo(givenRealName);
        assertThat(foundUser.get().getNickname().getValue()).isEqualTo(givenNickname);
        assertThat(foundUser.get().getPhoneNumber().getValue()).isEqualTo(givenPhoneNumber);
    }

    @Test
    @DisplayName("성공 - 없는 닉네임으로 찾을 경우 비어있는지 확인")
    void findByNotExistNickname_Success() {
        // given
        userRepository.save(user);
        Nickname notExistNickname = new Nickname(UUID.randomUUID().toString().substring(0, 10));

        // when
        Optional<User> foundUser = userRepository.findByNickname(notExistNickname);

        // then
        assertThat(foundUser).isEmpty();
    }
    @Test
    @DisplayName("성공 - accountId로 userId 조회")
    void findUserIdByAccountId_success() {
        // given
        userRepository.save(user);
        Long accountId = savedAccount.getId();

        // when
        Optional<Long> result = userRepository.findUserIdByAccountId(accountId);

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("실패 - 존재하지 않는 accountId로 조회 시 empty 반환")
    void findUserIdByAccountId_notFound() {
        // given
        userRepository.save(user);
        Long invalidAccountId = 9999L;

        // when
        Optional<Long> result = userRepository.findUserIdByAccountId(invalidAccountId);

        // then
        assertThat(result).isEmpty();
    }
}