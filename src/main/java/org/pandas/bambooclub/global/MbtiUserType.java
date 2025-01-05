package org.pandas.bambooclub.global;

import lombok.Getter;

@Getter
public enum MbtiUserType {
    ISTJ("관리자형"),
    ISFJ("수호자형"),
    INFJ("옹호자형"),
    INTJ("전략가형"),
    ISTP("백과사전형"),
    ISFP("예술가형"),
    INFP("잔다르크형"),
    INTP("사색가형"),
    ESTP("사업가형"),
    ESFP("연예인형"),
    ENFP("스파크형"),
    ENTP("발명가형"),
    ESTJ("사업가형"),
    ESFJ("친선도모형"),
    ENFJ("언변능숙형"),
    ENTJ("지도자형");

    private final String name;

    MbtiUserType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
