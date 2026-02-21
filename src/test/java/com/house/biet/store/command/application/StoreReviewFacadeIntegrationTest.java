package com.house.biet.store.command.application;

import com.house.biet.auth.command.application.AuthService;
import com.house.biet.common.domain.enums.StoreCategory;
import com.house.biet.common.domain.enums.UserRole;
import com.house.biet.common.domain.vo.Money;
import com.house.biet.fixtures.BusinessHoursFixture;
import com.house.biet.fixtures.StoreOperationInfoFixture;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.store.command.application.dto.StoreReviewCreateRequestDto;
import com.house.biet.store.command.application.dto.StoreReviewCreateResponseDto;
import com.house.biet.store.command.domain.aggregate.Store;
import com.house.biet.store.command.domain.vo.StoreName;
import com.house.biet.store.command.domain.vo.StoreReviewComment;
import com.house.biet.store.command.domain.vo.StoreReviewImages;
import com.house.biet.store.command.domain.vo.StoreThumbnail;
import com.house.biet.support.config.ServiceIntegrationTest;
import com.house.biet.user.command.application.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StoreReviewFacadeIntegrationTest extends ServiceIntegrationTest {

    @Autowired
    private StoreReviewFacade storeReviewFacade;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private StoreService storeService;

    private Long accountId1;
    private Long accountId2;
    private UUID storeId;

    @BeforeEach
    void setup() {
        Account savedAccount1 = authService.signup(
                "testAccount1@test.com",
                "akejalk1@ajkhakelx!1",
                UserRole.USER
        );
        accountId1 = savedAccount1.getId();

        Account savedAccount2 = authService.signup(
                "testAccount2@test.com",
                "akejalk1@ajkhakelx!2",
                UserRole.USER
        );
        accountId2 = savedAccount2.getId();

        userService.save(
                savedAccount1,
                "RealName1",
                "Nickname1",
                "010-0000-0001"
        );

        userService.save(
                savedAccount2,
                "RealName2",
                "Nickname2",
                "010-0000-0002"
        );

        Store store = Store.create(
                new StoreName("테스트가게"),
                StoreCategory.CHICKEN,
                new StoreThumbnail("http://image.com"),
                BusinessHoursFixture.withDefaultWeekdays(),
                StoreOperationInfoFixture.aStoreOperationInfo().build(),
                new Money(15000),
                new Money(3000)
        );

        Store savedStore = storeService.save(store);
        storeId = savedStore.getPublicId();
    }

    @Test
    @DisplayName("성공 - 리뷰 생성")
    void createReview_Success() {
        // given
        int newRating = 5;
        StoreReviewCreateRequestDto request =
                new StoreReviewCreateRequestDto(
                        10L,
                        newRating,
                        new StoreReviewImages(List.of("image1.jpg")),
                        new StoreReviewComment("통합 테스트 용 comment")
                );

        // when
        StoreReviewCreateResponseDto responseDto =
                storeReviewFacade.createReview(request, storeId, accountId1);

        // then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.rating()).isEqualTo(newRating);

        Store store = storeService.getStoreByPublicId(storeId);
        assertThat(store.getRating().average()).isEqualTo(newRating);
    }

    @Test
    @DisplayName("성공 - 리뷰 여러개 생성 시 평점 증가 및 리뷰 개수 증가")
    void createReview_Success_Multiple() {
        // given
        int newRating1 = 4;

        StoreReviewCreateRequestDto request1 =
                new StoreReviewCreateRequestDto(
                        10L,
                        newRating1,
                        new StoreReviewImages(List.of()),
                        new StoreReviewComment("삭제 테스트1")
                );


        int newRating2 = 5;
        StoreReviewCreateRequestDto request2 =
                new StoreReviewCreateRequestDto(
                        13L,
                        newRating2,
                        new StoreReviewImages(List.of()),
                        new StoreReviewComment("삭제 테스트2")
                );


        int newRating3 = 3;
        StoreReviewCreateRequestDto request3 =
                new StoreReviewCreateRequestDto(
                        16L,
                        newRating3,
                        new StoreReviewImages(List.of()),
                        new StoreReviewComment("삭제 테스트3")
                );

        // when
        storeReviewFacade.createReview(request1, storeId, accountId1);
        storeReviewFacade.createReview(request2, storeId, accountId1);
        storeReviewFacade.createReview(request3, storeId, accountId2);

        // then
        Store store = storeService.getStoreByPublicId(storeId);
        assertThat(store.getRating().average()).isEqualTo((double)(newRating1 + newRating2 + newRating3) / 3);
        assertThat(store.getRating().getReviewCount()).isEqualTo(3);
    }


    @Test
    @DisplayName("성공 - 리뷰 삭제 시 평점 감소")
    void deleteReview_Success() {
        // given
        int newRating1 = 4;

        StoreReviewCreateRequestDto request1 =
                new StoreReviewCreateRequestDto(
                        10L,
                        newRating1,
                        new StoreReviewImages(List.of()),
                        new StoreReviewComment("삭제 테스트1")
                );

        StoreReviewCreateResponseDto createResponseDto1 =
                storeReviewFacade.createReview(request1, storeId, accountId1);

        int newRating2 = 5;
        StoreReviewCreateRequestDto request2 =
                new StoreReviewCreateRequestDto(
                        13L,
                        newRating2,
                        new StoreReviewImages(List.of()),
                        new StoreReviewComment("삭제 테스트2")
                );

        storeReviewFacade.createReview(request2, storeId, accountId1);

        int newRating3 = 3;
        StoreReviewCreateRequestDto request3 =
                new StoreReviewCreateRequestDto(
                        16L,
                        newRating3,
                        new StoreReviewImages(List.of()),
                        new StoreReviewComment("삭제 테스트3")
                );

        storeReviewFacade.createReview(request3, storeId, accountId2);

        UUID deletedStoreReviewId = createResponseDto1.storeReviewPublicId();

        // when
        storeReviewFacade.deleteReview(deletedStoreReviewId, accountId1);

        // then
        Store store = storeService.getStoreByPublicId(storeId);
        assertThat(store.getRating().average()).isEqualTo((double)(newRating2 + newRating3) / 2);
        assertThat(store.getRating().getReviewCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("에러 - 없는 리뷰 삭제")
    void deleteReview_Error_ReviewNotFound() {
        // given
        UUID deletedStoreReviewId = UUID.randomUUID();

        // when & then
        assertThatThrownBy(() -> storeReviewFacade.deleteReview(deletedStoreReviewId, accountId1))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.STORE_REVIEW_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("에러 - 작성자 불일치")
    void deleteReview_Error_UserMismatch() {

        // given
        StoreReviewCreateRequestDto request =
                new StoreReviewCreateRequestDto(
                        10L,
                        5,
                        new StoreReviewImages(List.of()),
                        new StoreReviewComment("삭제 테스트 용 comment")
                );

        StoreReviewCreateResponseDto createdDto =
                storeReviewFacade.createReview(request, storeId, accountId1);

        Long otherAccountId = accountId2;

        // when & then
        assertThatThrownBy(() -> storeReviewFacade.deleteReview(createdDto.storeReviewPublicId(), otherAccountId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.FORBIDDEN_STORE_REVIEW_MODIFICATION.getMessage());
    }
}