package com.house.biet.storeSearch.query.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HangulTypingGeneratorTest {

    @Test
    @DisplayName("성공 - 일반 한글 타이핑 prefix 생성")
    void generateTypingPrefix_Success_NormalHangul() {

        // given
        String keyword = "치킨";

        // when
        List<String> result = HangulTypingGenerator.generate(keyword);

        // then
        assertThat(result).containsExactly(
                "ㅊ",
                "치",
                "치ㅋ",
                "치키",
                "치킨"
        );
    }

    @Test
    @DisplayName("성공 - 받침 없는 한글 처리")
    void generateTypingPrefix_Success_NoJong() {

        // given
        String keyword = "가";

        // when
        List<String> result = HangulTypingGenerator.generate(keyword);

        // then
        assertThat(result).containsExactly(
                "ㄱ",
                "가"
        );
    }

    @Test
    @DisplayName("성공 - 받침 있는 한글 처리")
    void generateTypingPrefixWithJong_Success() {

        // given
        String keyword = "각";

        // when
        List<String> result = HangulTypingGenerator.generate(keyword);

        // then
        assertThat(result).containsExactly(
                "ㄱ",
                "가",
                "각"
        );
    }

    @Test
    @DisplayName("성공 - 쌍받침 처리")
    void generateTypingPrefix_Success_DoubleJong() {

        // given
        String keyword = "값";

        // when
        List<String> result = HangulTypingGenerator.generate(keyword);

        // then
        assertThat(result).containsExactly(
                "ㄱ",
                "가",
                "값"
        );
    }

    @Test
    @DisplayName("성공 - 여러 글자 한글 처리")
    void generateTypingPrefix_Success_MultiHangul() {

        // given
        String keyword = "치즈";

        // when
        List<String> result = HangulTypingGenerator.generate(keyword);

        // then
        assertThat(result).containsExactly(
                "ㅊ",
                "치",
                "치ㅈ",
                "치즈"
        );
    }

    @Test
    @DisplayName("성공 - 한글과 영어 혼합 처리")
    void generateTypingPrefix_Success_HangulEnglishMix() {

        // given
        String keyword = "치킨A";

        // when
        List<String> result = HangulTypingGenerator.generate(keyword);

        // then
        assertThat(result).containsExactly(
                "ㅊ",
                "치",
                "치ㅋ",
                "치키",
                "치킨",
                "치킨A"
        );
    }

    @Test
    @DisplayName("성공 - 특수 유니코드 한글 처리")
    void generateTypingPrefix_Success_RareHangul() {

        // given
        String keyword = "꿿떊";

        // when
        List<String> result = HangulTypingGenerator.generate(keyword);

        // then
        assertThat(result.get(0)).isNotNull();
        assertThat(result.get(result.size() - 1)).isEqualTo("꿿떊");
    }

    @Test
    @DisplayName("성공 - 긴 단어 prefix 생성")
    void generateTypingPrefix_Success_LongWord() {

        // given
        String keyword = "치킨치즈버거";

        // when
        List<String> result = HangulTypingGenerator.generate(keyword);

        // then
        assertThat(result.get(0)).isEqualTo("ㅊ");
        assertThat(result.get(result.size() - 1)).isEqualTo("치킨치즈버거");
    }
}