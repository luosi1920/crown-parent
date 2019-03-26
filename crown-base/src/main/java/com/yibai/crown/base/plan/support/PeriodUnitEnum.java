package com.yibai.crown.base.plan.support;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 2017/11/8.
 */
@Getter
@AllArgsConstructor
public enum PeriodUnitEnum {
    MONTH((byte) 1, "月"),
    DAY((byte) 2, "天");

    private final Byte value;
    private final String desc;

    public static PeriodUnitEnum enumOf(Byte value) {
        for (PeriodUnitEnum unitEnum : values()) {
            if (unitEnum.value.equals(value)) {
                return unitEnum;
            }
        }
        throw new RuntimeException("PeriodUnitEnum:" + value);
    }

}
