package com.house.biet.storeSearch.query.util;

import java.util.ArrayList;
import java.util.List;

public final class HangulTypingGenerator {

    private HangulTypingGenerator() {}

    private static final String[] CHO = {
            "ㄱ","ㄲ","ㄴ","ㄷ","ㄸ","ㄹ","ㅁ","ㅂ","ㅃ",
            "ㅅ","ㅆ","ㅇ","ㅈ","ㅉ","ㅊ","ㅋ","ㅌ","ㅍ","ㅎ"
    };

    private static final String[] JUNG = {
            "ㅏ","ㅐ","ㅑ","ㅒ","ㅓ","ㅔ","ㅕ","ㅖ",
            "ㅗ","ㅘ","ㅙ","ㅚ","ㅛ","ㅜ","ㅝ","ㅞ",
            "ㅟ","ㅠ","ㅡ","ㅢ","ㅣ"
    };

    private static final String[] JONG = {
            "","ㄱ","ㄲ","ㄳ","ㄴ","ㄵ","ㄶ","ㄷ","ㄹ",
            "ㄺ","ㄻ","ㄼ","ㄽ","ㄾ","ㄿ","ㅀ","ㅁ",
            "ㅂ","ㅄ","ㅅ","ㅆ","ㅇ","ㅈ","ㅊ","ㅋ",
            "ㅌ","ㅍ","ㅎ"
    };

    public static List<String> generate(String word) {

        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        for (char c : word.toCharArray()) {

            if (c < 0xAC00 || c > 0xD7A3) {
                current.append(c);
                result.add(current.toString());
                continue;
            }

            int temp = c - 0xAC00;

            int jong = temp % 28;
            int jung = ((temp - jong) / 28) % 21;
            int cho = ((temp - jong) / 28) / 21;

            // 1️⃣ 초성
            result.add(current + CHO[cho]);

            // 2️⃣ 초성 + 중성
            char syllable = (char) (0xAC00 + cho * 21 * 28 + jung * 28);
            result.add(current + String.valueOf(syllable));

            // 3️⃣ 종성
            if (jong != 0) {

                char full = (char) (0xAC00 + cho * 21 * 28 + jung * 28 + jong);
                result.add(current + String.valueOf(full));

                current.append(full);

            } else {

                current.append(syllable);
            }
        }

        return result.stream().distinct().toList();
    }
}