package com.house.biet.api;

import com.house.biet.auth.infrastructure.security.AuthPrincipal;
import com.house.biet.global.response.CustomApiResponse;
import com.house.biet.global.response.SuccessCode;
import com.house.biet.user.command.application.UserCommandFacade;
import com.house.biet.user.command.dto.*;
import com.house.biet.user.query.application.UserAddressQueryFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 사용자(User) 관련 API를 제공하는 컨트롤러.
 *
 * <p>
 * 인증된 사용자의 Account ID를 기반으로,
 * 사용자 정보 및 배송지(Address)에 대한 조회(Query)와 상태 변경(Command) 요청을 처리한다.
 * </p>
 *
 * <p>
 * <b>Command 요청</b>은 {@link UserCommandFacade}를 통해 처리되며,
 * <b>Query 요청</b>은 {@link UserAddressQueryFacade}를 통해 처리된다.
 * </p>
 *
 * <p>
 * Controller는 요청/응답 변환 및 인증 정보 전달 역할만 수행하며,
 * 실제 비즈니스 로직은 각 Facade에 위임한다.
 * </p>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserCommandFacade userCommandFacade;
    private final UserAddressQueryFacade userAddressQueryFacade;

    /**
     * 사용자 닉네임 변경 API
     *
     * @param principal 인증된 사용자 정보
     * @param requestDto 변경할 닉네임 정보
     * @return 성공 응답
     */
    @PatchMapping("/nickname")
    public ResponseEntity<CustomApiResponse<Void>> changeNickname(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestBody @Valid ChangeNicknameRequestDto requestDto
    ) {
        userCommandFacade.changeNickname(
                principal.accountId(),
                requestDto.nickname()
        );

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.USER_NICKNAME_CHANGE_SUCCESS)
        );
    }

    /**
     * 사용자 전화번호 변경 API
     *
     * @param principal 인증된 사용자 정보
     * @param requestDto 변경할 전화번호 정보
     * @return 성공 응답
     */
    @PatchMapping("/phoneNumber")
    public ResponseEntity<CustomApiResponse<Void>> changePhoneNumber(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestBody @Valid ChangePhoneNumberRequestDto requestDto
    ) {
        userCommandFacade.changePhoneNumber(
                principal.accountId(),
                requestDto.phoneNumber()
        );

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.USER_PHONE_NUMBER_CHANGE_SUCCESS)
        );
    }

    /**
     * 사용자 전체 배송지 조회 API (Query)
     *
     * <p>
     * 인증된 사용자의 Account ID를 기반으로,
     * 해당 사용자의 모든 배송지 목록을 조회한다.
     * </p>
     *
     * <p>
     * 조회 결과는 {@code AddressResponseDto} 형태로 반환되며,
     * 도메인 엔티티는 외부로 노출되지 않는다.
     * </p>
     *
     * @param principal 인증된 사용자 정보
     * @return 배송지 목록
     */
    @GetMapping("/address")
    public ResponseEntity<CustomApiResponse<List<AddressResponseDto>>> getAllAddresses(
            @AuthenticationPrincipal AuthPrincipal principal
    ) {
        List<AddressResponseDto> addresses = userAddressQueryFacade.getAllAddresses(
                principal.accountId()
        );

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.USER_ADDRESS_LIST_SUCCESS, addresses)
        );
    }

    /**
     * 사용자 전체 배송지 조회 API (Query)
     *
     * <p>
     * 인증된 사용자의 Account ID를 기반으로,
     * 해당 사용자의 모든 배송지 목록을 조회한다.
     * </p>
     *
     * <p>
     * 조회 결과는 {@code AddressResponseDto} 형태로 반환되며,
     * 도메인 엔티티는 외부로 노출되지 않는다.
     * </p>
     *
     * @param principal 인증된 사용자 정보
     * @return 배송지 목록
     */
    @PostMapping("/address")
    public ResponseEntity<CustomApiResponse<Void>> addAddress(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestBody @Valid AddAddressRequestDto requestDto
    ) {
        userCommandFacade.addAddress(
                principal.accountId(),
                requestDto.roadAddress(),
                requestDto.jibunAddress(),
                requestDto.detailAddress(),
                requestDto.alias(),
                requestDto.isDefault()
        );

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.USER_ADDRESS_ADD_SUCCESS)
        );
    }

    /**
     * 사용자 배송지 삭제 API (Command)
     *
     * <p>
     * 지정된 주소 별칭(addressAlias)에 해당하는 배송지를 삭제한다.
     * </p>
     *
     * <p>
     * 삭제 대상이 기본 배송지인 경우,
     * 남아있는 배송지 중 하나가 자동으로 기본 배송지로 설정된다.
     * </p>
     *
     * @param principal 인증된 사용자 정보
     * @param requestDto 삭제할 배송지 정보
     * @return 성공 응답
     */
    @DeleteMapping("/address")
    public ResponseEntity<CustomApiResponse<Void>> removeAddress(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestBody @Valid RemoveAddressRequestDto requestDto
    ) {
        userCommandFacade.removeAddress(
                principal.accountId(),
                requestDto.addressAlias()
        );

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.USER_ADDRESS_REMOVE_SUCCESS)
        );
    }

    /**
     * 기본 배송지 변경 API (Command)
     *
     * <p>
     * 지정된 주소 별칭(addressAlias)을 기본 배송지로 설정한다.
     * </p>
     *
     * <p>
     * 기존 기본 배송지는 자동으로 해제된다.
     * </p>
     *
     * @param principal 인증된 사용자 정보
     * @param requestDto 기본 배송지 변경 정보
     * @return 성공 응답
     */
    @PatchMapping("/address/default")
    public ResponseEntity<CustomApiResponse<Void>> changeDefaultAddress(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestBody @Valid ChangeDefaultAddressRequestDto requestDto
    ) {
        userCommandFacade.changeDefaultAddress(
                principal.accountId(),
                requestDto.addressAlias()
        );

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.USER_DEFAULT_ADDRESS_CHANGE_SUCCESS)
        );
    }

    /**
     * 배송지 별칭 변경 API (Command)
     *
     * <p>
     * 기존 배송지의 별칭(addressAlias)을 새로운 별칭(newAddressAlias)으로 변경한다.
     * </p>
     *
     * <p>
     * 별칭은 사용자 내에서 고유해야 하며,
     * 중복될 경우 예외가 발생한다.
     * </p>
     *
     * @param principal 인증된 사용자 정보
     * @param requestDto 별칭 변경 정보
     * @return 성공 응답
     */
    @PatchMapping("/address/alias")
    public ResponseEntity<CustomApiResponse<Void>> changeAddressAlias(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestBody @Valid ChangeAddressAliasRequestDto requestDto
    ) {
        userCommandFacade.changeAddressAlias(
                principal.accountId(),
                requestDto.addressAlias(),
                requestDto.newAddressAlias()
        );

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.USER_ADDRESS_ALIAS_CHANGE_SUCCESS)
        );
    }
}