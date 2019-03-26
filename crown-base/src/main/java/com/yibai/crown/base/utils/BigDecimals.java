package com.yibai.crown.base.utils;

import org.springframework.util.Assert;

import java.math.BigDecimal;

/**
 * 2017/11/22.
 */
public class BigDecimals {

    public static BigDecimal max(BigDecimal arg1, BigDecimal arg2, BigDecimal... others) {
        Assert.notNull(arg1, "arg1 can not be null");
        Assert.notNull(arg2, "arg2 can not be null");
        Assert.noNullElements(others, "others can not contains null");
        BigDecimal max = arg1.max(arg2);
        for (BigDecimal other : others) {
            if (other.compareTo(max) > 0) {
                max = other;
            }
        }
        return max;
    }

    public static BigDecimal min(BigDecimal arg1, BigDecimal arg2, BigDecimal... others) {
        Assert.notNull(arg1, "arg1 can not be null");
        Assert.notNull(arg2, "arg2 can not be null");
        Assert.noNullElements(others, "others can not contains null");
        BigDecimal min = arg1.min(arg2);
        for (BigDecimal other : others) {
            if (other.compareTo(min) < 0) {
                min = other;
            }
        }
        return min;
    }

    public static BigDecimal multiply100(BigDecimal decimal) {
        Assert.notNull(decimal, "decimal can not be null");
        return decimal.multiply(BigDecimal.valueOf(100));
    }

	/**
	 * 格式化利率(乘100，去掉末尾的0)
	 *
	 * @param decimal
	 * @return
	 */
	public static String formatBorrowRate(BigDecimal decimal) {
		Assert.notNull(decimal, "decimal can not be null");
		return decimal.multiply(new BigDecimal(100)).stripTrailingZeros().toPlainString();
	}

}
