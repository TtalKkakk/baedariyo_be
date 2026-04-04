package com.house.biet.user.command.domain.entity;

import com.house.biet.global.jpa.BaseTimeEntity;
import com.house.biet.user.command.domain.aggregate.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_payment_methods")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPaymentMethod extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* 연관관계 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /* 빌링키 */
    @Column(name = "billing_key", nullable = false)
    private String billingKey;

    /* 카드 번호 (마스킹 처리, 예: 1234-56**-****-3456) */
    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    /* 카드 이름 */
    @Column(name = "card_name", nullable = false)
    private String cardName;

    /* 대표 결제수단 여부 */
    @Column(name = "is_default", nullable = false)
    private boolean isDefault;

    /* ===== 생성 ===== */

    private UserPaymentMethod(User user, String billingKey, String cardNumber, String cardName, boolean isDefault) {
        this.user = user;
        this.billingKey = billingKey;
        this.cardNumber = cardNumber;
        this.cardName = cardName;
        this.isDefault = isDefault;
    }

    public static UserPaymentMethod create(User user, String billingKey, String cardNumber, String cardName, boolean isDefault) {
        return new UserPaymentMethod(user, billingKey, cardNumber, cardName, isDefault);
    }

    /* ===== 도메인 로직 ===== */

    public void setAsDefault() {
        this.isDefault = true;
    }

    public void unsetDefault() {
        this.isDefault = false;
    }
}
