package com.yibai.crown.base.plan.support;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 构建还款方式参数DTO
 * @author liaojunjun
 *
 */
@Setter
@Getter
public class BuildRepaymentDTO {

	/**
	 * 还款方式(必填)
	 */
	private RepaymentTypeEnum repaymentType;

    /**
     * 标的类型(必填)
     */
    private Byte projectType;
	
	/**
	 * 金额(必填)
	 */
	private BigDecimal amount;
	
	/**
	 * 年利率(必填)
	 */
	private BigDecimal yearRate;

	/**
	 * 期限(必填)
	 */
	private Integer deadline;

	/**
	 * 期限单位(必填)
	 */
	private PeriodUnitEnum periodUnit;
	
	/**
	 * 平台管理费率(必填)
	 */
	private BigDecimal manageRate;

	/**
	 * 担保费率(必填)
	 */
	private BigDecimal guaranteeRate;

	/**
	 * 开始日期
	 */
	private Date startDate;
}
