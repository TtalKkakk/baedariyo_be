package com.house.biet.api;

import com.house.biet.auth.command.application.AuthService;
import com.house.biet.auth.command.domain.dto.LoginRequestDto;
import com.house.biet.auth.command.domain.dto.LoginResultDto;
import com.house.biet.global.response.CustomApiResponse;
import com.house.biet.global.response.SuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public  ResponseEntity<CustomApiResponse<LoginResultDto>> login(
            @RequestBody @Valid LoginRequestDto requestDto
    ) {
        LoginResultDto resultDto = authService.login(
                requestDto.email(),
                requestDto.password()
        );

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.LOGIN_SUCCESS, resultDto)
        );
    }
}
