package com.yibai.crown.base.plan.support;

import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

/**
 * 等额本息
 *
 * @author liaojunjun
 *
 */
public class AverageCapitalPlusInterest {

	public static List<RepaymentPlanDTO> getRepaymentPlan(BigDecimal amount, BigDecimal yearRate, Integer deadline,
			PeriodUnitEnum periodUnitEnum, Date startDate) {
		List<RepaymentPlanDTO> result = new ArrayList<RepaymentPlanDTO>();
		// 月利率： 年化利率/12
		BigDecimal monthRate = yearRate.divide(BigDecimal.valueOf(AmountHandleConstant.YEAR_OF_MONTH),
				MathContext.DECIMAL128);

		BigDecimal monthIncome = getPerMonthPrincipalInterest(amount, monthRate, deadline);
		Map<Integer, BigDecimal> capitalMap = getPerMonthCapital(amount, monthRate, deadline);
		Map<Integer, BigDecimal> interestMap = getPerMonthInterest(amount, monthRate, deadline);

		BigDecimal capitalTotal = BigDecimal.ZERO;
		
		for (int i = 1; i < deadline + 1; i++) {
			RepaymentPlanDTO repaymentPlanDTO = new RepaymentPlanDTO();
			repaymentPlanDTO.setCurrentPeriod(i);
			repaymentPlanDTO.setTotalPeriod(deadline);
			repaymentPlanDTO.setPrincipal(capitalMap.get(i));
			repaymentPlanDTO.setInterest(interestMap.get(i));
			repaymentPlanDTO.setPrincipalInterest(monthIncome);
			repaymentPlanDTO.setRefundDate(DateUtils.addDays(DateUtils.addMonths(startDate, i), -1));
			
			capitalTotal = capitalTotal.add(repaymentPlanDTO.getPrincipal());
			
			// 应还款本金不等于实际还款金额时 最后一期做本金抹平处理
			if (i == deadline) {
				repaymentPlanDTO.setPrincipalInterest(getLastPerMonthPrincipalInterest(amount, monthRate, deadline));
				
				if (amount.compareTo(capitalTotal) != 0) {
					repaymentPlanDTO.setPrincipal(capitalMap.get(i).add(amount.subtract(capitalTotal)));
					repaymentPlanDTO.setInterest(interestMap.get(i).subtract(amount.subtract(capitalTotal)));
					
					if(repaymentPlanDTO.getInterest().compareTo(BigDecimal.ZERO) < 0){
						repaymentPlanDTO.setInterest(BigDecimal.ZERO);
						repaymentPlanDTO.setPrincipalInterest(repaymentPlanDTO.getPrincipal());
					}
				}
			} 
			
			result.add(repaymentPlanDTO);
		}

		return result;
	}

	public static List<RepaymentPlanDTO> getBorrowerRepaymentPlan(BuildRepaymentDTO buildRepaymentDTO, List<RateConfigDTO> rateConfigDTOList) {
		List<RepaymentPlanDTO> result = new ArrayList<RepaymentPlanDTO>();

		// 月利率： 年化利率/12
		BigDecimal monthRate = buildRepaymentDTO.getYearRate().divide(BigDecimal.valueOf(AmountHandleConstant.YEAR_OF_MONTH),
				MathContext.DECIMAL128);

		// 担保服务费
		BigDecimal guaranteeServiceFeeTotal = buildRepaymentDTO.getAmount().multiply(buildRepaymentDTO.getGuaranteeRate())
				.setScale(AmountHandleConstant.CALCULATE_PRECISION_TWO, BigDecimal.ROUND_HALF_EVEN);
		BigDecimal guaranteeServiceFee = BigDecimal.ZERO;

		// 平台管理费
		BigDecimal platformManageFeeTotal = buildRepaymentDTO.getAmount().multiply(buildRepaymentDTO.getManageRate())
				.setScale(AmountHandleConstant.CALCULATE_PRECISION_TWO, BigDecimal.ROUND_HALF_EVEN);
		BigDecimal platformManageFee = BigDecimal.ZERO;

		BigDecimal monthIncome = getPerMonthPrincipalInterest(buildRepaymentDTO.getAmount(), monthRate,
				buildRepaymentDTO.getDeadline());
		Map<Integer, BigDecimal> capitalMap = getPerMonthCapital(buildRepaymentDTO.getAmount(), monthRate,
				buildRepaymentDTO.getDeadline());
		Map<Integer, BigDecimal> interestMap = getPerMonthInterest(buildRepaymentDTO.getAmount(), monthRate,
				buildRepaymentDTO.getDeadline());

		BigDecimal capitalTotal = BigDecimal.ZERO;

		for (int i = 1; i < buildRepaymentDTO.getDeadline() + 1; i++) {
			RepaymentPlanDTO repaymentPlanDTO = new RepaymentPlanDTO();
			repaymentPlanDTO.setCurrentPeriod(i);
			repaymentPlanDTO.setTotalPeriod(buildRepaymentDTO.getDeadline());
			repaymentPlanDTO.setPrincipal(capitalMap.get(i));
			repaymentPlanDTO.setInterest(interestMap.get(i));
			repaymentPlanDTO.setPrincipalInterest(monthIncome);
			repaymentPlanDTO.setRefundDate(DateUtils.addDays(
					DateUtils.addMonths(
							buildRepaymentDTO.getStartDate() == null ? new Date() : buildRepaymentDTO.getStartDate(), i), -1));

			repaymentPlanDTO.setGuaranteeServiceFee(BigDecimal.ZERO);
			repaymentPlanDTO.setPlatformManageFee(BigDecimal.ZERO);

			for (int j = 0; j < rateConfigDTOList.size(); j++) {
				RateConfigDTO projectRate = rateConfigDTOList.get(j);
				if (projectRate != null && projectRate.getCurrentPeriod() == i) {
					// 抹平管理费
					// projectRateList.size()为收费次数N 第N次收费用总收费减去前N-1次收费之和
					if (j == (rateConfigDTOList.size() - 1)) {
						repaymentPlanDTO.setGuaranteeServiceFee(guaranteeServiceFeeTotal.subtract(guaranteeServiceFee));
						repaymentPlanDTO.setPlatformManageFee(platformManageFeeTotal.subtract(platformManageFee));
					} else {
						repaymentPlanDTO.setGuaranteeServiceFee(guaranteeServiceFeeTotal.multiply(
								projectRate.getGuaranteeServiceFeePercent()).setScale(
								AmountHandleConstant.CALCULATE_PRECISION_TWO, BigDecimal.ROUND_HALF_EVEN));
						repaymentPlanDTO.setPlatformManageFee(platformManageFeeTotal.multiply(
								projectRate.getPlatformManageFeePercent()).setScale(
								AmountHandleConstant.CALCULATE_PRECISION_TWO, BigDecimal.ROUND_HALF_EVEN));

						guaranteeServiceFee = guaranteeServiceFee.add(repaymentPlanDTO.getGuaranteeServiceFee());
						platformManageFee = platformManageFee.add(repaymentPlanDTO.getPlatformManageFee());
					}
					break;
				}
			}

			capitalTotal = capitalTotal.add(repaymentPlanDTO.getPrincipal());

			// 应还款本金不等于实际还款金额时 最后一期做本金抹平处理
			if (i == buildRepaymentDTO.getDeadline()) {
				
				repaymentPlanDTO.setPrincipalInterest(getLastPerMonthPrincipalInterest(buildRepaymentDTO.getAmount(), monthRate, buildRepaymentDTO.getDeadline()));
				
				if (buildRepaymentDTO.getAmount().compareTo(capitalTotal) != 0) {
					repaymentPlanDTO.setPrincipal(capitalMap.get(i).add(buildRepaymentDTO.getAmount().subtract(capitalTotal)));
					repaymentPlanDTO.setInterest(interestMap.get(i).subtract(buildRepaymentDTO.getAmount().subtract(capitalTotal)));
					
					if(repaymentPlanDTO.getInterest().compareTo(BigDecimal.ZERO) < 0){
						repaymentPlanDTO.setInterest(BigDecimal.ZERO);
						repaymentPlanDTO.setPrincipalInterest(repaymentPlanDTO.getPrincipal());
					}
				}
			}

			repaymentPlanDTO.setRepaymentTotalAmount(repaymentPlanDTO.getPrincipalInterest()
					.add(repaymentPlanDTO.getGuaranteeServiceFee()).add(repaymentPlanDTO.getPlatformManageFee()));

			result.add(repaymentPlanDTO);
		}

		return result;
	}

	/**
	 * 等额本息计算每月偿还本金和利息
	 * <p>
	 * 公式：每月偿还本息=〔贷款本金×月利率×(1＋月利率)＾还款月数〕÷〔(1＋月利率)＾还款月数-1〕
	 *
	 * @param capital
	 *            本金
	 * @param monthRate
	 *            月利息
	 * @param totalPeriod
	 *            总期数
	 * @return
	 */
	private static BigDecimal getPerMonthPrincipalInterest(BigDecimal capital, BigDecimal monthRate, int totalPeriod) {
		BigDecimal dividend = capital.multiply(monthRate.multiply(BigDecimal.ONE.add(monthRate).pow(totalPeriod)));
		BigDecimal divisor = (BigDecimal.ONE.add(monthRate)).pow(totalPeriod).subtract(BigDecimal.ONE);
		BigDecimal result = dividend.divide(divisor, AmountHandleConstant.CALCULATE_PRECISION_TWO,
				BigDecimal.ROUND_HALF_EVEN);
		return result;
	}
	
	/**
	 * 计算最后一期偿还本金和利息([不做保留两位的每月还款 乘以 总期数] 减去 [保留两位的每月还款 乘以 (总期数 -1)]) 最后保留两位
	 * @param capital
	 * @param monthRate
	 * @param totalPeriod
	 * @return
	 */
	private static BigDecimal getLastPerMonthPrincipalInterest(BigDecimal capital, BigDecimal monthRate, int totalPeriod) {
		BigDecimal dividend = capital.multiply(monthRate.multiply(BigDecimal.ONE.add(monthRate).pow(totalPeriod)));
		BigDecimal divisor = (BigDecimal.ONE.add(monthRate)).pow(totalPeriod).subtract(BigDecimal.ONE);
		BigDecimal result = dividend.divide(divisor, MathContext.DECIMAL128);
		
		BigDecimal total = getPerMonthPrincipalInterest(capital, monthRate, totalPeriod).multiply(new BigDecimal(totalPeriod - 1)).setScale(AmountHandleConstant.CALCULATE_PRECISION_TWO,
				BigDecimal.ROUND_HALF_EVEN);
		result = result.multiply(new BigDecimal(totalPeriod)).subtract(total).setScale(AmountHandleConstant.CALCULATE_PRECISION_TWO,
				BigDecimal.ROUND_HALF_EVEN);
		return result;
	}

	/**
	 * 等额本息计每月偿还利息
	 * <p>
	 * 公式：每月偿还利息=贷款本金×月利率×〔(1+月利率)^还款期数-(1+月利率)^(还款月序号-1)〕÷〔(1+月利率)^还款期数-1〕
	 *
	 * @param capital
	 *            总借款额（贷款本金）
	 * @param monthRate
	 *            月利率
	 * @param totalPeriod
	 *            总期数
	 * @return 每月偿还利息
	 */
	private static Map<Integer, BigDecimal> getPerMonthInterest(BigDecimal capital, BigDecimal monthRate, int totalPeriod) {
		Map<Integer, BigDecimal> map = new HashMap<Integer, BigDecimal>();
		BigDecimal monthInterest;
		for (int i = 1; i < totalPeriod + 1; i++) {
			BigDecimal multiply = capital.multiply(monthRate);
			BigDecimal sub = (BigDecimal.ONE.add(monthRate).pow(totalPeriod)).subtract(BigDecimal.ONE.add(monthRate)
					.pow(i - 1));
			monthInterest = multiply.multiply(sub).divide(
					(BigDecimal.ONE.add(monthRate).pow(totalPeriod).subtract(BigDecimal.ONE)), MathContext.DECIMAL128);
			monthInterest = monthInterest.setScale(AmountHandleConstant.CALCULATE_PRECISION_TWO,
					BigDecimal.ROUND_HALF_EVEN);
			map.put(i, monthInterest);
		}
		return map;
	}

	/**
	 * 等额本息计算每月偿还本金
	 *
	 * @param capital
	 *            总借款额（贷款本金）
	 * @param monthRate
	 *            月利率
	 * @param totalPeriod
	 *            还款期数
	 * @return 每月偿还本金
	 */
	private static Map<Integer, BigDecimal> getPerMonthCapital(BigDecimal capital, BigDecimal monthRate, int totalPeriod) {
		BigDecimal monthIncome = getPerMonthPrincipalInterest(capital, monthRate, totalPeriod);
		Map<Integer, BigDecimal> mapInterest = getPerMonthInterest(capital, monthRate, totalPeriod);

		Map<Integer, BigDecimal> mapPrincipal = new HashMap<Integer, BigDecimal>();
		for (Map.Entry<Integer, BigDecimal> entry : mapInterest.entrySet()) {
			if(entry.getKey() < mapInterest.entrySet().size()){
				mapPrincipal.put(entry.getKey(), monthIncome.subtract(entry.getValue()));
			} else {
				mapPrincipal.put(entry.getKey(),
						getLastPerMonthPrincipalInterest(capital, monthRate, totalPeriod).subtract(entry.getValue()));
			}
		}
		return mapPrincipal;
	}
	
}
