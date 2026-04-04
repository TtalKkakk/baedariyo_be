package com.house.biet.api;

import com.house.biet.auth.infrastructure.security.AuthPrincipal;
import com.house.biet.global.response.CustomApiResponse;
import com.house.biet.global.response.SuccessCode;
import com.house.biet.user.command.application.PaymentMethodService;
import com.house.biet.user.command.application.UserCommandFacade;
import com.house.biet.user.command.dto.*;
import com.house.biet.user.query.application.UserAddressQueryFacade;
import com.house.biet.user.query.application.UserQueryService;
import com.house.biet.user.query.application.dto.UserProfileResponseDto;
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
    private final UserQueryService userQueryService;
    private final PaymentMethodService paymentMethodService;

    /**
     * 사용자 전체 정보 API
     *
     */

    @GetMapping("/me")
    public ResponseEntity<CustomApiResponse<UserProfileResponseDto>> getMyNickname(
            @AuthenticationPrincipal AuthPrincipal principal
    ) {
        Long accountId = principal.accountId();

        UserProfileResponseDto responseDto = userQueryService.getUserProfile(accountId);

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.USER_GET_SUCCESS, responseDto)
        );
    }

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
     * @param principal 인증된 사용자 정보
     * @param requestDto 요청 정보
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

    /**
     * 사용자 결제수단 목록 조회 API
     *
     * @param principal 인증된 사용자 정보
     * @return 결제수단 목록
     */
    @GetMapping("/payment-methods")
    public ResponseEntity<CustomApiResponse<List<PaymentMethodResponseDto>>> getPaymentMethods(
            @AuthenticationPrincipal AuthPrincipal principal
    ) {
        Long accountId = principal.accountId();

        List<PaymentMethodResponseDto> responseDto = paymentMethodService.getPaymentMethods(accountId);

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.USER_PAYMENT_METHOD_LIST_SUCCESS, responseDto)
        );
    }

    /**
     * 카드 등록 시작 API
     *
     * @param principal 인증된 사용자 정보
     * @return UUID 토큰
     */
    @PostMapping("/payment-methods/start")
    public ResponseEntity<CustomApiResponse<StartRegistrationResponseDto>> startRegistration(
            @AuthenticationPrincipal AuthPrincipal principal
    ) {
        Long accountId = principal.accountId();

        StartRegistrationResponseDto responseDto = paymentMethodService.startRegistration(accountId);

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.USER_PAYMENT_METHOD_START_SUCCESS, responseDto)
        );
    }

    /**
     * 결제수단 등록 API
     *
     * @param principal  인증된 사용자 정보
     * @param requestDto 등록할 결제수단 정보
     * @return 등록된 결제수단 정보
     */
    @PostMapping("/payment-methods")
    public ResponseEntity<CustomApiResponse<PaymentMethodResponseDto>> addPaymentMethod(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestBody AddPaymentMethodRequestDto requestDto
    ) {
        Long accountId = principal.accountId();

        PaymentMethodResponseDto responseDto = paymentMethodService.addPaymentMethod(
                accountId,
                requestDto.billingKey(),
                requestDto.cardNumber()
        );

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.USER_PAYMENT_METHOD_ADD_SUCCESS, responseDto)
        );
    }

    /**
     * 결제수단 삭제 API
     *
     * @param principal       인증된 사용자 정보
     * @param paymentMethodId 삭제할 결제수단 식별자
     * @return 성공 응답
     */
    @DeleteMapping("/payment-methods/{paymentMethodId}")
    public ResponseEntity<CustomApiResponse<Void>> deletePaymentMethod(
            @AuthenticationPrincipal AuthPrincipal principal,
            @PathVariable Long paymentMethodId
    ) {
        Long accountId = principal.accountId();

        paymentMethodService.deletePaymentMethod(accountId, paymentMethodId);

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.USER_PAYMENT_METHOD_DELETE_SUCCESS)
        );
    }
}