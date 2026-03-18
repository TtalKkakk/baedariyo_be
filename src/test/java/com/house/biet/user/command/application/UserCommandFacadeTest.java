package com.house.biet.user.command.application;

import com.house.biet.user.query.UserQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserCommandFacadeTest {

    @Mock
    private UserService userService;

    @Mock
    private UserQueryService userQueryService;

    @InjectMocks
    private UserCommandFacade facade;

    @Test
    @DisplayName("성공 - 닉네임 변경")
    void changeNickname_Success() {
        // given
        Long accountId = 1L;
        Long userId = 100L;
        String nickname = "newNickname";

        given(userQueryService.getUserIdByAccountId(accountId))
                .willReturn(userId);

        // when
        facade.changeNickname(accountId, nickname);

        // then
        verify(userQueryService).getUserIdByAccountId(accountId);
        verify(userService).changeNicknameByUserId(userId, nickname);
    }

    @Test
    @DisplayName("성공 - 전화번호 변경")
    void changePhoneNumber_Success() {
        // given
        Long accountId = 1L;
        Long userId = 100L;
        String phoneNumber = "010-1234-5678";

        given(userQueryService.getUserIdByAccountId(accountId))
                .willReturn(userId);

        // when
        facade.changePhoneNumber(accountId, phoneNumber);

        // then
        verify(userService).changePhoneNumberByUserId(userId, phoneNumber);
    }

    @Test
    @DisplayName("성공 - 주소 추가")
    void addAddress_Success() {
        // given
        Long accountId = 1L;
        Long userId = 100L;

        given(userQueryService.getUserIdByAccountId(accountId))
                .willReturn(userId);

        // when
        facade.addAddress(
                accountId,
                "도로명",
                "지번",
                "상세",
                "집",
                true
        );

        // then
        verify(userService).addAddress(
                userId,
                "도로명",
                "지번",
                "상세",
                "집",
                true
        );
    }

    @Test
    @DisplayName("성공 - 기본 배송지 변경")
    void changeDefaultAddress_Success() {
        // given
        Long accountId = 1L;
        Long userId = 100L;
        String addressAlias = "회사";

        given(userQueryService.getUserIdByAccountId(accountId))
                .willReturn(userId);

        // when
        facade.changeDefaultAddress(accountId, addressAlias);

        // then
        verify(userService).changeDefaultAddress(userId, addressAlias);
    }

    @Test
    @DisplayName("성공 - 주소 삭제")
    void removeAddress_Success() {
        // given
        Long accountId = 1L;
        Long userId = 100L;
        String addressAlias = "집";

        given(userQueryService.getUserIdByAccountId(accountId))
                .willReturn(userId);

        // when
        facade.removeAddress(accountId, addressAlias);

        // then
        verify(userService).removeAddress(userId, addressAlias);
    }

    @Test
    @DisplayName("성공 - 배송지 별칭 변경")
    void changeAddressAlias_Success() {
        // given
        Long accountId = 1L;
        Long userId = 100L;
        String addressAlias = "집";
        String newAddressAlias = "우리집";

        given(userQueryService.getUserIdByAccountId(accountId))
                .willReturn(userId);

        // when
        facade.changeAddressAlias(accountId, addressAlias, newAddressAlias);

        // then
        verify(userQueryService).getUserIdByAccountId(accountId);
        verify(userService).changeAddressAlias(userId, addressAlias, newAddressAlias);
    }
}