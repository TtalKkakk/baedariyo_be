package com.house.biet.user.command.domain.entity;

import com.house.biet.user.command.domain.vo.Email;
import com.house.biet.user.command.domain.vo.Password;
import com.house.biet.user.command.domain.vo.UserRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "email", nullable = false, unique = true)
    )
    private Email email;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "password", nullable = false)
    )
    private Password password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private LocalDateTime createdAt;

    private UserAccount(Email email, Password password, UserRole role) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdAt = LocalDateTime.now();
    }

    public static UserAccount signUp(Email email, Password encryptedPassword) {
        return new UserAccount(email, encryptedPassword, UserRole.USER);
    }

    public boolean matchedPassword(String rawPassword, PasswordEncoder encoder) {
        return password.matches(rawPassword, encoder);
    }

    public void changePassword(Password newEncryptedPassword) {
        this.password = newEncryptedPassword;
    }
}
