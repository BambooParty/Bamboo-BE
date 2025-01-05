package org.pandas.bambooclub.domain.mentality.dto;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MbtiCharacters {
    @Setter
    @Getter
    public  String mbti1;
    @Setter
    @Getter
    public  String mbti2;
    @Setter
    @Getter
    public  String mbti3;
    @Setter
    @Getter
    public  String mbti4;

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

}
