package com.house.biet.store.command.domain.aggregate;

import com.house.biet.common.domain.enums.StoreCategory;
import com.house.biet.common.domain.vo.Money;
import com.house.biet.store.command.domain.entity.Menu;
import com.house.biet.store.command.domain.vo.*;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "stores")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private UUID publicId;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "store_name", nullable = false)
    )
    private StoreName storeName;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private StoreCategory storeCategory;

    @Embedded
    @AttributeOverride(
            name = "imageUrl",
            column = @Column(name = "thumbnail_image_url")
    )
    private StoreThumbnail thumbnail;        // nullable

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "totalRating", column = @Column(name = "rating_total")),
            @AttributeOverride(name = "reviewCount", column = @Column(name = "rating_count"))
    })
    private StoreRating rating;

    @Embedded
    private BusinessHours businessHours;     // nullable 가능

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "storeInfo", column = @Column(name = "store_info")),
            @AttributeOverride(name = "originInfo", column = @Column(name = "origin_info"))
    })
    private StoreOperationInfo operationInfo;  // nullable

    @Embedded
    @AttributeOverride(
            name = "amount",
            column = @Column(name = "minimum_order_amount", nullable = false)
    )
    private Money minimumOrderAmount;

    @Embedded
    @AttributeOverride(
            name = "amount",
            column = @Column(name = "delivery_fee", nullable = false)
    )
    private Money deliveryFee;

    @OneToMany(
            mappedBy = "store",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Menu> menus = new ArrayList<>();

    /* ===== 생성 ===== */

    private Store(
            StoreName storeName,
            StoreCategory storeCategory,
            StoreThumbnail thumbnail,
            BusinessHours businessHours,
            StoreOperationInfo operationInfo,
            Money minimumOrderAmount,
            Money deliveryFee
    ) {
        this.publicId = UUID.randomUUID();
        this.storeName = storeName;
        this.storeCategory = storeCategory;
        this.thumbnail = thumbnail;
        this.businessHours = businessHours;
        this.operationInfo = operationInfo;
        this.minimumOrderAmount = minimumOrderAmount;
        this.deliveryFee = deliveryFee;
        this.rating = StoreRating.empty();
    }

    public static Store create(
            StoreName storeName,
            StoreCategory storeCategory,
            StoreThumbnail thumbnail,
            BusinessHours businessHours,
            StoreOperationInfo operationInfo,
            Money minimumOrderAmount,
            Money deliveryFee
    ) {
        return new Store(
                storeName,
                storeCategory,
                thumbnail,
                businessHours,
                operationInfo,
                minimumOrderAmount,
                deliveryFee
        );
    }

    /* ===== 도메인 로직 ===== */

    public Menu addMenu(MenuName name, Money price, String description) {
        Menu menu = Menu.create(this, name, price, description);
        menus.add(menu);
        return menu;
    }

    public void addRating(int ratingScore) {
        this.rating = this.rating.addRating(ratingScore);
    }
}
