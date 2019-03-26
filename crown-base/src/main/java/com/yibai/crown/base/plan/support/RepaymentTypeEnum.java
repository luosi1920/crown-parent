package com.yibai.crown.base.plan.support;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 还款方式
 * @author liaojunjun
 *
 */
@AllArgsConstructor
@Getter
public enum RepaymentTypeEnum {
	REPAY_PRINCIPAL_INTEREST_AT_TERM((byte)1, "到期还本息"),
    AVERAGE_CAPITAL_PLUS_INTEREST ((byte)2, "等额本息"),
    MONTHLY_INTEREST_PAYMENT_DUE((byte)3, "按月还息，到期还本");

    private Byte value;
    private String desc;

    public static RepaymentTypeEnum enumOf(int value) {
        for (RepaymentTypeEnum retEnum : values()) {
            if (retEnum.value == value) {
                return retEnum;
            }
        }
        return null;
    }
}
