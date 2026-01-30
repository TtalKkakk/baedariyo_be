package com.house.biet.auth.command.application;

import com.house.biet.auth.command.domain.dto.LoginResultDto;
import com.house.biet.auth.infrastructure.jwt.JwtProvider;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.user.command.AccountRepository;
import com.house.biet.user.command.domain.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    public LoginResultDto login(String email, String rawPassword) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        if (!account.matchedPassword(rawPassword, passwordEncoder))
            throw new CustomException(ErrorCode.NOT_CORRECT_PASSWORD);

        String accessToken = jwtProvider.createAccessToken(account.getId(), account.getRole().name());
        String refreshToken = jwtProvider.createRefreshToken(account.getId(), account.getRole().name());

        return new LoginResultDto(accessToken, refreshToken);
    }
}
