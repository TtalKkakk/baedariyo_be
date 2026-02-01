package com.house.biet.member.command;


import com.house.biet.global.vo.UserRole;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.member.command.domain.vo.Email;
import com.house.biet.member.command.domain.vo.Password;
import com.house.biet.member.command.infrastructure.AccountRepositoryJpaAdapter;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(AccountRepositoryJpaAdapter.class)
@ActiveProfiles("test")
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    String givenEmailValue = "abc@xyz.com";
    String givenPasswordValue = "MdxN@t6UnLNCRh!py1FjREm!CvC8KNVjhxu";

    Email givenEmail;
    Password givenPassword;
    Account userAccount;

    @BeforeEach
    void setup() {
        givenEmail = new Email(givenEmailValue);
        givenPassword = Password.encrypt(givenPasswordValue, ENCODER);

        userAccount = Account.signUp(givenEmail, givenPassword, UserRole.USER);
    }

    @Test
    @DisplayName("성공 - 저장된 모든 계정 가져오기")
    void findAllEmail_Success() {
        // given
        accountRepository.save(userAccount);

        // when
        List<Account> accounts = accountRepository.findAll();

        // then
        assertThat(accounts).hasSize(1);
        assertThat(accounts.get(0).getEmail()).isEqualTo(givenEmail);
        assertThat(accounts.get(0).getRole()).isEqualTo(UserRole.USER);

        assertThat(accounts)
                .extracting(Account::getEmail)
                .containsExactly(givenEmail);
    }

    @Test
    @DisplayName("성공 - 동일한 이메일과 다른 Role로 회원가입")
    void saveEmail_Success_SameEmailLogin() {
        // given
        Account riderAccount = Account.signUp(givenEmail, givenPassword, UserRole.RIDER);

        // when
        accountRepository.save(userAccount);
        accountRepository.save(riderAccount);

        List<Account> accounts = accountRepository.findAll();

        // then
        assertThat(accounts).hasSize(2);
        assertThat(accounts)
                .extracting(Account::getRole)
                .containsExactlyInAnyOrder(UserRole.USER, UserRole.RIDER);
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

        Account userAccount1 = Account.signUp(email1, givenPassword1, UserRole.USER);
        Account userAccount2 = Account.signUp(email2, givenPassword2, UserRole.USER);

        accountRepository.save(userAccount1);

        // when & then
        assertThatThrownBy(() -> accountRepository.save(userAccount2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }


    @Test
    @DisplayName("성공 - 이메일로 계정을 조회")
    void findByEmail__AndRole__Success() {
        // given
        accountRepository.save(userAccount);

        // when
        Optional<Account> result = accountRepository.findByEmailAndRole(givenEmail, UserRole.USER);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.get().getEmail()).isEqualTo(givenEmail);
    }

    @Test
    @DisplayName("성공 - 이메일 계정이 저장되었는지 확인")
    void existsByEmail__Role__Success() {
        // given
        accountRepository.save(userAccount);

        // when
        boolean isSaved = accountRepository.existsByEmailAndRole(givenEmail, UserRole.USER);

        // then
        assertThat(isSaved).isTrue();
    }
}