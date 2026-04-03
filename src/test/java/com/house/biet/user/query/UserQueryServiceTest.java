package com.house.biet.user.query;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.user.query.UserQueryRepository;
import com.house.biet.user.query.application.UserQueryServiceImpl;
import com.house.biet.user.query.application.dto.UserProfileResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserQueryServiceTest {

    @InjectMocks
    private UserQueryServiceImpl userQueryService;

    @Mock
    private UserQueryRepository userQueryRepository;

    @Nested
    @DisplayName("getUserIdByAccountId")
    class GetUserIdByAccountId {

        @Test
        @DisplayName("성공 - accountId로 userId 조회")
        void getUserIdByAccountId_Success() {
            // given
            Long accountId = 1L;
            Long userId = 10L;

            given(userQueryRepository.findUserIdByAccountId(accountId))
                    .willReturn(userId);

            // when
            Long result = userQueryService.getUserIdByAccountId(accountId);

            // then
            assertThat(result).isEqualTo(userId);
            verify(userQueryRepository).findUserIdByAccountId(accountId);
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 accountId로 조회 시 USER_NOT_FOUND 예외")
        void getUserIdByAccountId_Error_NotFound() {
            // given
            Long accountId = 99L;

            given(userQueryRepository.findUserIdByAccountId(accountId))
                    .willReturn(null);

            // when & then
            assertThatThrownBy(() ->
                    userQueryService.getUserIdByAccountId(accountId)
            ).isInstanceOf(CustomException.class)
                    .hasMessage(ErrorCode.USER_NOT_FOUND.getMessage());
        }
    }

    @Nested
    @DisplayName("getUserProfile")
    class GetUserProfile {

        @Test
        @DisplayName("성공 - accountId로 유저 프로필 조회")
        void getUserProfile_Success() {
            // given
            Long accountId = 1L;

            UserProfileResponseDto response =
                    new UserProfileResponseDto(
                            "닉네임",
                            "010-0000-0000",
                            "test@test.com"
                    );

            given(userQueryRepository.getUserProfile(accountId))
                    .willReturn(response);

            // when
            UserProfileResponseDto result =
                    userQueryService.getUserProfile(accountId);

            // then
            assertThat(result).isNotNull();
            assertThat(result.nickname()).isEqualTo("닉네임");
            assertThat(result.phoneNumber()).isEqualTo("010-0000-0000");
            assertThat(result.email()).isEqualTo("test@test.com");

            verify(userQueryRepository).getUserProfile(accountId);
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 accountId로 조회 시 null 반환")
        void getUserProfile_Error_NotFound() {
            // given
            Long accountId = 99L;

            given(userQueryRepository.getUserProfile(accountId))
                    .willReturn(null);

            // when
            UserProfileResponseDto result =
                    userQueryService.getUserProfile(accountId);

            // then
            assertThat(result).isNull();
            verify(userQueryRepository).getUserProfile(accountId);
        }
    }
}