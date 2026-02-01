package com.house.biet.user.command.domain.entity;

import com.house.biet.member.command.domain.vo.Email;
import com.house.biet.member.command.domain.vo.Nickname;
import com.house.biet.member.command.domain.vo.Password;
import com.house.biet.global.vo.UserRole;
import com.house.biet.member.command.domain.vo.RealName;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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
            column = @Column(name = "real_name", nullable = false)
    )
    private RealName realName;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "nickname", nullable = false, unique = true)
    )
    private Nickname nickname;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "password", nullable = false)
    )
    private Password password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    public static User create(String email, String realName, String nickname, String rawPassword, PasswordEncoder encoder) {
        return new User(
                null,
                new Email(email),
                new RealName(realName),
                new Nickname(nickname),
                Password.encrypt(rawPassword, encoder),
                UserRole.USER
        );
    }
}
