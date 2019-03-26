package com.yibai.crown.base.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtils {
	private static final Logger logger= LoggerFactory.getLogger(DateUtils.class);
	public static final String DATETIME_FORMAT = "yyyyMMddHHmmss";
	public static final String DATETIME_FORMAT_UU = "yyyyMMddHHmmssSSSuuu";
	public static final String DATE_FORMAT = "yyyyMMdd";
	public static final String SHOW_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String SHOW_DATE_FORMAT = "yyyy-MM-dd";
	public static final String SHOW_TIME_FORMAT = "HH:mm";
	public static final String SHOW_TIMESEC_FORMAT = "HH:mm:ss";
	public static final String SHOW_US_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";

	public static final String SHOW_DATETIME_MILLISECOND ="yyyy-MM-dd HH:mm:ss.SSS";

	public static final String SHOW_MONTH_DATE_HOUR_SECOND ="MM-dd HH:mm";

	public static final String SHOW_YEAR_MONTH_DATE_HOUR_SECOND ="yyyy-MM-dd HH:mm";

	public static final String DATETIME_FOMAT_FF = "yyyyMMddHHmmssFFF";

	public static final String DATETIME_FOMAT_MU = "yyyymmddHHMMSSmmmuuu";

	public static final String DATETIME_FOMAT_MS = "yyyyMMddHHmmssSSS";

	public static final String DATETIME_FOMAT_CHINESE = "MM月dd日HH:mm";

	public static final String SHOW_DATE_FORMAT_DD_MM_YYYYY = "dd-MM-yyyy";

	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat df3 = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat df4 = new SimpleDateFormat(DATETIME_FORMAT);
	private static SimpleDateFormat df5 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private static SimpleDateFormat df6 = new SimpleDateFormat("MMdd");
	private static SimpleDateFormat df7 = new SimpleDateFormat("MM");
	private static SimpleDateFormat df8 = new SimpleDateFormat("dd");


	private static Map<String, DateFormat> dfMap = new HashMap<String, DateFormat>();
	{
		dfMap.put("yyyy-MM-dd", df);
		dfMap.put("yyyy-MM-dd HH:mm:ss", df2);
		dfMap.put(DATETIME_FORMAT, df4);
		dfMap.put("yyyy-MM-dd", df);
		dfMap.put("yyyy-MM-dd HH:mm", df5);
		dfMap.put("MMdd", df6);
	}

	/**
	 * 获取当前时间串，格式为：yyyymmddHHMiSS
	 *
	 * @return
	 */
	public static final String getCurrDatetime() {
		return formatDate(new Date(), DATETIME_FORMAT);
	}

	public static final String formatDateTimeWithNum(Date date){
		if(date == null){
			return null;
		}
		return df4.format(date);
	}

	public static final String getMonth(Date date){
		if(date == null){
			return null;
		}
		return df7.format(date);
	}

	public static final String getDay(Date date){
		if(date == null){
			return null;
		}
		return df8.format(date);
	}

	/**
	 * 获取当前日期串，格式为yyyymmdd
	 *
	 * @return
	 */
	public static final String getCurrDate() {
		return formatDate(new Date(), DATE_FORMAT);
	}

	/**
	 * 获取当前日期串，格式为yyyymmdd
	 *
	 * @return
	 */
	public static final String getCurrDateNew() {
		return formatDate(new Date(), SHOW_DATE_FORMAT);
	}
	/**
	 * 获取指定日期日期串，格式为yyyymmdd
	 *
	 * @return
	 */
	public static final String getGivenDate(Date date) {
		return formatDate(date, DATE_FORMAT);
	}

	/**
	 * 获取当前日期串，格式为yyyy-MM-dd HH:mm:ss
	 *
	 * @return
	 */
	public static final String getCurrDateTime() {
		return formatDate(new Date(), SHOW_DATETIME_FORMAT);
	}

	public static final Timestamp getSystemTime() {
		return new Timestamp(new Date().getTime());
	}

	/**
	 * @param date
	 *            时间
	 * @param formatStr
	 *            格式化串
	 * @return
	 */
	public static String format(Date date, String formatStr) {
		if (null == date) {
			return "";
		}
		if(StringUtils.isEmpty(formatStr)){
			return "";
		}
		DateFormat sdf = dfMap.get(formatStr);
		if(sdf==null){
			sdf = new SimpleDateFormat(formatStr);
		}

		return sdf.format(date);
	}

	/**
	 * 计算剩余时间
	 * @Title: getRemainingTime
	 * @Description:
	 * @param d1 当前时间
	 * @param d2 （担保完成时间、尽调完成时间等）
	 * @return String    返回类型
	 * @throws
	 */
	public static String getRemainingTime(Date d1, Date d2){
		if(d1==null||d2==null){
			return "";
		}
		if(d2.before(d1)){
			return "已完成";
		}
		Calendar c = Calendar.getInstance();
		c.setTime(d1);
		long now = c.getTimeInMillis();
		c.setTime(d2);
		long diff = c.getTimeInMillis()-now;
		String result = "";
		if(diff<0){
			return "已完成";
		}

		long[] ls =getDistanceTimes(d1, d2);

		int day = (int) ls[0];
		int h = (int) ls[1];
		int min = (int) ls[2];
		int sec = (int) ls[3];
		if(day>=2){
			result+=day+"天";
		}else if(day>=1){
			result+=day+"天"+h+"小时";
		}else if(h>=1){
			result += h+"小时"+min+"分钟";
		}else if(min>=1){
			result+=min+"分钟";
		}else if (sec>0){
			result+="1分钟";
		}
		result+="后截止";
		return result;
	}

	/**
	 * 计算剩余时间
	 * @Title: getRemainingTime
	 * @Description:
	 * @param d1 当前时间
	 * @param d2 （担保完成时间、尽调完成时间等）
	 * @return String    返回类型
	 */
	public static String getMicroGuaranteeRemainingTime(Date d1, Date d2){
		if(d1==null||d2==null){
			return "";
		}
		if(d2.before(d1)){
			return "已完成";
		}
		Calendar c = Calendar.getInstance();
		c.setTime(d1);
		long now = c.getTimeInMillis();
		c.setTime(d2);
		long diff = c.getTimeInMillis()-now;
		String result = "剩余";
		if(diff<0){
			return "已完成";
		}
		long[] ls =getDistanceTimes(d1, d2);

		int day = (int) ls[0];
		int h = (int) ls[1];
		int min = (int) ls[2];
		int sec = (int) ls[3];
		if(day>=2){
			result+=day+"天";
		}else if(day>=1){
			result+=day+"天"+h+"小时";
		}else if(h>=1){
			result += h+"小时"+min+"分钟";
		}else if(min>=1){
			result+=min+"分钟";
		}else if (sec>0){
			result+="1分钟";
		}
		return result;
	}


	/**
	 * 计算剩余时间 我的借款详情使用
	 * @Title: getRemainingTime
	 * @Description:
	 * @param d1 当前时间
	 * @param d2 （担保完成时间、尽调完成时间等）
	 * @return String    返回类型
	 */
	public static String getRemainingTimeForLoanDetail(Date d1, Date d2){
		if(d1==null||d2==null){
			return "";
		}
		if(d2.before(d1)){
			return "已截止";
		}
		Calendar c = Calendar.getInstance();
		c.setTime(d1);
		long now = c.getTimeInMillis();
		c.setTime(d2);
		long diff = c.getTimeInMillis()-now;
		String result = "";
		if(diff<0){
			return "已截止";
		}

		long[] ls =getDistanceTimes(d1, d2);

		int day = (int) ls[0];
		int h = (int) ls[1];
		int min = (int) ls[2];
		int sec = (int) ls[3];
		if(day>=2){
			result+=day+"天";
		}else if(day>=1){
			result+=day+"天"+h+"小时";
		}else if(h>=1){
			result += h+"小时"+min+"分钟";
		}else if(min>=1){
			result+=min+"分钟";
		}else if (sec>0){
			result+="1分钟";
		}
		result+="后截止";
		return result;
	}
	/**
	 *
	 * @Title: getRemainingTime2
	 * @Description: 计算逾期天数
	 * @param n
	 * @param d
	 * @return String    返回类型
	 */
	public static String getRemainingTime2(Date d, Date n){
		if(n==null||d==null){
			return "";
		}
		if(d.before(n)){
			return "";
		}
		Calendar c = Calendar.getInstance();
		c.setTime(n);
		long now = c.getTimeInMillis();
		c.setTime(d);
		long diff = c.getTimeInMillis()-now;
		String result = "";
		//天
		int day = (int) (diff/(1000*60*60*24));
		//小时
		int h = (int) (diff/(1000*60*60));
		result+=day+"天";
		result+= h%24+"小时";

		return result.replaceAll("0天", "").replace("0小时", "");
	}


	/**
	 *
	 * @Title: getRemainingTime
	 * @Description:根据毫秒值计算时间
	 * @param l
	 * @return String    返回类型
	 */
	public static String getRemainingTime(long l){
		String result = "";
		//天
		if(l<0) {
			return "已过期";
		}
		int day = (int) (l/(1000*60*60*24));
		//小时
		int h = (int) (l/(1000*60*60));
		result+=day+"天";
		result+= h%24+"小时";
		return result.replaceAll("0天", "").replaceAll("0小时", "");
	}

	/**
	 * @Title: isToday
	 * @Description: 判断日期是否是当天
	 * @param d
	 * @return boolean    返回类型
	 */
	public static boolean isToday(Date d){
		try{
			return df.format(new Date()).equals(df.format(d));
		}catch(Exception e){
			//
		}
		return false;
	}
	/**
	 * @Title: isBeforeNow
	 * @Description: 判断日期是否在__秒以前
	 * @param d
	 * @param seconds
	 * @return boolean    返回类型
	 */
	public static boolean isBeforeNow(Date d, int seconds){
		try{
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			long dd = c.getTimeInMillis();
			c.setTime(new Date());
			long now = c.getTimeInMillis();
			return (now-dd)/1000<=seconds;
		}catch(Exception e){
			logger.error("isBeforeNow error, the reason is : {}",e);
		}
		return false;
	}
	/**
	 *
	 * @Title: formatDate
	 * @Description: 将日期格式化成  yyyy-MM-dd HH:mm:ss
	 * @param d
	 * @return String    返回类型
	 */
	public static String formatDate(Date d){
		if(d==null){
			return null;
		}
		return df2.format(d);
	}

	/**
	 * 解析一个格式化的日期时间字符串中
	 * @param strDate 格式化的日期时间字符串
	 * @param format 格式化字符串默认值为yyyy-MM-dd HH:mm:ss
	 * @return 一个Date类型的对象
	 */
	public static Date parseDateFromString(String strDate,String format){
		try {
			if(!StringUtils.isEmpty(strDate)){
				SimpleDateFormat sdf = StringUtils.isEmpty(format) ? df2 : new SimpleDateFormat(format);
				return sdf.parse(strDate);
			}
		} catch (ParseException e) {
			logger.error("parseDateFromString error, the reason is : {}",e);
		}
		return null;
	}

	/**
	 * 解析一个格式化的日期时间字符串中(locale.us)
	 *
	 * @param strDate 格式化的日期时间字符串
	 * @param format 格式化字符串默认值为yyyy-MM-dd HH:mm:ss
	 * @return 一个Date类型的对象
	 */
	public static Date parseDateFromStringWithUSLocale(String strDate,String format){
		try {
			if(!StringUtils.isEmpty(strDate)){
				SimpleDateFormat sdf = StringUtils.isEmpty(format) ? df2 : new SimpleDateFormat(format,Locale.US);
				return sdf.parse(strDate);
			}
		} catch (ParseException e) {
			logger.error("parseDateFromStringWithUSLocale error, the reason is : {}",e);
		}
		return null;
	}

	/**
	 * 格式化日期时间
	 * @param date 时间
	 * @param format 格式化字符串默认值为yyyy-MM-dd HH:mm:ss
	 * @return 格式化的日期时间字符串
	 */
	public static String formatDate(Date date,String format){
		try{
			if(date != null){
				SimpleDateFormat sd = StringUtils.isEmpty(format) ? df2 : new SimpleDateFormat(format);
				return sd.format(date);
			}
		}catch (Exception e) {
			logger.error("formatDate error, the reason is : {}",e);
		}
		return "";
	}

	/**
	 * 格式化日期时间 长整形
	 *
	 * @param date 时间
	 * @param format 格式化字符串默认值为yyyy-MM-dd HH:mm:ss
	 * @return 格式化的日期时间字符串
	 */
	public static Date formatDateWithLong(Long date,String format){
		try{
			if(date != null){
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(date);
				return c.getTime();
			}
		}catch (Exception e) {
			logger.error("formatDateWithLong error, the reason is : {}",e);
		}
		return null;
	}

	/**
	 *
	 * @Title: formatDate
	 * @Description: 将日期格式化成  yyyy-MM-dd
	 * @param d
	 * @return String    返回类型
	 */
	public static String formatDateWithoutTime(Date d){
		if(d==null){
			return null;
		}
		return df3.format(d);
	}

	//是否是超过传入时间的第4天
	public static boolean isJustOverFourDays(Date date){
		if(date==null){
			return false;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, 4);
		return isToday(cal.getTime());
	}
	//如果在三天内属于缓冲期
	public static boolean isInBufferDays(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if(date!=null){
			cal.add(Calendar.DAY_OF_MONTH, 1);
			date = cal.getTime();
			if(isToday(date)){
				return true;
			}
			cal.add(Calendar.DAY_OF_MONTH, 1);
			date = cal.getTime();
			if(isToday(date)){
				return true;
			}
			cal.add(Calendar.DAY_OF_MONTH, 1);
			date = cal.getTime();
			if(isToday(date)){
				return true;
			}
		}
		return false;
	}

	/**
	 * 	是否刚好超过传入时间1天
	 */
	public static boolean isJustPassOneDays(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		return isToday(cal.getTime());
	}

	/**
	 * 是否刚逾期一个月
	 */
	public static boolean isJustOverOneMonth(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		return isToday(cal.getTime());
	}

	/**
	 * 是否刚逾期一个月
	 *
	 * @param date	项目满标时间
	 * @param periods	当前期数
	 * @return
	 */
	public static boolean isJustOverOneMonth(Date date, int periods){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, periods+1);
		return isToday(cal.getTime());
	}

	/**
	 *
	 * @Title: getDaySub
	 * @Description: 计算两个日期之前相差的天数
	 * @param beginDateStr
	 * @param endDateStr
	 * @return long    返回类型
	 */
	public static long getDaySub(String beginDateStr, String endDateStr) {
		long day = 0;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date beginDate;
		Date endDate;
		try {
			beginDate = format.parse(beginDateStr);
			endDate = format.parse(endDateStr);
			day = getDaySub(beginDate, endDate);
		} catch (ParseException e) {
			logger.error("getDaySub error, the reason is : {}", e);
		}
		return day;
	}

	/**
	 * @Title: daysBetween
	 * @Description: 计算两个日期之前相差的天数
	 * @param beginDate
	 * @param endDate
	 * @return long    返回类型
	 */
	public static long daysBetween(Date beginDate, Date endDate){
		long day = 0;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			beginDate = format.parse(format.format(beginDate));
			endDate = format.parse(format.format(endDate));
			day = getDaySub(beginDate, endDate);
			// 处理负数
			if (day < 0) {
				day = 0;
			}
		} catch (ParseException e) {
			logger.error("daysBetween error, the reason is : {}", e);
		}
		return day;
	}

	/**
	 * @Title: daysBetween
	 * @Description: 计算两个日期之前相差的天数
	 * @param beginDate
	 * @param endDate
	 * @return long    返回类型 可能为负数
	 */
	public static long daysBetweenNegative(Date beginDate, Date endDate){
		long day = 0;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			beginDate = format.parse(format.format(beginDate));
			endDate = format.parse(format.format(endDate));
			day = getDaySub(beginDate, endDate);
		} catch (ParseException e) {
			logger.error("daysBetween error, the reason is : {}", e);
		}
		return day;
	}
	/**
	 *
	 * @Title: getDaySub
	 * @Description: 计算两个日期之前相差的天数
	 * @param beginDate 开始日期
	 * @param endDate 结束日期
	 * @return long    返回类型
	 */
	public static long getDaySub(Date beginDate, Date endDate) {

		return (endDate.getTime() - beginDate.getTime())/ (24 * 60 * 60 * 1000);
	}

	/**
	 *
	 * @Title: parseByString
	 * @Description: 根据字符串解析日期
	 * @param str
	 * @return Date    返回类型
	 */
	public static Date parseByString(String str){
		try {
			return df.parse(str);
		} catch (ParseException e) {
			logger.error("parseByString error, the reason is : {}", e);
			return null;
		}
	}
	public static Date addDate(Date d,int field,int amount){
		try{
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.add(field, amount);
			return cal.getTime();
		}catch(Exception e){
			logger.error("addDate error, the reason is : {}", e);
			return null;
		}
	}


	/**
	 * 两个时间相差距离多少天多少小时多少分多少秒
	 * @param str1 时间参数 1 格式：1990-01-01 12:00:00
	 * @param str2 时间参数 2 格式：2009-01-01 12:00:00
	 * @return long[] 返回值为：{天, 时, 分, 秒}
	 */
	public static long[] getDistanceTimes(String str1, String str2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date one;
		Date two;

		try {
			one = df.parse(str1);
			two = df.parse(str2);
			return getDistanceTimes(one,two);
		} catch (ParseException e) {
			logger.error("getDistanceTimes error, the reason is : {}", e);
		}

		return null;
	}

	/**
	 * 两个时间相差距离多少天多少小时多少分多少秒
	 * @param date1 时间参数 1 格式：1990-01-01 12:00:00
	 * @param date2 时间参数 2 格式：2009-01-01 12:00:00
	 * @return long[] 返回值为：{天, 时, 分, 秒}
	 */
	public static long[] getDistanceTimes(Date date1, Date date2) {
		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;
		long time1 = date1.getTime();
		long time2 = date2.getTime();
		long diff ;
		if(time1 < time2) {
			diff = time2 - time1;
		} else {
			diff = time1 - time2;
		}
		day = diff / (24 * 60 * 60 * 1000);
		hour = diff / (60 * 60 * 1000) - day * 24;
		min = diff / (60 * 1000) - day * 24 * 60 - hour * 60;
		sec = diff/1000-day*24*60*60-hour*60*60-min*60;
		long[] times = {day, hour, min, sec};
		return times;
	}

	/**
	 * 两个时间相差距离多少天多少小时多少分多少秒
	 * @param str1 时间参数 1 格式：1990-01-01 12:00:00
	 * @param str2 时间参数 2 格式：2009-01-01 12:00:00
	 * @return String 返回值为：xx天xx小时xx分xx秒
	 */
	public static String getDistanceTime(String str1, String str2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date one;
		Date two;
		try {
			one = df.parse(str1);
			two = df.parse(str2);
			return getDistanceTime(one, two);
		} catch (ParseException e) {
			logger.error("getDistanceTime error, the reason is : {}", e);
			return null;
		}
	}


	/**
	 * 两个时间相差距离多少天多少小时多少分多少秒
	 * @param date1 时间参数 1 格式：1990-01-01 12:00:00
	 * @param Date2 时间参数 2 格式：2009-01-01 12:00:00
	 * @return String 返回值为：xx天xx小时xx分xx秒
	 */
	public static String getDistanceTime(Date date1, Date Date2){

		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;

		long time1 = date1.getTime();
		long time2 = Date2.getTime();
		long diff ;
		if(time1 < time2) {
			diff = time2 - time1;
		} else {
			diff = time1 - time2;
		}

		day = diff / (24 * 60 * 60 * 1000);
		hour = diff / (60 * 60 * 1000) - day * 24;
		min = diff / (60 * 1000) - day * 24 * 60 - hour * 60;
		sec = diff/1000-day*24*60*60-hour*60*60-min*60;
		return day + "天" + hour + "小时" + min + "分" + sec + "秒";

	}

	/**
	 * 两个时间相差距离多少天多少小时 分秒忽略
	 * @param date1 时间参数 1 格式：1990-01-01 12:00:00
	 * @param Date2 时间参数 2 格式：2009-01-01 12:00:00
	 * @return String 返回值为：xx天xx小时
	 */
	public static String getDistanceHour(Date date1, Date Date2){

		long day = 0;
		long hour = 0;
		long min = 0;
		long sec = 0;

		long time1 = date1.getTime();
		long time2 = Date2.getTime();
		long diff ;
		if(time1 < time2) {
			diff = time2 - time1;
		} else {
			diff = time1 - time2;
		}

		day = diff / (24 * 60 * 60 * 1000);
		hour = diff / (60 * 60 * 1000) - day * 24;
		min = diff / (60 * 1000) - day * 24 * 60 - hour * 60;
		sec = diff/1000-day*24*60*60-hour*60*60-min*60;
		String result="0分钟";
		if(day >=2){
			result= day + "天";
		}

		if(day==1){
			result = day+"天" + hour+"小时";
		}

		if(day==0 && hour >0){
			result = hour+"小时" +min+"分钟";
		}

		if(day==0 && hour ==0 && min > 0){
			result = min+"分钟";
		}
		if(day==0 && hour ==0 && min == 0 && sec >0){
			result = 1+"分钟";
		}

		if(day==0 && hour ==0 && min == 0 && sec <=0){
			result = 0+"分钟";
		}

		return result;

	}

	/**
	 * {@code time1}是否小于{@code time2},即类似于
	 *
	 * <pre>
	 * time1 &lt; time2
	 * </pre>
	 *
	 * 。 如果{@code time2}为<code>null</code>， 则视为最小。
	 *
	 * @param time1
	 *            时间字符串，格式为 yyyyMMddHHmmss，不足14位后补0
	 * @param time2
	 *            时间字符串，格式为 yyyyMMddHHmmss，不足14位后补0
	 * @return
	 */
	public static boolean lessThan(String time1, String time2) {
		if (StringUtils.isEmpty(time1)) {
			return !StringUtils.isEmpty(time2);
		} else {
			return time1.compareTo(time2) < 0;
		}
	}

	public static boolean last24Hours(Date date1){
		if(date1 == null) {
			return false;
		}
		Date date2 = new Date();
		long time1 = date1.getTime();
		long time2 = date2.getTime();
		long diff = (time2 - time1) / 1000;
		return diff > 0 && diff <= 24 * 60 * 60;
	}

	public static Date getDateFromCalender(int year,int month,int day,int hour,int min,int sec){
		Calendar cal = GregorianCalendar.getInstance();
		cal.set(year,month - 1, day, hour, min, sec);
		return cal.getTime();
	}



	public static int compareDate(Date date,Date date2,int stype){
		int n = 0;
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(date);
		c2.setTime(date2);
		while (!c1.after(c2)) {                   // 循环对比，直到相等，n 就是所要的结果
			//list.add(df.format(c1.getTime()));    // 这里可以把间隔的日期存到数组中 打印出来
			n++;
			if(stype==1){
				c1.add(Calendar.MONTH, 1);          // 比较月份，月份+1
			}
			else{
				c1.add(Calendar.DATE, 1);           // 比较天数，日期+1
			}
		}
		n = n-1;
		if(stype==2){
			n = n/365;
		}
		return n;
	}

	public static String getFinishTime(Date d1, Date d2){
		if(d1==null || d2==null || d2.before(d1)){
			return "";
		}
		Calendar c = Calendar.getInstance();
		c.setTime(d1);
		long now = c.getTimeInMillis();
		c.setTime(d2);
		long diff = c.getTimeInMillis()-now;
		String result = "";
		if(diff < 0){
			return result;
		}
		long[] ls =getDistanceTimes(d1, d2);
		int day = (int) ls[0];
		int h = (int) ls[1];
		int min = (int) ls[2];
		int sec = (int) ls[3];
		if(day > 0){
			result+=day+"天";
		}else if(h > 0){
			result+=h+"小时";
		}else if(min > 0){
			result += min+"分钟";
		}else if(sec > 0){
			result += sec+"秒钟";
		}
		return result;
	}


	public static final String[] constellationArr = { "水瓶座", "双鱼座", "牡羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座",
			"天蝎座", "射手座", "魔羯座" };
	public static final int[] constellationEdgeDay = { 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };

	/**
	 * 根据日期获取星座  
	 * @param time
	 * @return
	 */
	public static String date2Constellation(Calendar time) {
		int month = time.get(Calendar.MONTH);
		int day = time.get(Calendar.DAY_OF_MONTH);
		if (day < constellationEdgeDay[month]) {
			month = month - 1;
		}
		if (month >= 0) {
			return constellationArr[month];
		}
		//default to return 魔羯
		return constellationArr[11];
	}


	public static final String formatDateWithHHmm(Date date){
		return df5.format(date);
	}

	//日期年月日显示
	public static String getDateWithChinaFormat(Date date){
		if(date == null){
			return "";
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		int mohth = c.get(Calendar.MONTH)+1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		return year+"年"+mohth+"月"+day+"日";
	}

	/**
	 * YYYY年MM月DD日 hh:mm:ss
	 *
	 * @param date
	 * @return
	 */
	public static String getDateWithChinaFormat1(Date date){
		if(date == null){
			return "";
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		int mohth = c.get(Calendar.MONTH)+1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int min = c.get(Calendar.MINUTE);
		int sec = c.get(Calendar.SECOND);
		String sHour = hour + "";
		if(hour < 10){
			sHour = "0" + hour;
		}
		String sMin = min + "";
		if(min < 10){
			sMin = "0" + min;
		}
		String sSec = sec + "";
		if(sec < 10){
			sSec = "0" + sec;
		}
		return year+"年"+mohth+"月"+day+"日" + " " + sHour + ":" + sMin + ":" + sSec;
	}

	/**
	 * 计算两个日期之间的月份与天数
	 * @param fromDate <String>
	 * @param toDate <String>
	 *
	 * @return int
	 * @throws ParseException
	 */
	public static String getMonthAndDaySpace(Date fromDate, Date toDate){
		int yearSpace,monthSpace = 0,daySpace = 0;

		Calendar fromCal = Calendar.getInstance();
		Calendar toCal = Calendar.getInstance();
		fromCal.setTime(fromDate);
		toCal.setTime(toDate);

		if(fromDate.after(toDate)){
			return "";
		}

		int yeayOfFromDate = fromCal.get(Calendar.YEAR);
		int yeayOfToDate = toCal.get(Calendar.YEAR);
		int monthOfFromDate = fromCal.get(Calendar.MONTH);
		int monthOfToDate = toCal.get(Calendar.MONTH);
		int dayOfFromDate = fromCal.get(Calendar.DAY_OF_MONTH);
		int dayOfToDate = toCal.get(Calendar.DAY_OF_MONTH);

		yearSpace = yeayOfToDate - yeayOfFromDate;
		monthSpace = monthOfToDate - monthOfFromDate;
		daySpace = dayOfToDate - dayOfFromDate;

		if(monthSpace < 0){
			yearSpace -= 1;
			monthSpace = 12 - monthOfFromDate + monthOfToDate;
		}else{
			monthSpace = monthOfToDate - monthOfFromDate;
		}

		if(dayOfToDate < dayOfFromDate){
			monthSpace -= 1;

			//一个月多少天
			int monthDays = fromCal.getActualMaximum(Calendar.DATE);
			daySpace = monthDays - dayOfFromDate + dayOfToDate;
		}

		return yearSpace * 12 + monthSpace + "个月" + daySpace + "天";
	}
	/**
	 * 计算两个日期之间的月份
	 * @param date1 <String>
	 * @param date2 <String>
	 * @return int
	 * @throws ParseException
	 */
	public static int getMonthSpace(String date1, String date2)
			throws ParseException {

		int result = 0;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();

		c1.setTime(sdf.parse(date1));
		c2.setTime(sdf.parse(date2));

		result = (c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR)) * 12+c1.get(Calendar.MONDAY) - c2.get(Calendar.MONTH);

		return result == 0 ? 1 : Math.abs(result);

	}

	/**
	 * 根据年月 计算相隔月份
	 * @param date1
	 * @param date2
	 * @return
	 * @throws ParseException
	 */
	public static int getMonthSpaceWithMonth(String date1, String date2)
			throws ParseException {

		int result = 0;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();

		c1.setTime(sdf.parse(date1));
		c2.setTime(sdf.parse(date2));

		result = (c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR)) * 12+c1.get(Calendar.MONDAY) - c2.get(Calendar.MONTH);

		return result == 0 ? 1 : Math.abs(result);

	}
	/*
	 * 清除日期时分秒
	 */
	public static Date clearHMS(Date date) throws ParseException{
		String dateStr = df.format(date);
		return df.parse(dateStr);
	}

	/**
	 * {@code time1}是否大于{@code time2},即类似于
	 *
	 * <pre>
	 * time1 &gt; time2
	 * </pre>
	 *
	 * 。如果{@code time2}为<code>null</code>， 则视为最大。
	 *
	 * @param time1
	 *            时间字符串，格式为 yyyyMMddHHmmss，不足14位后补9
	 * @param time2
	 *            时间字符串，格式为 yyyyMMddHHmmss，不足14位后补9
	 * @return
	 */
	public static boolean greaterThan(String time1, String time2) {
		if (StringUtils.isEmpty(time1)) {
			return !StringUtils.isEmpty(time2);
		} else {
			return time1.compareTo(time2) > 0;
		}
	}

	public static int getAge(Date birthDate) {
		int age = 0;
		if (birthDate == null)
			return age;

		Date now = new Date();

		SimpleDateFormat format_y = new SimpleDateFormat("yyyy");
		SimpleDateFormat format_M = new SimpleDateFormat("MM");
		SimpleDateFormat format_D = new SimpleDateFormat("dd");

		String birth_year = format_y.format(birthDate);
		String this_year = format_y.format(now);

		String birth_month = format_M.format(birthDate);
		String this_month = format_M.format(now);

		String birth_day = format_D.format(birthDate);
		String this_day = format_D.format(now);

		// 初步，估算
		age = Integer.parseInt(this_year) - Integer.parseInt(birth_year);

		// 如果未到出生月份，则age - 1
		if (this_month.compareTo(birth_month) < 0) {
			age -= 1;
		} else if (this_month.compareTo(birth_month) == 0
				&& birth_day.compareTo(this_day) > 0) {
			age -= 1;
		}

		if (age < 0) {
			age = 0;
		}
		return age;
	}

	/**
	 *
	 * @Title: getNowMonth
	 * @Description: 获取当前时间月份
	 * @param @return    设定文件
	 * @return int    返回类型
	 * @throws
	 */
	public static String getNowMonth() {
		SimpleDateFormat format_M = new SimpleDateFormat("MM");
		return format_M.format(new Date());
	}


	/**
	 *
	 * @Title: getNowMonth
	 * @Description: 获取当前年份
	 * @param @return    设定文件
	 * @return int    返回类型
	 * @throws
	 */
	public static String getNowYear() {
		SimpleDateFormat format_M = new SimpleDateFormat("yyyy");
		return format_M.format(new Date());
	}

	/**
	 * 时间戳转换成日期格式字符串
	 * @param seconds 精确到秒的字符串
	 * @param format
	 * @return
	 */
	public static String timeStamp2Date(String seconds,String format) {
		if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
			return "";
		}
		if(format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(Long.valueOf(seconds)));
	}


	/**
	 * 检查时间是否在开始日期0:0:0-结束时间23:59:59内
	 * @param startDate
	 * @param endDate
	 * @param usedDate
	 * @return
	 */
	public static boolean checkDateBetweenStartAndEndDate(Date startDate,Date endDate, Date usedDate){
		if(startDate==null || endDate==null || usedDate==null){
			return false;
		}
		String startDateStr =format(startDate,"yyyy-MM-dd");
		String endDateStr =format(endDate,"yyyy-MM-dd");
		String usedDateStr =format(usedDate,"yyyy-MM-dd");

		//开始时间检查
		boolean startCheck=false;
		if(usedDateStr.equals(startDateStr)){
			startCheck=true;
		}

		if(usedDate.after(startDate)){
			startCheck=true;
		}
		//结束时间检查
		boolean endCheck = false;
		if(usedDateStr.equals(endDateStr)){
			endCheck=true;
		}

		if(usedDate.before(endDate)){
			endCheck=true;
		}
		return (startCheck && endCheck);

	}

	/**
	 * 在指定的日期d新增addDays天，并取当天的结束时间返回
	 * @param d
	 * @param addDays
	 * @return
	 */
	public static Date addDaysEndTime(Date d,int addDays){
		try{
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.add(Calendar.DAY_OF_YEAR, addDays);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			return cal.getTime();
		}catch(Exception e){
			logger.error("addDaysEndTime error, the reason is : {}", e);
			return null;
		}
	}
	/**
	 * 在指定的日期d新增addDays天，并取当天的结束时间返回
	 * @param d
	 * @param addDays
	 * @return
	 */
	public static Date addDaysReturnTime(Date d,int addDays){
		try{
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.add(Calendar.DAY_OF_YEAR, addDays);
			return cal.getTime();
		}catch(Exception e){
			logger.error("addDaysEndTime error, the reason is : {}", e);
			return null;
		}
	}
	/**
	 * 在指定的日期d新增addYears年，并取当天的结束时间返回
	 * @param fromDate
	 * @param addYears
	 * @return
	 */
	public static Date addYearsEndTime(Date fromDate,int addYears){
		try{
			Calendar cal = Calendar.getInstance();
			cal.setTime(fromDate);
			cal.add(Calendar.YEAR, addYears);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			return cal.getTime();
		}catch(Exception e){
			logger.error("addDaysEndTime error, the reason is : {}", e);
			return null;
		}
	}

	/**
	 * 获取当日剩余分钟数
	 * @return
	 */
	public static long getRemainMillisecondsOfToday() {
		final Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return  cal.getTimeInMillis() - System.currentTimeMillis();
	}

	public static int compareDate(Date d1,
								  Date d2) {
		long t1 = d1.getTime();
		long t2 = d2.getTime();
		if (t1 == t2) {
			return 0;
		}
		if (t1 > t2) {
			return 1;
		}
		return -1;
	}

	/**
	 * 获取当前的小时时间
	 *
	 * @return
	 */
	public static int getCurrentDateOfHours(){
		final Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获取当前的小时的秒
	 *
	 * @return
	 */
	public static int getCurrentDateOfSecond(){
		final Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.SECOND);
	}

	/**
	 * 返回两个日志相差的天数（如果为同一天，则返回0，1号和2号则返回1，支持跨年计算）
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int differentDaysByMillisecond(Date date1,Date date2)
	{
		int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
		return days;
	}

	/**
	 * 获取两个日历的月份之差
	 *
	 * @param calendarBirth
	 * @param calendarNow
	 * @return
	 */
	public static int getMonthsOfSpace(Calendar calendarBirth, Calendar calendarNow) {
		return (calendarNow.get(Calendar.YEAR) - calendarBirth.get(Calendar.YEAR)) * 12
				+ calendarNow.get(Calendar.MONTH) - calendarBirth.get(Calendar.MONTH);
	}

	/**
	 *
	 * @Description 获取指定日期开始时间
	 * @param today
	 * @return
	 * @return String
	 * @throws
	 *
	 */
	public static String getDateStart(Date today) {
		String todayFormat = format(today,SHOW_DATE_FORMAT);
		return todayFormat + " 00:00:00";
	}

	/**
	 *
	 * @Description 获取指定日期结束时间
	 * @param today
	 * @return
	 * @return String
	 * @throws
	 *
	 */
	public static String getDateEnd(Date today) {
		String todayFormat = format(today,SHOW_DATE_FORMAT);
		return todayFormat + " 23:59:59";
	}

	/**
	 * 当前月第一天
	 * @return
	 */
	public static Date getMonthStartDate() {
		Date nowdate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(nowdate);
		cal.add(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}


	/**
	 * 判断年龄是否在规定范围内 规则由产品提供 2016.12.23
	 *年龄限制最大55周岁，若用户是2000年10月1日出生， 则到 2055年10月2日就不能发布借款；
	 *限制借款年龄下限值22周岁用户2000年10月1日出生，应该 2022年10月1日可以借
	 * @param birthDate 出生日期 日期格式
	 * @param minAge 准入最小年龄
	 * @param maxAge 准入最大年龄
	 * @return true 合法年龄 false 非法年龄
	 */
	public static boolean isValidAge(Date birthDate,int minAge,int maxAge) {
		try {
			if (birthDate == null)
				return true;
			Date now = new Date();
			SimpleDateFormat format_y = new SimpleDateFormat("yyyy");
			SimpleDateFormat format_M = new SimpleDateFormat("MM");
			SimpleDateFormat format_D = new SimpleDateFormat("dd");
			int birthYear = Integer.parseInt(format_y.format(birthDate));
			int thisYear = Integer.parseInt(format_y.format(now));

			int birthMonth =  Integer.parseInt(format_M.format(birthDate));
			int thisMonth = Integer.parseInt(format_M.format(now));

			int birthDay = Integer.parseInt(format_D.format(birthDate));
			int thisDay = Integer.parseInt(format_D.format(now));

			//年龄小于最小年龄
			if(getAgeForMinAge(birthYear,birthMonth,birthDay,thisYear,thisMonth,thisDay)<minAge){
				return false;
			}
			//年龄大于最大年龄
			return getAgeForMaxAge(birthYear, birthMonth, birthDay, thisYear, thisMonth, thisDay) <= maxAge;
		}catch (Exception e){
			e.printStackTrace();
			return true;
		}
	}

	/**
	 * 判断年龄是否在规定范围内 规则由产品提供 2016.12.23
	 *年龄限制最大55周岁，若用户是2000年10月1日出生， 则到 2055年10月2日就不能发布借款；
	 *限制借款年龄下限值22周岁用户2000年10月1日出生，应该 2022年10月1日可以借
	 * @param birthDate 出生日期 yyyyMMdd 格式
	 * @param minAge 准入最小年龄
	 * @param maxAge 准入最大年龄
	 * @return true 合法年龄 false 非法年龄
	 */
	public static boolean isValidAge(String birthDate,int minAge,int maxAge) {
		try {
			Date birth = DateUtils.parseDateFromString(birthDate, DATE_FORMAT);
			return isValidAge(birth,minAge,maxAge);
		}catch (Exception e){
			e.printStackTrace();
			return true;
		}
	}


	/**
	 * 计算当前年龄-判断最大年龄限制时使用
	 * @param birthYear
	 * @param birthMonth
	 * @param birthDay
	 * @param thisYear
	 * @param thisMonth
	 * @param thisDay
	 * @return
	 */
	private static int getAgeForMaxAge(int birthYear,int birthMonth,int birthDay,int thisYear,int thisMonth,int thisDay){
		// 初步，估算
		int age = thisYear - birthYear;
		// 超过出生月份，则age+ 1
		if (thisMonth>birthMonth) {
			age = age +1;
		} else if (thisMonth==birthMonth && thisDay > birthDay) {
			//当前月份等于出生月份且天数大于出生天数
			age = age + 1;
		}else if(thisMonth<birthMonth){
			//未到出生月份则减一
			age=age-1;
		}
		if (age < 0) {
			age = 0;
		}
		return age;
	}
	/**
	 * 计算当前年龄-判断最小年龄限制时使用
	 * @param birthYear
	 * @param birthMonth
	 * @param birthDay
	 * @param thisYear
	 * @param thisMonth
	 * @param thisDay
	 * @return
	 */
	private static int getAgeForMinAge(int birthYear,int birthMonth,int birthDay,int thisYear,int thisMonth,int thisDay){
		// 初步，估算
		int age = thisYear - birthYear;
		if(thisMonth<birthMonth || (thisMonth==birthMonth && thisDay<birthDay)){
			//未到出生月份,或出生日则减一
			age=age-1;
		}
		if (age < 0) {
			age = 0;
		}
		return age;
	}


	private static int getDaysByYearMonth(int year, int month) {

		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, 1);
		a.roll(Calendar.DATE, -1);
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	/**
	 * 算两个日期时间差  格式：6个月5天
	 * @param nowDate
	 * @param oldDate
	 * @return
	 */
	public static String getDifference (Date nowDate ,Date oldDate){
		Calendar calNow = Calendar.getInstance();
		Calendar calOld = Calendar.getInstance();
		calNow.setTime(nowDate);
		calOld.setTime(oldDate);
		int nowYear = calNow.get(Calendar.YEAR);
		int nowMonth = calNow.get(Calendar.MONTH);
		int nowDay = calNow.get(Calendar.DAY_OF_MONTH);
		int oldYear = calOld.get(Calendar.YEAR);
		int oldMonth = calOld.get(Calendar.MONTH);
		int oldDay = calOld.get(Calendar.DAY_OF_MONTH);
		if(nowYear<oldYear){
			return "";
		}
		int month = (nowYear - oldYear) * 12;
		if(0 == month){
			month = nowMonth-oldMonth;
		}
		int nowMaxDay = getDaysByYearMonth(nowYear,nowMonth);
		if(nowDay<oldDay){
			nowDay = nowDay +nowMaxDay;
			month--;
		}
		int day = nowDay - oldDay;
		String differenceMessage =  month+"个月"+day+"天";
		return differenceMessage;
	}

	/**
	 * 根据minute分钟数获取时间 比如5分钟之前minute = -5  5分钟之后传minute =  5
	 * @param minute
	 * @return
	 */
	public static Date getDateTimeByMinute(int minute){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE,minute);
		return calendar.getTime();
	}


}
