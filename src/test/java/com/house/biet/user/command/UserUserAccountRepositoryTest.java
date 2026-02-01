package com.house.biet.user.command;


import com.house.biet.user.command.domain.entity.UserAccount;
import com.house.biet.member.domain.vo.Email;
import com.house.biet.member.domain.vo.Password;
import com.house.biet.user.command.infrastructure.UserUserAccountRepositoryJpaAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(UserUserAccountRepositoryJpaAdapter.class)
@ActiveProfiles("test")
class UserUserAccountRepositoryTest {

    @Autowired
    private UserAccountRepository userAccountRepository;

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    String givenEmailValue = "abc@xyz.com";
    String givenPasswordValue = "MdxN@t6UnLNCRh!py1FjREm!CvC8KNVjhxu";

    Email givenEmail;
    Password givenPassword;
    UserAccount userAccount;

    @BeforeEach
    void setup() {
        givenEmail = new Email(givenEmailValue);
        givenPassword = Password.encrypt(givenPasswordValue, ENCODER);

        userAccount = UserAccount.signUp(givenEmail, givenPassword);
    }

    @Test
    @DisplayName("에러 - 이메일 중복 저장 에러")
    void saveEmail_Error_EmailRedundantStorage() {
        // given

        Email email1 = new Email(givenEmailValue);
        Email email2 = new Email(givenEmailValue);

        String givenPasswordValue1 = "MdxN@t6UnLNCRh!py1FjREm!CvC8KNVjhxu1";
        String givenPasswordValue2 = "MdxN@t6UnLNCRh!py1FjREm!CvC8KNVjhxu2";

        Password givenPassword1 = Password.encrypt(givenPasswordValue1, ENCODER);
        Password givenPassword2 = Password.encrypt(givenPasswordValue2, ENCODER);

        UserAccount userAccount1 = UserAccount.signUp(email1, givenPassword1);
        UserAccount userAccount2 = UserAccount.signUp(email2, givenPassword2);

        userAccountRepository.save(userAccount1);

        // when & then
        assertThatThrownBy(() -> userAccountRepository.save(userAccount2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }


    @Test
    @DisplayName("성공 - 이메일로 계정을 조회")
    void findByEmail__Success() {
        // given
        userAccountRepository.save(userAccount);

        // when
        Optional<UserAccount> result = userAccountRepository.findByEmail(givenEmail);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.get().getEmail()).isEqualTo(givenEmail);
    }

    @Test
    @DisplayName("성공 - 이메일 계정이 저장되었는지 확인")
    void existsByEmail__Success() {
        // given
        userAccountRepository.save(userAccount);

        // when
        boolean isSaved = userAccountRepository.existsByEmail(givenEmail);

        // then
        assertThat(isSaved).isTrue();
    }
}