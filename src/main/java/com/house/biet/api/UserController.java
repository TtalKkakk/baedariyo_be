package com.house.biet.api;

import com.house.biet.auth.infrastructure.security.AuthPrincipal;
import com.house.biet.global.response.CustomApiResponse;
import com.house.biet.global.response.SuccessCode;
import com.house.biet.user.command.application.UserCommandFacade;
import com.house.biet.user.command.application.UserService;
import com.house.biet.user.command.dto.ChangeNicknameRequestDto;
import com.house.biet.user.command.dto.ChangePhoneNumberRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserCommandFacade userCommandFacade;

    @PatchMapping("/nickname")
    public ResponseEntity<CustomApiResponse<Void>> changeNickname(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestBody @Valid ChangeNicknameRequestDto requestDto
    ) {
        userCommandFacade.changeNickname(principal.accountId(), requestDto.nickname());

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.USER_NICKNAME_CHANGE_SUCCESS)
        );
    }

    @PatchMapping("/phoneNumber")
    public ResponseEntity<CustomApiResponse<Void>> changePhoneNumber(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestBody @Valid ChangePhoneNumberRequestDto requestDto
    ) {
        userCommandFacade.changePhoneNumber(principal.accountId(), requestDto.phoneNumber());

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.USER_PHONE_NUMBER_CHANGE_SUCCESS)
        );
    }
}
