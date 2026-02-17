package com.house.biet.user.query;

import com.house.biet.fixtures.AccountFixtures;
import com.house.biet.fixtures.UserFixtures;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.common.domain.enums.UserRole;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.user.command.UserRepository;
import com.house.biet.user.command.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserQueryServiceTest {

    @InjectMocks
    private UserQueryServiceImpl userQueryService;

    @Mock
    private UserRepository userRepository;

    Account account;
    User user;

    @BeforeEach
    void setup() {
        account = AccountFixtures.account(UserRole.USER);
        user = UserFixtures.user(account);
    }

    @Test
    @DisplayName("성공 - accountId로 userId 조회")
    void getUserIdByAccountId_Success() {
        // given
        Long accountId = 1L;
        Long userId = 10L;

        when(userRepository.findUserIdByAccountId(accountId))
                .thenReturn(Optional.of(userId));

        // when
        Long result = userQueryService.getUserIdByAccountId(accountId);

        // then
        assertThat(result).isEqualTo(userId);
        verify(userRepository).findUserIdByAccountId(accountId);
    }

    @Test
    @DisplayName("실패 - accountId에 해당하는 User 없음")
    void getUserIdByAccountId_Fail() {
        // given
        Long accountId = 99L;

        when(userRepository.findUserIdByAccountId(accountId))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userQueryService.getUserIdByAccountId(accountId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.USER_NOT_FOUND.getMessage());
    }
}