package com.house.biet.user.command;


import com.house.biet.user.command.domain.entity.Account;
import com.house.biet.user.command.domain.vo.Email;
import com.house.biet.user.command.domain.vo.Password;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    @Test
    @DisplayName("에러 - 이메일 중복 저장 에러")
    void saveEmail_Error_EmailRedundantStorage() {
        // given
        String givenEmailValue = "abc@xyz.com";

        Email email1 = new Email(givenEmailValue);
        Email email2 = new Email(givenEmailValue);

        String givenPasswordValue1 = "MdxN@t6UnLNCRh!py1FjREm!CvC8KNVjhxu1";
        String givenPasswordValue2 = "MdxN@t6UnLNCRh!py1FjREm!CvC8KNVjhxu2";

        Password givenPassword1 = Password.encrypt(givenPasswordValue1, ENCODER);
        Password givenPassword2 = Password.encrypt(givenPasswordValue2, ENCODER);

        Account account1 = Account.signUp(email1, givenPassword1);
        Account account2 = Account.signUp(email2, givenPassword2);

        accountRepository.save(account1);

        // when & then
        assertThatThrownBy(() -> accountRepository.save(account2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }


    @Test
    @DisplayName("성공 - 이메일로 계정을 조회")
    void findByEmail__Success() {
        // given
        String givenEmailValue = "abc@xyz.com";
        Email givenEmail = new Email(givenEmailValue);

        String givenPasswordValue = "MdxN@t6UnLNCRh!py1FjREm!CvC8KNVjhxu";
        Password givenPassword = Password.encrypt(givenPasswordValue, ENCODER);

        Account account = Account.signUp(givenEmail, givenPassword);

        accountRepository.save(account);

        // when
        Optional<Account> result = accountRepository.findByEmail(givenEmailValue);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.get().getEmail()).isEqualTo(givenEmail);
    }


}