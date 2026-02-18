package com.house.biet.store.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class StoreOperationInfoTest {

    @Test
    @DisplayName("성공 - 가게 정보와 원산지 정보 생성")
    void CreateStoreOperationInfo_Success() {
        // given
        String storeInfo = "매장 내 식사 가능";
        String originInfo = "돼지고기: 국내산";

        // when
        StoreOperationInfo info = new StoreOperationInfo(storeInfo, originInfo);

        // then
        assertThat(info.getStoreInfo()).isEqualTo(storeInfo);
        assertThat(info.getOriginInfo()).isEqualTo(originInfo);
    }

    @Test
    @DisplayName("성공 - 원산지 정보만 있는 경우")
    void CreateWithOnlyOriginInfo() {
        // given
        String originInfo = "쌀: 국내산";

        // when
        StoreOperationInfo info = new StoreOperationInfo(null, originInfo);

        // then
        assertThat(info.getStoreInfo()).isNull();
        assertThat(info.getOriginInfo()).isEqualTo(originInfo);
    }

    @Test
    @DisplayName("에러 - 가게 정보가 빈 문자열")
    void CreateStoreOperationInfo_Error_WhenStoreInfoIsBlank() {
        // given
        String storeInfo = "   ";

        // when & then
        assertThatThrownBy(() -> new StoreOperationInfo(storeInfo, null))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_STORE_INFO_FORMAT.getMessage());
    }

    @Test
    @DisplayName("에러 - 원산지 정보가 빈 문자열")
    void CreateStoreOperationInfo_Error_WhenOriginInfoIsBlank() {
        // given
        String originInfo = "";

        // when & then
        assertThatThrownBy(() -> new StoreOperationInfo(null, originInfo))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_OPERATION_INFO_FORMAT.getMessage());
    }
}
