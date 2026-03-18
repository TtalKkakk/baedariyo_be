package com.house.biet.api;

import com.house.biet.auth.infrastructure.security.AuthPrincipal;
import com.house.biet.global.response.CustomApiResponse;
import com.house.biet.global.response.SuccessCode;
import com.house.biet.user.command.application.UserCommandFacade;
import com.house.biet.user.command.dto.ChangeNicknameRequestDto;
import com.house.biet.user.command.dto.ChangePhoneNumberRequestDto;
import com.house.biet.user.command.dto.AddAddressRequestDto;
import com.house.biet.user.command.dto.ChangeDefaultAddressRequestDto;
import com.house.biet.user.command.dto.RemoveAddressRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 사용자(User) 관련 Command API를 제공하는 컨트롤러.
 *
 * <p>
 * 인증된 사용자의 Account ID를 기반으로,
 * 사용자 정보 및 배송지(Address) 관련 상태 변경 요청을 처리한다.
 * </p>
 *
 * <p>
 * 모든 요청은 {@link UserCommandFacade}를 통해 처리되며,
 * Controller는 요청/응답 변환 역할만 담당한다.
 * </p>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserCommandFacade userCommandFacade;

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
     * 사용자 배송지 추가 API
     *
     * <p>
     * 새로운 배송지를 추가하며, 기본 배송지 여부를 함께 설정할 수 있다.
     * </p>
     *
     * @param principal 인증된 사용자 정보
     * @param requestDto 배송지 정보
     * @return 성공 응답
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
     * 기본 배송지 변경 API
     *
     * <p>
     * 지정된 addressId를 기본 배송지로 설정한다.
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
                requestDto.addressId()
        );

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.USER_DEFAULT_ADDRESS_CHANGE_SUCCESS)
        );
    }

    /**
     * 배송지 삭제 API
     *
     * <p>
     * 지정된 addressId에 해당하는 배송지를 삭제한다.
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
                requestDto.addressId()
        );

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.USER_ADDRESS_REMOVE_SUCCESS)
        );
    }
}