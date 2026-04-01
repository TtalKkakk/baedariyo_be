package com.house.biet.auth.command.application;

import com.house.biet.auth.command.application.dto.AuthLoginResultDto;
import com.house.biet.auth.infrastructure.jwt.JwtProvider;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.common.domain.enums.UserRole;
import com.house.biet.member.command.AccountRepository;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.member.command.domain.vo.Email;
import com.house.biet.member.command.domain.vo.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 인증 및 계정 생명주기(회원가입, 로그인, 탈퇴)를 담당
 *
 * <p>
 * 이 서비스는 Account 도메인을 중심으로
 * 인증 처리 및 계정 상태 변경을 조율하며,
 * 실제 비즈니스 규칙은 도메인(Account)에 위임합니다.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    /**
     * 회원가입 수행
     *
     * @param emailValue 회원 이메일
     * @param rawPasswordValue 평문 비밀번호
     * @param userRole 회원 역할
     * @return signup 결과
     */
    public Account signup(String emailValue, String rawPasswordValue, UserRole userRole) {
        Email email = new Email(emailValue);

        if (accountRepository.existsByEmailAndRole(email, userRole))
            throw new CustomException(ErrorCode.ALREADY_EXIST_EMAIL_AND_ROLE);

        Password password = Password.encrypt(rawPasswordValue, passwordEncoder);
        Account account = Account.signup(email, password, userRole);
        
        return accountRepository.save(account);
    }

    /**
     * 회원 탈퇴 처리
     *
     * @param accountId 탈퇴할 계정의 ID
     */
    public void withdraw(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        account.withdraw();
    }

    /**
     * 로그인을 수행하고 인증에 필요한 정보를 반환
     *
     * @param emailValue 로그인 이메일
     * @param rawPasswordValue 평문 비밀번호
     * @param userRole 로그인 역할
     * @return accountId, AccessToken, RefreshToken을 포함한 내부 로그인 결과
     */
    public AuthLoginResultDto login(String emailValue, String rawPasswordValue, UserRole userRole) {
        Email email = new Email(emailValue);

        Account account = accountRepository.findByEmailAndRole(email, userRole)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        if (!account.matchedPassword(rawPasswordValue, passwordEncoder)) {
            throw new CustomException(ErrorCode.NOT_CORRECT_PASSWORD);
        }

        String accessToken = jwtProvider.createAccessToken(
                account.getId(),
                account.getRole().name()
        );

        String refreshToken = jwtProvider.createRefreshToken(
                account.getId(),
                account.getRole().name()
        );

        return new AuthLoginResultDto(
                account.getId(),
                accessToken,
                refreshToken
        );
    }

    /**
     * 계정의 비밀번호를 변경한다.
     *
     * @param accountId 비밀번호를 변경할 계정 ID
     * @param newPasswordValue 변경할 평문 비밀번호
     */
    public void changePassword(Long accountId, String newPasswordValue) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        Password newPassword = Password.encrypt(newPasswordValue, passwordEncoder);
        account.changePassword(newPassword);
    }

    /**
     * 이메일과 사용자 역할(UserRole)의 중복 여부를 확인한다.
     *
     * @param email 확인할 이메일 (평문)
     * @param userRole 사용자 역할
     * @return true - 동일한 이메일 + 역할을 가진 계정이 존재하는 경우 (중복) false - 존재하지 않는 경우 (사용 가능)
     */
    @Transactional(readOnly = true)
    public boolean isDuplicatedEmailAndRole(String email, UserRole userRole) {
        return accountRepository.existsByEmailAndRole(new Email(email), userRole);
    }
}
