package com.yibai.crown.base.plan.support;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 计息方式
 * @author liaojunjun
 *
 */
@AllArgsConstructor
@Getter
public enum InterestAccrualEnum {
	REPAY_PRINCIPAL_INTEREST_AT_TERM((byte)1, "到期还本息"),
    AVERAGE_CAPITAL_PLUS_INTEREST ((byte)2, "等额本息"),
    MONTHLY_INTEREST_PAYMENT_DUE((byte)3, "按月还息，到期还本"),
	TU_BA_TU_CALCULATE_INTEREST_BY_DAY((byte)4, "土巴兔按日计算利息");

    private Byte value;
    private String desc;

	public static InterestAccrualEnum enumOf(Byte value) {
		for (InterestAccrualEnum retEnum : values()) {
			if (retEnum.value.equals(value)) {
				return retEnum;
			}
		}
		throw new UnsupportedOperationException("RepaymentTypeEnum:" + value);
	}
}
