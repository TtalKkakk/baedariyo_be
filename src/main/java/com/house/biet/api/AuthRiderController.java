package com.house.biet.api;

import com.house.biet.auth.command.application.AuthService;
import com.house.biet.auth.command.domain.dto.LoginResultDto;
import com.house.biet.auth.command.domain.dto.LoginRequestDto;
import com.house.biet.auth.command.dto.ChangePasswordRequestDto;
import com.house.biet.auth.infrastructure.security.AuthPrincipal;
import com.house.biet.common.domain.enums.UserRole;
import com.house.biet.global.response.CustomApiResponse;
import com.house.biet.global.response.SuccessCode;
import com.house.biet.login.command.application.RiderLoginService;
import com.house.biet.signup.command.application.RiderSignupService;
import com.house.biet.auth.command.domain.dto.RiderSignupRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/rider")
public class AuthRiderController {

    private final RiderSignupService riderSignupService;
    private final RiderLoginService riderLoginService;
    private final AuthService authService;

    /**
     * 대상을 처리한다
     *
     * @param requestDto 요청 정보
     * @return signup 결과
     */
    @PostMapping("/signup")
    public ResponseEntity<CustomApiResponse<Void>> signup(
            @RequestBody @Valid RiderSignupRequestDto requestDto
    ) {
        riderSignupService.signup(requestDto);

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.SIGNUP_SUCCESS)
        );
    }

    /**
     * 대상을 처리한다
     *
     * @param principal 인증 사용자 정보
     * @return withdraw 결과
     */
    @PatchMapping("/withdraw")
    public ResponseEntity<CustomApiResponse<Void>> withdraw(
            @AuthenticationPrincipal AuthPrincipal principal
    ) {
        authService.withdraw(principal.accountId());

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.WITHDRAW_SUCCESS)
        );
    }

    /**
     * 대상을 처리한다
     *
     * @param requestDto 요청 정보
     * @return login 결과
     */
    @PostMapping("/login")
    public ResponseEntity<CustomApiResponse<LoginResultDto>> login(
            @RequestBody @Valid LoginRequestDto requestDto
    ) {
        LoginResultDto resultDto = riderLoginService.login(requestDto);

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.LOGIN_SUCCESS, resultDto)
        );
    }

    /**
     * Password을 변경한다
     *
     * @param principal 인증 사용자 정보
     * @param requestDto 요청 정보
     * @return changePassword 결과
     */
    @PatchMapping("/password")
    public ResponseEntity<CustomApiResponse<Void>> changePassword(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestBody @Valid ChangePasswordRequestDto requestDto
    ) {
        authService.changePassword(principal.accountId(), requestDto.password());

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.PASSWORD_CHANGE_SUCCESS)
        );
    }

    /**
     * Email Duplicate을 처리한다
     *
     * @param email email 값
     * @return checkEmailDuplicate 결과
     */
    @GetMapping("/email/duplicate")
    public ResponseEntity<CustomApiResponse<Boolean>> checkEmailDuplicate(
            @RequestParam @Email @NotBlank String email
    ) {
        boolean isDuplicated = authService.isDuplicatedEmailAndRole(email, UserRole.RIDER);

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.EMAIL_DUPLICATE_SEARCH_SUCCESS, isDuplicated)
        );
    }
}