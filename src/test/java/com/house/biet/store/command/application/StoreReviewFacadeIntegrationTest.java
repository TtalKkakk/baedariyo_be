package com.house.biet.store.command.application;

import com.house.biet.auth.command.application.AuthService;
import com.house.biet.common.domain.enums.UserRole;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.store.command.application.dto.StoreReviewCreateRequestDto;
import com.house.biet.store.command.application.dto.StoreReviewCreateResponseDto;
import com.house.biet.store.command.domain.vo.StoreReviewComment;
import com.house.biet.store.command.domain.vo.StoreReviewImages;
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

    private Long accountId1;
    private Long accountId2;
    private UUID storeId = UUID.randomUUID();

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
    }

    @Test
    @DisplayName("성공 - 리뷰 생성")
    void createReview_Success() {

        // given
        StoreReviewCreateRequestDto request =
                new StoreReviewCreateRequestDto(
                        10L,
                        5,
                        new StoreReviewImages(List.of("image1.jpg")),
                        new StoreReviewComment("통합 테스트 용 comment")
                );

        // when
        StoreReviewCreateResponseDto responseDto =
                storeReviewFacade.createReview(request, storeId, accountId1);

        // then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.rating()).isEqualTo(5);
    }

    @Test
    @DisplayName("실패 - 작성자 불일치")
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