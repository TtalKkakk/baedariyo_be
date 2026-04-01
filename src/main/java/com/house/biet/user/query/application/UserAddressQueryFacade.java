package com.house.biet.user.query.application;

import com.house.biet.user.command.application.UserService;
import com.house.biet.user.command.dto.AddressResponseDto;
import com.house.biet.user.query.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 사용자(User) 배송지 조회 기능을 제공하는 Facade.
 *
 * <p>
 * 외부 계층(Controller)에서 Account ID를 기반으로 요청을 받아,
 * User ID로 변환 후 {@link UserService}를 통해 실제 데이터를 조회한다.
 * 조회된 도메인 객체(UserAddress)는 {@link AddressResponseDto}로 변환하여 반환한다.
 * </p>
 *
 * <p>
 * 주된 책임:
 * <ul>
 *     <li>Account ID → User ID 변환</li>
 *     <li>UserService 호출 및 도메인 객체 조회</li>
 *     <li>DTO 변환</li>
 * </ul>
 * </p>
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAddressQueryFacade {

    private final UserQueryService userQueryService;
    private final UserService userService;

    /**
     * 사용자의 모든 배송지를 조회한다.
     *
     * @param accountId 조회 대상 계정 ID
     * @return 사용자 배송지 리스트 (DTO)
     */
    public List<AddressResponseDto> getAllAddresses(Long accountId) {
        Long userId = userQueryService.getUserIdByAccountId(accountId);

        return userService.getAllAddresses(userId)
                .stream()
                .map(AddressResponseDto::from)
                .toList();
    }
}