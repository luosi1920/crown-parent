package com.yibai.crown.base.plan.support;

import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 按月付息,到期还本
 *
 * @author liaojunjun
 *
 */
public class MonthlyInterestPaymentDue {

	public static List<RepaymentPlanDTO> getRepaymentPlan(BigDecimal amount, BigDecimal yearRate, Integer deadline,
			PeriodUnitEnum periodUnit, Date startDate) {
		List<RepaymentPlanDTO> result = new ArrayList<RepaymentPlanDTO>();
		// 月利率： 年化利率/12
		BigDecimal monthRate = yearRate.divide(BigDecimal.valueOf(AmountHandleConstant.YEAR_OF_MONTH),
				MathContext.DECIMAL128);

		for (int i = 1; i < deadline + 1; i++) {
			RepaymentPlanDTO repaymentPlanDTO = new RepaymentPlanDTO();
			repaymentPlanDTO.setCurrentPeriod(i);
			repaymentPlanDTO.setTotalPeriod(deadline);
			repaymentPlanDTO.setInterest(monthRate.multiply(amount).setScale(
					AmountHandleConstant.CALCULATE_PRECISION_TWO, BigDecimal.ROUND_HALF_EVEN));

			repaymentPlanDTO.setRefundDate(DateUtils.addDays(DateUtils.addMonths(startDate, i), -1));
			if (i == deadline) {
				repaymentPlanDTO.setPrincipal(amount);
			} else {
				repaymentPlanDTO.setPrincipal(BigDecimal.ZERO);
			}
			repaymentPlanDTO.setPrincipalInterest(repaymentPlanDTO.getPrincipal().add(repaymentPlanDTO.getInterest()));

			result.add(repaymentPlanDTO);
		}

		return result;
	}

	public static List<RepaymentPlanDTO> getBorrowerRepaymentPlan(BuildRepaymentDTO project, List<RateConfigDTO> rateConfigDTOList) {
		List<RepaymentPlanDTO> result = new ArrayList<RepaymentPlanDTO>();

		// 月利率： 年化利率/12
		BigDecimal monthRate = project.getYearRate().divide(BigDecimal.valueOf(AmountHandleConstant.YEAR_OF_MONTH),
				MathContext.DECIMAL128);

		// 担保服务费
		BigDecimal guaranteeServiceFeeTotal = project.getAmount().multiply(project.getGuaranteeRate())
				.setScale(AmountHandleConstant.CALCULATE_PRECISION_TWO, BigDecimal.ROUND_HALF_EVEN);
		BigDecimal guaranteeServiceFee = BigDecimal.ZERO;

		// 平台管理费
		BigDecimal platformManageFeeTotal = project.getAmount().multiply(project.getManageRate())
				.setScale(AmountHandleConstant.CALCULATE_PRECISION_TWO, BigDecimal.ROUND_HALF_EVEN);
		BigDecimal platformManageFee = BigDecimal.ZERO;

		for (int i = 1; i < project.getDeadline() + 1; i++) {
			RepaymentPlanDTO repaymentPlanDTO = new RepaymentPlanDTO();
			repaymentPlanDTO.setCurrentPeriod(i);
			repaymentPlanDTO.setTotalPeriod(project.getDeadline());
			repaymentPlanDTO.setInterest(monthRate.multiply(project.getAmount()).setScale(
					AmountHandleConstant.CALCULATE_PRECISION_TWO, BigDecimal.ROUND_HALF_EVEN));

			repaymentPlanDTO.setRefundDate(DateUtils.addDays(
					DateUtils.addMonths(
							project.getStartDate() == null ? new Date() : project.getStartDate(), i), -1));
			if (i == project.getDeadline()) {
				repaymentPlanDTO.setPrincipal(project.getAmount());
			} else {
				repaymentPlanDTO.setPrincipal(BigDecimal.ZERO);
			}
			repaymentPlanDTO.setPrincipalInterest(repaymentPlanDTO.getPrincipal().add(repaymentPlanDTO.getInterest()));

			repaymentPlanDTO.setGuaranteeServiceFee(BigDecimal.ZERO);
			repaymentPlanDTO.setPlatformManageFee(BigDecimal.ZERO);

			for (int j = 0; j < rateConfigDTOList.size(); j++) {
				RateConfigDTO rateConfigDTO = rateConfigDTOList.get(j);
				if (rateConfigDTO != null && rateConfigDTO.getCurrentPeriod() == i) {
					// 抹平管理费
					// projectRateList.size()为收费次数N 第N次收费用总收费减去前N-1次收费之和
					if (j == (rateConfigDTOList.size() - 1)) {
						repaymentPlanDTO.setGuaranteeServiceFee(guaranteeServiceFeeTotal.subtract(guaranteeServiceFee));
						repaymentPlanDTO.setPlatformManageFee(platformManageFeeTotal.subtract(platformManageFee));
					} else {
						repaymentPlanDTO.setGuaranteeServiceFee(guaranteeServiceFeeTotal.multiply(
								rateConfigDTO.getGuaranteeServiceFeePercent()).setScale(
								AmountHandleConstant.CALCULATE_PRECISION_TWO, BigDecimal.ROUND_HALF_EVEN));
						repaymentPlanDTO.setPlatformManageFee(platformManageFeeTotal.multiply(
								rateConfigDTO.getPlatformManageFeePercent()).setScale(
								AmountHandleConstant.CALCULATE_PRECISION_TWO, BigDecimal.ROUND_HALF_EVEN));

						guaranteeServiceFee = guaranteeServiceFee.add(repaymentPlanDTO.getGuaranteeServiceFee());
						platformManageFee = platformManageFee.add(repaymentPlanDTO.getPlatformManageFee());
					}
					break;
				}
			}

			repaymentPlanDTO.setRepaymentTotalAmount(repaymentPlanDTO.getPrincipalInterest()
					.add(repaymentPlanDTO.getGuaranteeServiceFee()).add(repaymentPlanDTO.getPlatformManageFee()));

			result.add(repaymentPlanDTO);
		}

		return result;
	}

}
