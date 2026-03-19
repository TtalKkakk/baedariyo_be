package com.house.biet.api;

import com.house.biet.auth.infrastructure.security.AuthPrincipal;
import com.house.biet.global.response.CustomApiResponse;
import com.house.biet.global.response.SuccessCode;
import com.house.biet.rider.command.application.RiderCommandFacade;
import com.house.biet.rider.command.dto.ChangeNicknameRequestDto;
import com.house.biet.rider.command.dto.ChangePhoneNumberRequestDto;
import com.house.biet.rider.command.dto.ChangeVehicleTypeRequestDto;
import com.house.biet.rider.command.dto.ChangeWorkingStatusRequestDto;
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
 * 라이더 정보 변경(Command) 요청을 처리한다.
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
     * @param principal 인증된 사용자 정보
     * @param requestDto 변경할 닉네임 정보
     * @return 성공 응답
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
     * @param principal 인증된 사용자 정보
     * @param requestDto 변경할 전화번호 정보
     * @return 성공 응답
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
     * @param principal 인증된 사용자 정보
     * @param requestDto 변경할 차량 타입 정보
     * @return 성공 응답
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
     * 라이더 근무 상태 변경 API
     *
     * @param principal 인증된 사용자 정보
     * @param requestDto 변경할 근무 상태 정보
     * @return 성공 응답
     */
    @PatchMapping("/working-status")
    public ResponseEntity<CustomApiResponse<Void>> changeWorkingStatus(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestBody @Valid ChangeWorkingStatusRequestDto requestDto
    ) {
        riderCommandFacade.changeRiderWorkingStatus(
                principal.accountId(),
                requestDto.workingStatus()
        );

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.RIDER_WORKING_STATUS_CHANGE_SUCCESS)
        );
    }

    /**
     * 라이더 온라인 상태 전환 API
     *
     * <p>
     * 현재 OFFLINE 상태인 경우 ONLINE으로 변경한다.
     * </p>
     *
     * @param principal 인증된 사용자 정보
     * @return 성공 응답
     */
    @PatchMapping("/online")
    public ResponseEntity<CustomApiResponse<Void>> markOnlineIfOffline(
            @AuthenticationPrincipal AuthPrincipal principal
    ) {
        riderCommandFacade.markOnlineIfOffline(
                principal.accountId()
        );

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.RIDER_ONLINE_CHANGE_SUCCESS)
        );
    }
}