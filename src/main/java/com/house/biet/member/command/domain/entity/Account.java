package com.house.biet.member.command.domain.entity;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.member.command.domain.vo.AccountStatus;
import com.house.biet.member.command.domain.vo.Email;
import com.house.biet.member.command.domain.vo.Password;
import com.house.biet.global.vo.UserRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "accounts",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"email", "role"})  // 복합 유니크 설정
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "email", nullable = false)
    )
    private Email email;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "password", nullable = false)
    )
    private Password password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus accountStatus;

    private Account(Email email, Password password, UserRole role) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdAt = LocalDateTime.now();
        this.accountStatus = AccountStatus.ACTIVE;
    }

    public static Account signUp(Email email, Password encryptedPassword, UserRole role) {
        return new Account(email, encryptedPassword, role);
    }

    public void withdraw() {
        if (this.accountStatus == AccountStatus.WITHDRAWN)
            throw new CustomException(ErrorCode.ALREADY_WITHDRAWN_ACCOUNT);

        this.accountStatus = AccountStatus.WITHDRAWN;
    }

    public boolean matchedPassword(String rawPassword, PasswordEncoder encoder) {
        return password.matches(rawPassword, encoder);
    }

    public void changePassword(Password newEncryptedPassword) {
        this.password = newEncryptedPassword;
    }
}
