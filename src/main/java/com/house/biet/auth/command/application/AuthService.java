package com.house.biet.auth.command.application;

import com.house.biet.auth.command.domain.dto.LoginResultDto;
import com.house.biet.auth.infrastructure.jwt.JwtProvider;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.global.vo.UserRole;
import com.house.biet.member.command.AccountRepository;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.member.command.domain.vo.Email;
import com.house.biet.member.command.domain.vo.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    public void signup(String emailValue, String rawPasswordValue) {
        Email email = new Email(emailValue);

        if (accountRepository.existsByEmailAndRole(email, UserRole.USER))
            throw new CustomException(ErrorCode.ALREADY_EXIST_EMAIL);

        Password password = Password.encrypt(rawPasswordValue, passwordEncoder);
        Account account = Account.signUp(email, password, UserRole.USER);
        
        accountRepository.save(account);
    }

    public LoginResultDto login(String emailValue, String rawPasswordValue) {
        Email email = new Email(emailValue);

        Account account = accountRepository.findByEmailAndRole(email, UserRole.USER)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        if (!account.matchedPassword(rawPasswordValue, passwordEncoder))
            throw new CustomException(ErrorCode.NOT_CORRECT_PASSWORD);

        String accessToken = jwtProvider.createAccessToken(account.getId(), account.getRole().name());
        String refreshToken = jwtProvider.createRefreshToken(account.getId(), account.getRole().name());

        return new LoginResultDto(accessToken, refreshToken);
    }
}
