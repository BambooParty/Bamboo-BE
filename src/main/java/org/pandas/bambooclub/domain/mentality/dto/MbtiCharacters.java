package org.pandas.bambooclub.domain.mentality.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MbtiCharacters {
    @Setter
    @Getter
    public String mbti1;
    @Setter
    @Getter
    public String mbti2;
    @Setter
    @Getter
    public String mbti3;
    @Setter
    @Getter
    public String mbti4;

    public static MbtiCharacters makeMbti(String mbti) {
        if (mbti == null || mbti.isEmpty()) {
            return null;
        }

        MbtiCharacters condition = new MbtiCharacters();

        // 각 문자를 순회하며 조건에 따라 설정
        for (char c : mbti.toCharArray()) {
            switch (c) {
                case 'I', 'E' -> condition.setMbti1(String.valueOf(c));
                case 'N', 'S' -> condition.setMbti2(String.valueOf(c));
                case 'T', 'F' -> condition.setMbti3(String.valueOf(c));
                case 'J', 'P' -> condition.setMbti4(String.valueOf(c));
                default -> throw new IllegalArgumentException("Invalid MBTI character: " + c);
            }
        }

        return condition;
    }

    //존재하는 글자가 모두 들어가는 mbti 묶음 생성
    public static List<String> mbtis(String input) {
        // MBTI의 각 카테고리
        char[] group1 = {'E', 'I'};
        char[] group2 = {'S', 'N'};
        char[] group3 = {'T', 'F'};
        char[] group4 = {'J', 'P'};

        // 결과 리스트
        List<String> result = new ArrayList<>();

        // 입력을 대문자로 변환
        input = input.toUpperCase();

        // 모든 가능한 MBTI 조합 생성
        for (char g1 : group1) {
            for (char g2 : group2) {
                for (char g3 : group3) {
                    for (char g4 : group4) {
                        String candidate = "" + g1 + g2 + g3 + g4;
                        // 입력값이 포함된 MBTI만 추가
                        if (candidate.contains(input)) {
                            result.add(candidate);
                        }
                    }
                }
            }
        }

        return result;
    }


}
