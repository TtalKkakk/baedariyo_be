package com.house.biet.auth.command.application;

import com.house.biet.auth.command.domain.dto.LoginResultDto;
import com.house.biet.auth.infrastructure.jwt.JwtProvider;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.user.command.UserAccountRepository;
import com.house.biet.user.command.domain.entity.UserAccount;
import com.house.biet.user.command.domain.vo.Email;
import com.house.biet.user.command.domain.vo.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUserService {

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserAccountRepository userAccountRepository;

    public void signup(String emailValue, String rawPasswordValue) {
        Email email = new Email(emailValue);

        if (userAccountRepository.existsByEmail(email))
            throw new CustomException(ErrorCode.ALREADY_EXIST_EMAIL);

        Password password = Password.encrypt(rawPasswordValue, passwordEncoder);
        UserAccount userAccount = UserAccount.signUp(email, password);

        userAccountRepository.save(userAccount);
    }

    public LoginResultDto login(String emailValue, String rawPasswordValue) {
        Email email = new Email(emailValue);

        UserAccount userAccount = userAccountRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        if (!userAccount.matchedPassword(rawPasswordValue, passwordEncoder))
            throw new CustomException(ErrorCode.NOT_CORRECT_PASSWORD);

        String accessToken = jwtProvider.createAccessToken(userAccount.getId(), userAccount.getRole().name());
        String refreshToken = jwtProvider.createRefreshToken(userAccount.getId(), userAccount.getRole().name());

        return new LoginResultDto(accessToken, refreshToken);
    }
}
