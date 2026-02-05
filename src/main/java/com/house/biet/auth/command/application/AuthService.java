package com.house.biet.auth.command.application;

import com.house.biet.auth.command.application.dto.AuthLoginResultDto;
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
public class AuthService {

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    /**
     * 회원가입 수행
     *
     * <p>
     * 동일한 이메일과 역할(UserRole)을 가진 계정이 이미 존재할 경우
     * 회원가입은 실패
     * </p>
     *
     * @param emailValue      회원 이메일
     * @param rawPasswordValue 평문 비밀번호
     * @param userRole        회원 역할
     * @throws CustomException 이미 존재하는 이메일과 역할 조합인 경우
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
     * <p>
     * 이 메서드는 이미 인증된 사용자가 자신의 계정을 탈퇴하는 시나리오를 전제로 하며,
     * accountId는 토큰 등을 통해 검증된 값이라고 가정
     * </p>
     *
     * <p>
     * 실제 탈퇴 처리 로직은 Account 도메인의 withdraw() 메서드에 위임
     * </p>
     *
     * @param accountId 탈퇴할 계정의 ID
     * @throws CustomException 계정을 찾을 수 없는 경우
     */
    public void withdraw(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        account.withdraw();
    }

    /**
     * 로그인을 수행하고 인증에 필요한 정보를 반환
     *
     * <p>
     * 이 메서드는 이메일, 비밀번호, 사용자 역할(UserRole)을 기반으로
     * 계정을 검증한 뒤 로그인을 수행한다.
     * </p>
     *
     * <p>
     * 로그인에 성공하면 AccessToken, RefreshToken과 함께
     * 내부 후속 처리(도메인 상태 변경 등)를 위해 Account 식별자(accountId)를 반환한다.
     * </p>
     *
     * <p>
     * 반환되는 결과는 <b>Application Layer 내부 전용</b> 데이터이며,
     * Controller 또는 외부 API 응답으로 직접 노출되어서는 안 된다.
     * </p>
     *
     * @param emailValue       로그인 이메일
     * @param rawPasswordValue 평문 비밀번호
     * @param userRole         로그인 역할
     *
     * @return accountId, AccessToken, RefreshToken을 포함한 내부 로그인 결과
     *
     * @throws CustomException 계정을 찾을 수 없는 경우
     * @throws CustomException 비밀번호가 일치하지 않는 경우
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

}
