package com.yibai.crown.base.plan.support;

import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 到期本息
 *
 * @author liaojunjun
 *
 */
public class RepayPrincipalInterestAtTerm {

	private static final int CURRENT_PERIOD = 1;

	private static final int TOTAL_PERIOD = CURRENT_PERIOD;

	public static List<RepaymentPlanDTO> getRepaymentPlan(BigDecimal amount, BigDecimal yearRate, Integer deadline,
			PeriodUnitEnum periodUnitEnum, Date startDate) {
		List<RepaymentPlanDTO> result = new ArrayList<RepaymentPlanDTO>();
		Date refundDate = null;
		BigDecimal monthRate = null;

		switch (periodUnitEnum) {
		case MONTH:
			refundDate = DateUtils.addDays(DateUtils.addMonths(startDate, deadline), -1);

			// 月利率： 年化利率/12
			monthRate = yearRate.divide(BigDecimal.valueOf(AmountHandleConstant.YEAR_OF_MONTH), MathContext.DECIMAL128);
			break;

		case DAY:
			refundDate = DateUtils.addDays(startDate, deadline);

			// 日利率： 年化利率/365
			monthRate = yearRate.divide(BigDecimal.valueOf(AmountHandleConstant.YEAR_OF_DAY), MathContext.DECIMAL128);
			break;

		default:
			break;
		}

		RepaymentPlanDTO repaymentPlanDTO = new RepaymentPlanDTO();
		repaymentPlanDTO.setCurrentPeriod(TOTAL_PERIOD);
		repaymentPlanDTO.setTotalPeriod(TOTAL_PERIOD);
		repaymentPlanDTO.setInterest(amount.multiply(monthRate)
				.multiply(BigDecimal.valueOf(deadline)).setScale(AmountHandleConstant.CALCULATE_PRECISION_TWO,
						BigDecimal.ROUND_HALF_EVEN));
		repaymentPlanDTO.setRefundDate(refundDate);
		repaymentPlanDTO.setPrincipal(amount);
		repaymentPlanDTO.setPrincipalInterest(repaymentPlanDTO.getPrincipal().add(repaymentPlanDTO.getInterest()));

		result.add(repaymentPlanDTO);

		return result;
	}

	public static List<RepaymentPlanDTO> getBorrowerRepaymentPlan(BuildRepaymentDTO buildRepaymentDTO, List<RateConfigDTO> rateConfigDTOList) {
		// 担保服务费
		BigDecimal guaranteeServiceFeeTotal = buildRepaymentDTO.getAmount().multiply(buildRepaymentDTO.getGuaranteeRate())
				.setScale(AmountHandleConstant.CALCULATE_PRECISION_TWO, BigDecimal.ROUND_HALF_EVEN);

		// 平台管理费
		BigDecimal platformManageFeeTotal = buildRepaymentDTO.getAmount().multiply(buildRepaymentDTO.getManageRate())
				.setScale(AmountHandleConstant.CALCULATE_PRECISION_TWO, BigDecimal.ROUND_HALF_EVEN);

		List<RepaymentPlanDTO> result = new ArrayList<RepaymentPlanDTO>();

		PeriodUnitEnum periodUnit = buildRepaymentDTO.getPeriodUnit();
		Date refundDate = null;
		BigDecimal monthRate = null;

		switch (periodUnit) {
			case MONTH:
				refundDate = DateUtils.addDays(
						DateUtils.addMonths(
								buildRepaymentDTO.getStartDate() == null ? new Date() : buildRepaymentDTO.getStartDate(),
								buildRepaymentDTO.getDeadline()), -1);

				// 月利率： 年化利率/12
				monthRate = buildRepaymentDTO.getYearRate().divide(BigDecimal.valueOf(AmountHandleConstant.YEAR_OF_MONTH),
						MathContext.DECIMAL128);
				break;

			case DAY:
				refundDate = DateUtils.addDays(
						buildRepaymentDTO.getStartDate() == null ? new Date() : buildRepaymentDTO.getStartDate(),
						buildRepaymentDTO.getDeadline());

				// 日利率： 年化利率/365
				monthRate = buildRepaymentDTO.getYearRate().divide(BigDecimal.valueOf(AmountHandleConstant.YEAR_OF_DAY),
						MathContext.DECIMAL128);
				break;

			default:
				break;
		}

		RepaymentPlanDTO repaymentPlanDTO = new RepaymentPlanDTO();
		repaymentPlanDTO.setCurrentPeriod(TOTAL_PERIOD);
		repaymentPlanDTO.setTotalPeriod(TOTAL_PERIOD);
        repaymentPlanDTO.setInterest(buildRepaymentDTO.getAmount().multiply(monthRate)
                .multiply(BigDecimal.valueOf(buildRepaymentDTO.getDeadline())).setScale(
                        AmountHandleConstant.CALCULATE_PRECISION_TWO, BigDecimal.ROUND_HALF_EVEN));
		repaymentPlanDTO.setRefundDate(refundDate);
		repaymentPlanDTO.setPrincipal(buildRepaymentDTO.getAmount());
		repaymentPlanDTO.setPrincipalInterest(repaymentPlanDTO.getPrincipal().add(repaymentPlanDTO.getInterest()));

		repaymentPlanDTO.setGuaranteeServiceFee(guaranteeServiceFeeTotal);
		repaymentPlanDTO.setPlatformManageFee(platformManageFeeTotal);

		repaymentPlanDTO.setRepaymentTotalAmount(repaymentPlanDTO.getPrincipalInterest()
				.add(repaymentPlanDTO.getGuaranteeServiceFee()).add(repaymentPlanDTO.getPlatformManageFee()));

		result.add(repaymentPlanDTO);
		return result;
	}

}
