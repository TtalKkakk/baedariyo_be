package com.house.biet.user.command.domain.entity;

import com.house.biet.global.jpa.BaseTimeEntity;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.member.command.domain.vo.*;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private Account account;

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
            column = @Column(name = "phoneNumber", nullable = false, unique = true)
    )
    private PhoneNumber phoneNumber;

    public static User create(Account account, String realName, String nickname, String phoneNumber) {
        return new User(
                null,
                account,
                new RealName(realName),
                new Nickname(nickname),
                new PhoneNumber(phoneNumber)
        );
    }

    public void changeNickname(Nickname nickname) {
        this.nickname = nickname;
    }

    public void changePhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
