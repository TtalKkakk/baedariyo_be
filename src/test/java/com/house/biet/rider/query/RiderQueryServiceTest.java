package com.house.biet.rider.query;

import com.house.biet.fixtures.AccountFixtures;
import com.house.biet.fixtures.RiderFixtures;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.global.vo.UserRole;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.rider.command.RiderRepository;
import com.house.biet.rider.command.domain.entity.Rider;
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
import static org.awaitility.Awaitility.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RiderQueryServiceTest {

    @InjectMocks
    private RiderQueryServiceImpl riderQueryService;

    @Mock
    private RiderRepository riderRepository;

    @Test
    @DisplayName("성공 - Nickname 으로 riderId 찾기")
    void getRiderIdByNickname_Success() {
        // given
        Long riderId = 1L;
        String riderNicknameValue = "<RIDER_NICKNAME>";

        when(riderRepository.findRiderIdByNickname(riderNicknameValue))
                .thenReturn(Optional.of(riderId));

        // when
        Long getRiderId = riderQueryService.getRiderIdByNickname(riderNicknameValue);

        // then
        assertThat(getRiderId).isEqualTo(riderId);
    }

    @Test
    @DisplayName("에러 - 없는 Nickname 찾기")
    void getRiderIdByNickname_Error_NotFoundRiderNickname() {
        // given
        String notExistsRiderNickname = "<NOT_EXISTS_RIDER_NICKNAME>";

        when(riderRepository.findRiderIdByNickname(notExistsRiderNickname))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> riderQueryService.getRiderIdByNickname(notExistsRiderNickname))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.RIDER_NOT_FOUND.getMessage());
    }
}