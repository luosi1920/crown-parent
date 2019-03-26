package com.yibai.crown.base.plan;

import com.yibai.crown.base.plan.support.*;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class RepaymentPlanContext {

	/**
	 * 生成还款回款计划
	 * 
	 * @param repaymentType
	 *            还款方式
	 * @param projectType
	 *            标的类型
	 * @param amount
	 *            金额
	 * @param yearRate
	 *            年利率
	 * @param deadline
	 *            期限
	 * @param periodUnitEnum
	 *            期限单位
	 * @param startDate
	 *            开始日期
	 * @return
	 */
	public static List<RepaymentPlanDTO> getRepaymentPlan(RepaymentTypeEnum repaymentType, Byte projectType, BigDecimal amount,
			BigDecimal yearRate, Integer deadline, PeriodUnitEnum periodUnitEnum, Date startDate) {

		InterestAccrualEnum interestAccrualEnum = getInterestAccrualEnum(repaymentType, projectType);

		switch (interestAccrualEnum) {
		case REPAY_PRINCIPAL_INTEREST_AT_TERM:
			return RepayPrincipalInterestAtTerm.getRepaymentPlan(amount, yearRate, deadline, periodUnitEnum, startDate);
		case AVERAGE_CAPITAL_PLUS_INTEREST:
			return AverageCapitalPlusInterest.getRepaymentPlan(amount, yearRate, deadline, periodUnitEnum, startDate);

		case MONTHLY_INTEREST_PAYMENT_DUE:
			return MonthlyInterestPaymentDue.getRepaymentPlan(amount, yearRate, deadline, periodUnitEnum, startDate);

		case TU_BA_TU_CALCULATE_INTEREST_BY_DAY:
			return ToBaToCalculateInterestByDay.getRepaymentPlan(amount, yearRate, deadline, periodUnitEnum, startDate);
		default:
			break;
		}
		return null;
	}

	/**
	 * 根据还款方式、年利率、金额、期限、期限单位、管理费率、担保费率和费率配置信息生成还款计划
	 * 开始如期为null使用当前日期
	 *
	 * @param buildRepaymentDTO
	 *            标的信息
	 * @param rateConfigDTOList
	 *            费率配置
	 * @return
	 */
	public static List<RepaymentPlanDTO> getBorrowerRepaymentPlan(BuildRepaymentDTO buildRepaymentDTO, List<RateConfigDTO> rateConfigDTOList) {
		Assert.notNull(buildRepaymentDTO, "getBorrowerRepaymentPlan rateConfigDTOList is null.");
		Assert.notEmpty(rateConfigDTOList, "getBorrowerRepaymentPlan rateConfigDTOList is null.");

		InterestAccrualEnum interestAccrualEnum = getInterestAccrualEnum(buildRepaymentDTO.getRepaymentType(), buildRepaymentDTO.getProjectType());

		switch (interestAccrualEnum) {
			case REPAY_PRINCIPAL_INTEREST_AT_TERM:
				return RepayPrincipalInterestAtTerm.getBorrowerRepaymentPlan(buildRepaymentDTO, rateConfigDTOList);
			case AVERAGE_CAPITAL_PLUS_INTEREST:
				return AverageCapitalPlusInterest.getBorrowerRepaymentPlan(buildRepaymentDTO, rateConfigDTOList);

			case MONTHLY_INTEREST_PAYMENT_DUE:
				return MonthlyInterestPaymentDue.getBorrowerRepaymentPlan(buildRepaymentDTO, rateConfigDTOList);

			case TU_BA_TU_CALCULATE_INTEREST_BY_DAY:
				return ToBaToCalculateInterestByDay.getBorrowerRepaymentPlan(buildRepaymentDTO, rateConfigDTOList);
			default:
				break;
		}
		return null;

	}

	private static InterestAccrualEnum getInterestAccrualEnum(RepaymentTypeEnum repaymentType, Byte projectType) {
		if(Byte.valueOf("12").equals(projectType)){
			return InterestAccrualEnum.TU_BA_TU_CALCULATE_INTEREST_BY_DAY;
		} else {
			switch (repaymentType) {
				case REPAY_PRINCIPAL_INTEREST_AT_TERM:
					return InterestAccrualEnum.REPAY_PRINCIPAL_INTEREST_AT_TERM;
				case AVERAGE_CAPITAL_PLUS_INTEREST:
					return InterestAccrualEnum.AVERAGE_CAPITAL_PLUS_INTEREST;

				case MONTHLY_INTEREST_PAYMENT_DUE:
					return InterestAccrualEnum.MONTHLY_INTEREST_PAYMENT_DUE;

				default:
					throw new UnsupportedOperationException("RepaymentTypeEnum:" + repaymentType);
			}
		}
	}
	
}
