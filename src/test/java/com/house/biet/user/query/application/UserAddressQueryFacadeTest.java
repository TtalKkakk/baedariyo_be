package com.house.biet.user.query.application;

import com.house.biet.common.domain.vo.Address;
import com.house.biet.store.command.domain.vo.GeoLocation;
import com.house.biet.user.command.application.UserService;
import com.house.biet.user.command.domain.entity.UserAddress;
import com.house.biet.user.command.domain.vo.AddressAlias;
import com.house.biet.user.command.dto.AddressResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserAddressQueryFacadeTest {

    @Mock
    private UserQueryService userQueryService;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserAddressQueryFacade facade;

    @Test
    @DisplayName("성공 - 사용자 배송지 조회")
    void getAllAddresses_Success() {
        // given
        Long accountId = 1L;
        Long userId = 100L;

        UserAddress address = UserAddress.create(
                null,
                new Address("도로명", "지번", "상세"),
                new GeoLocation(37.0, 127.0),
                new AddressAlias("집"),
                true
        );

        given(userQueryService.getUserIdByAccountId(accountId)).willReturn(userId);
        given(userService.getAllAddresses(userId)).willReturn(List.of(address));

        // when
        List<AddressResponseDto> result = facade.getAllAddresses(accountId);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).alias()).isEqualTo("집");
        assertThat(result.get(0).roadAddress()).isEqualTo("도로명");
        assertThat(result.get(0).jibunAddress()).isEqualTo("지번");
        assertThat(result.get(0).detailAddress()).isEqualTo("상세");
    }
}