package com.ball.biz.bet.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author lhl
 * @date 2022/11/15 下午6:01
 */
@Getter
@AllArgsConstructor
public enum OddsOutrightsType {
    EMPTY(-1),

    OUTRIGHT(1),
    THE_FINAL(2),
    SEMI_FINALS(3),
    QUARTER_FINALS(4),
    TOP_GOALSCORER(5),
    WINNER_OF_THE_GROUP_A(6, "A"),
    WINNER_OF_THE_GROUP_B(7, "B"),
    WINNER_OF_THE_GROUP_C(8, "C"),
    WINNER_OF_THE_GROUP_D(9, "D"),
    WINNER_OF_THE_GROUP_E(10, "E"),
    WINNER_OF_THE_GROUP_F(11, "F"),
    WINNER_OF_THE_GROUP_G(12, "G"),
    WINNER_OF_THE_GROUP_H(13, "H"),
    QUALIFIED_FROM_GROUP_A(14, "A"),
    QUALIFIED_FROM_GROUP_B(15, "B"),
    QUALIFIED_FROM_GROUP_C(16, "C"),
    QUALIFIED_FROM_GROUP_D(17, "D"),
    QUALIFIED_FROM_GROUP_E(18, "E"),
    QUALIFIED_FROM_GROUP_F(19, "F"),
    QUALIFIED_FROM_GROUP_G(20, "G"),
    QUALIFIED_FROM_GROUP_H(21, "H")
    ;

    private Integer code;
    private String group;

    OddsOutrightsType(Integer code) {
        this.code = code;
    }

    public static OddsOutrightsType parse(Integer code) {
        for (OddsOutrightsType e : values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return EMPTY;
    }

    public static List<Integer> groupTypes() {
        return Stream.of(values()).filter(e -> e.getGroup() != null).map(OddsOutrightsType::getCode).collect(Collectors.toList());
    }
}
