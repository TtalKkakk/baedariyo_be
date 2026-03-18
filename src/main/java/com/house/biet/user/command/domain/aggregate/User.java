package com.house.biet.user.command.domain.aggregate;

import com.house.biet.common.domain.vo.Address;
import com.house.biet.global.jpa.BaseTimeEntity;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.member.command.domain.vo.*;
import com.house.biet.store.command.domain.vo.GeoLocation;
import com.house.biet.user.command.domain.entity.UserAddress;
import com.house.biet.user.command.domain.vo.AddressAlias;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<UserAddress> addresses = new ArrayList<>();

    public static User create(
            Account account,
            String realName,
            String nickname,
            String phoneNumber,
            Address address,
            GeoLocation geoLocation,
            AddressAlias alias
    ) {
        User user = new User(
                null,
                account,
                new RealName(realName),
                new Nickname(nickname),
                new PhoneNumber(phoneNumber),
                new ArrayList<>()
        );

        // 기본 주소 무조건 추가
        user.addAddress(address, geoLocation, alias, true);

        return user;
    }

    /* ===== 도메인 로직 ===== */

    public void changeNickname(Nickname nickname) {
        this.nickname = nickname;
    }

    public void changePhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * 주소 추가
     */
    public UserAddress addAddress(Address address, GeoLocation geoLocation, AddressAlias addressAlias, boolean isDefault) {
        validateAliasNotDuplicate(addressAlias.getValue());

        // 첫 주소면 무조건 default
        if (addresses.isEmpty()) {
            isDefault = true;
        }

        // default 설정이면 기존 default 제거
        if (isDefault) {
            addresses.forEach(UserAddress::unsetDefault);
        }

        UserAddress userAddress =
                UserAddress.create(this, address, geoLocation, addressAlias, isDefault);

        addresses.add(userAddress);

        return userAddress;
    }

    /**
     * 배송지 별명 변경
     */
    public void changeAddressAlias(String addressAlias, String newAddressAlias) {
        validateAliasNotDuplicate(newAddressAlias);

        UserAddress target = addresses.stream()
                .filter(addr -> addr.getAlias().getValue().equals(addressAlias))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.DELIVERY_ADDRESS_NOT_FOUND));

        target.changeAlias(new AddressAlias(newAddressAlias));
    }

    /**
     * 기본 배송지 변경
     */
    public void changeDefaultAddress(String addressAlias) {
        UserAddress target = addresses.stream()
                .filter(addr -> addr.getAlias().getValue().equals(addressAlias))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.DELIVERY_ADDRESS_NOT_FOUND));

        addresses.forEach(UserAddress::unsetDefault);
        target.setAsDefault();
    }

    /**
     * 주소 삭제
     */
    public void removeAddress(String addressAlias) {
        UserAddress target = addresses.stream()
                .filter(addr -> addr.getAlias().getValue().equals(addressAlias))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.DELIVERY_ADDRESS_NOT_FOUND));

        boolean wasDefault = target.isDefault();

        addresses.remove(target);

        // default였으면 다른 주소를 default로 설정
        if (wasDefault && !addresses.isEmpty()) {
            addresses.get(0).setAsDefault();
        }
    }

    private void validateAliasNotDuplicate(String addressAlias) {
        boolean exists = addresses.stream()
                .anyMatch(addr -> addr.getAlias().getValue().equals(addressAlias));

        if (exists) {
            throw new CustomException(ErrorCode.DUPLICATE_ADDRESS_ALIAS);
        }
    }
}
