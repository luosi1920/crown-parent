package com.yibai.crown.base.plan.support;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 费率配置
 * @author liaojunjun
 *
 */
@Getter
@Setter
@ToString
public class RateConfigDTO {

	/**
	 * 当前期数
	 */
	private Integer currentPeriod;

	/**
	 * 担保服务费率比例
	 */
	private BigDecimal guaranteeServiceFeePercent;

	/**
	 * 平台管理费率比例
	 */
	private BigDecimal platformManageFeePercent;
}
