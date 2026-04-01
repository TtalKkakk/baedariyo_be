package com.house.biet.api;

import com.house.biet.auth.infrastructure.security.AuthPrincipal;
import com.house.biet.global.response.CustomApiResponse;
import com.house.biet.global.response.SuccessCode;
import com.house.biet.rider.command.application.RiderCommandFacade;
import com.house.biet.rider.command.dto.ChangeNicknameRequestDto;
import com.house.biet.rider.command.dto.ChangePhoneNumberRequestDto;
import com.house.biet.rider.command.dto.ChangeVehicleTypeRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 라이더(Rider) 관련 API를 제공하는 컨트롤러.
 *
 * <p>
 * 인증된 사용자의 Account ID를 기반으로,
 * 라이더 정보 변경 및 상태 전이 요청을 처리한다.
 * </p>
 *
 * <p>
 * 상태 변경은 단순 값 변경이 아닌 "도메인 행위" 기반으로 수행된다.
 * </p>
 *
 * <p>
 * 모든 요청은 {@link RiderCommandFacade}를 통해 처리되며,
 * Controller는 요청/응답 변환 및 인증 정보 전달 역할만 수행한다.
 * </p>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rider")
public class RiderController {

    private final RiderCommandFacade riderCommandFacade;

    /**
     * 라이더 닉네임 변경 API
     *
     * @param principal 인증 사용자 정보
     * @param requestDto 요청 정보
     * @return changeNickname 결과
     */
    @PatchMapping("/nickname")
    public ResponseEntity<CustomApiResponse<Void>> changeNickname(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestBody @Valid ChangeNicknameRequestDto requestDto
    ) {
        riderCommandFacade.changeNicknameByRiderId(
                principal.accountId(),
                requestDto.nickname()
        );

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.RIDER_NICKNAME_CHANGE_SUCCESS)
        );
    }

    /**
     * 라이더 전화번호 변경 API
     *
     * @param principal 인증 사용자 정보
     * @param requestDto 요청 정보
     * @return changePhoneNumber 결과
     */
    @PatchMapping("/phoneNumber")
    public ResponseEntity<CustomApiResponse<Void>> changePhoneNumber(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestBody @Valid ChangePhoneNumberRequestDto requestDto
    ) {
        riderCommandFacade.changePhoneNumberByRiderId(
                principal.accountId(),
                requestDto.phoneNumber()
        );

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.RIDER_PHONE_NUMBER_CHANGE_SUCCESS)
        );
    }

    /**
     * 라이더 차량 타입 변경 API
     *
     * @param principal 인증 사용자 정보
     * @param requestDto 요청 정보
     * @return changeVehicleType 결과
     */
    @PatchMapping("/vehicle")
    public ResponseEntity<CustomApiResponse<Void>> changeVehicleType(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestBody @Valid ChangeVehicleTypeRequestDto requestDto
    ) {
        riderCommandFacade.changeVehicleType(
                principal.accountId(),
                requestDto.vehicleType()
        );

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.RIDER_VEHICLE_TYPE_CHANGE_SUCCESS)
        );
    }

    /**
     * 라이더를 ONLINE 상태로 전환
     *
     * @param principal 인증 사용자 정보
     * @return goOnline 결과
     */
    @PatchMapping("/online")
    public ResponseEntity<CustomApiResponse<Void>> goOnline(
            @AuthenticationPrincipal AuthPrincipal principal
    ) {
        riderCommandFacade.goOnline(principal.accountId());

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.RIDER_ONLINE_CHANGE_SUCCESS)
        );
    }

    /**
     * 배달 시작 (WORKING 상태 전환)
     *
     * @param principal 인증 사용자 정보
     * @return startDelivery 결과
     */
    @PatchMapping("/deliveries/start")
    public ResponseEntity<CustomApiResponse<Void>> startDelivery(
            @AuthenticationPrincipal AuthPrincipal principal
    ) {
        riderCommandFacade.startDelivery(principal.accountId());

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.RIDER_START_DELIVERY_SUCCESS)
        );
    }

    /**
     * 배달 완료 (ONLINE 상태 전환)
     *
     * @param principal 인증 사용자 정보
     * @return completeDelivery 결과
     */
    @PatchMapping("/deliveries/complete")
    public ResponseEntity<CustomApiResponse<Void>> completeDelivery(
            @AuthenticationPrincipal AuthPrincipal principal
    ) {
        riderCommandFacade.completeDelivery(principal.accountId());

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.RIDER_COMPLETE_DELIVERY_SUCCESS)
        );
    }

    /**
     * 라이더를 OFFLINE 상태로 전환
     *
     * @param principal 인증 사용자 정보
     * @return goOffline 결과
     */
    @PatchMapping("/offline")
    public ResponseEntity<CustomApiResponse<Void>> goOffline(
            @AuthenticationPrincipal AuthPrincipal principal
    ) {
        riderCommandFacade.goOffline(principal.accountId());

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.RIDER_OFFLINE_CHANGE_SUCCESS)
        );
    }
}