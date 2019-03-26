package com.yibai.crown.base.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.Random;

public class MD5Util {
	
	private static final Logger logger = LoggerFactory.getLogger(MD5Util.class);
	
	// 随机盐值长度
	private static final int RANDOM_SALT_LENGTH = 5;
	
	// 16进制字符串
	private static final char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
			'A', 'B', 'C', 'D', 'E', 'F' };
	
	/**
	 * md5加密
	 * @param plainText
	 * @return
	 */
	public static String md5(String plainText) {
		return md5(plainText, null);
	}
	
	/**
	 * MD5加密
	 * 
	 * @param plainText
	 * 			明文
	 * @param salt
	 * 			盐值
	 * @return
	 */
	public static String md5(String plainText, String salt) {
		try {
			if (StringUtils.isNotEmpty(salt)) {
				plainText = plainText + "{" + salt + "}";
			}
			MessageDigest messageDigest = MessageDigest.getInstance("md5");
			byte[] hash = messageDigest.digest(plainText.getBytes());
			return toHex(hash);
		} catch (Exception e) {
			logger.error("md5 occur exception, the reason is : {}", e);
		}
		return null;
	}

	/**
	 * 转化成16进制
	 * 
	 * @param hash
	 * @return
	 */
	private static final String toHex(byte hash[]) {
		if (hash == null) {
			return null;
		}
		StringBuffer buffer = new StringBuffer(hash.length * 2);
		for (int i = 0; i < hash.length; i++) {
			// 将高4位转换成字符串
			int x = (hash[i] >>> 4 & 0x0f); 
			buffer.append(hexDigits[x]);
			// 将低4位转换成字符串
			x = (hash[i] & 0x0f);
			buffer.append(hexDigits[x]);
		}
		return buffer.toString();
	}
	
	/**
	 * 随机生成盐值
	 * 
	 * @return
	 */
	public static String randomSalt() {
		StringBuilder salt = new StringBuilder();
		Random random = new Random();
		// 随机获取ASCII码上33~126
		for (int i = 0; i < RANDOM_SALT_LENGTH; i++) {
			int a = random.nextInt(94) + 33;
			salt.append((char) a);
		}
		return salt.toString();
	}
	
	public static void main(String[] args) {
		String src = String.format("111111");

		System.out.println("明文：" + src);
		
		System.out.println("MD5加密后：" + MD5Util.md5(src));
	}
}