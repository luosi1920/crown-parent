package com.yibai.crown.base.plan.support;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by lujing on 2017/11/16.
 */
@Setter
@Getter
public class RepaymentPlanDTO implements Serializable {
	private static final long serialVersionUID = -6375935307349549968L;

	/**
	 * 当前期数
	 */
	private Integer currentPeriod;

	/**
	 * 总期数
	 */
	private Integer totalPeriod;

	/**
	 * 还款日期
	 */
	private Date refundDate;

	/**
	 * 本金
	 */
	private BigDecimal principal;
	/**
	 * 利息
	 */
	private BigDecimal interest;
	/**
	 * 本息
	 */
	private BigDecimal principalInterest;

	/**
	 * 平台管理费
	 */
	private BigDecimal platformManageFee;

	/**
	 * 担保服务费
	 */
	private BigDecimal guaranteeServiceFee;

	/**
	 * 每月还款总额(本金+利息+担保服务费+平台管理费)
	 */
	private BigDecimal repaymentTotalAmount;
}
