package com.house.biet.api;

import com.house.biet.auth.command.application.AuthService;
import com.house.biet.auth.command.domain.dto.LoginResultDto;
import com.house.biet.auth.command.domain.dto.UserLoginRequestDto;
import com.house.biet.auth.infrastructure.security.AuthPrincipal;
import com.house.biet.global.response.CustomApiResponse;
import com.house.biet.global.response.SuccessCode;
import com.house.biet.global.vo.UserRole;
import com.house.biet.login.command.application.RiderLoginService;
import com.house.biet.signup.command.application.RiderSignupService;
import com.house.biet.auth.command.domain.dto.RiderSignupRequestDto;
import jakarta.validation.Valid;
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

    @PostMapping("/signup")
    public ResponseEntity<CustomApiResponse<Void>> signup(
            @RequestBody @Valid RiderSignupRequestDto requestDto
    ) {
        riderSignupService.signup(requestDto);

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.SIGNUP_SUCCESS)
        );
    }

    @PatchMapping("/withdraw")
    public ResponseEntity<CustomApiResponse<Void>> withdraw(
            @AuthenticationPrincipal AuthPrincipal principal
    ) {
        authService.withdraw(principal.accountId());

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.WITHDRAW_SUCCESS)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<CustomApiResponse<LoginResultDto>> login(
            @RequestBody @Valid UserLoginRequestDto requestDto
    ) {
        LoginResultDto resultDto = riderLoginService.login(requestDto);

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.LOGIN_SUCCESS, resultDto)
        );
    }
}