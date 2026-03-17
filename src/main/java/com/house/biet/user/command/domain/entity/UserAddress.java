package com.house.biet.user.command.domain.entity;

import com.house.biet.common.domain.vo.Address;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.store.command.domain.vo.GeoLocation;
import com.house.biet.user.command.domain.aggregate.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_addresses")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* 연관관계 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /* 주소 */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "roadAddress", column = @Column(name = "road_address", nullable = false)),
            @AttributeOverride(name = "jibunAddress", column = @Column(name = "jibun_address", nullable = false)),
            @AttributeOverride(name = "detailAddress", column = @Column(name = "detail_address", nullable = false))
    })
    private Address address;

    /* 좌표 */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "latitude", nullable = false)),
            @AttributeOverride(name = "longitude", column = @Column(name = "longitude", nullable = false))
    })
    private GeoLocation geoLocation;

    /* 별칭 (집, 회사 등) */
    @Column(name = "alias", nullable = false, length = 20)
    private String alias;

    /* 대표 배송지 여부 */
    @Column(name = "is_default", nullable = false)
    private boolean isDefault;

    /* ===== 생성 ===== */

    private UserAddress(User user, Address address, GeoLocation geoLocation, String alias, boolean isDefault) {
        this.user = user;
        this.address = address;
        this.geoLocation = geoLocation;
        this.alias = alias;
        this.isDefault = isDefault;
    }

    public static UserAddress create(User user, Address address, GeoLocation geoLocation, String alias, boolean isDefault) {
        validateAlias(alias);
        return new UserAddress(user, address, geoLocation, alias, isDefault);
    }

    /* ===== 도메인 로직 ===== */

    public void setAsDefault() {
        this.isDefault = true;
    }

    public void unsetDefault() {
        this.isDefault = false;
    }

    public void changeAlias(String alias) {
        validateAlias(alias);
        this.alias = alias;
    }

    private static void validateAlias(String alias) {
        if (alias == null || alias.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_ADDRESS_ALIAS_FORMAT);
        }
    }
}
