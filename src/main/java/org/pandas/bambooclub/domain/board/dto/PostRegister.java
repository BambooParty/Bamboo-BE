package org.pandas.bambooclub.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRegister {
    private String title;
    private String content;
    private String mbti;
    private String nickname;
}
