package com.house.biet.store.command.domain.entity;

import com.house.biet.global.jpa.BaseTimeEntity;
import com.house.biet.store.command.domain.vo.StoreReviewComment;
import com.house.biet.store.command.domain.vo.StoreReviewImages;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "store_reviews")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreReview extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 외부 노출용 StoreReview 식별자
     */
    @Column(nullable = false, unique = true)
    private UUID publicStoreReviewId;

    /**
     * 리뷰 대상 Store의 외부 식별자
     */
    @Column(nullable = false)
    private UUID publicStoreId;

    /**
     * 리뷰 작성자 사용자 ID
     */
    @Column(nullable = false)
    private Long userId;

    /**
     * 리뷰가 작성된 주문 ID
     * 해당 주문을 통해 메뉴 정보는 Order 도메인에서 조회한다.
     */
    @Column(nullable = false)
    private Long orderId;

    /**
     * 가게에 대한 평점 (1~5)
     */
    @Column(nullable = false)
    private int rating;

    /**
     * 리뷰 이미지 (최대 4장)
     */
    @Embedded
    private StoreReviewImages storeReviewImages;

    /**
     * 리뷰 코멘트
     */
    @Embedded
    @AttributeOverride(
            name = "comment",
            column = @Column(name = "store_review_comment", length = 150)
    )
    private StoreReviewComment storeReviewComment;

    public static StoreReview create(
            UUID storeId,
            Long userId,
            Long orderId,
            int rating,
            StoreReviewImages storeReviewImages,
            StoreReviewComment storeReviewComment
    ) {
        return new StoreReview(
                null, // id는 DB에서 생성
                UUID.randomUUID(), // publicStoreReviewId 자동 생성
                storeId,
                userId,
                orderId,
                rating,
                storeReviewImages,
                storeReviewComment
        );
    }
}
