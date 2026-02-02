package com.house.biet.api;

import com.house.biet.auth.command.application.AuthUserService;
import com.house.biet.auth.command.domain.dto.UserLoginRequestDto;
import com.house.biet.auth.command.domain.dto.LoginResultDto;
import com.house.biet.auth.command.domain.dto.UserSignupRequestDto;
import com.house.biet.global.response.CustomApiResponse;
import com.house.biet.global.response.SuccessCode;
import com.house.biet.signup.command.application.UserSignupService;
import com.house.biet.user.command.application.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/user")
public class AuthUserController {

    private final UserSignupService userSignupService;
    private final AuthUserService authUserService;

    @PostMapping("/signup")
    public ResponseEntity<CustomApiResponse<Void>> signup(
            @RequestBody @Valid UserSignupRequestDto requestDto
    ) {

        userSignupService.signup(requestDto);

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.SIGNUP_SUCCESS)
        );
    }

    @PostMapping("/login")
    public  ResponseEntity<CustomApiResponse<LoginResultDto>> login(
            @RequestBody @Valid UserLoginRequestDto requestDto
    ) {
        LoginResultDto resultDto = authUserService.login(
                requestDto.email(),
                requestDto.password()
        );

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.LOGIN_SUCCESS, resultDto)
        );
    }
}
