package com.yibai.crown.base.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class MD5SignUtil {
	
	/**
	 * 签名字段
	 */
	private static String signatureFieldName = "signature";
	
	/**
	 * 链接字符串
	 */
	private static final String CONCATENATION_CHARACTER = "&";
	
	/**
	 * 生成签名
	 * 
	 * 算法说明：
	 *     1、对请求参数按首字母进行字典排序
	 *     2、遍历排序或的KEY，以【key=value + 连接字符】格式拼装字符串，去除不需要验证以及value为空的字段
	 *	   3、对第2步获取的字符串进行MD5加密
	 *     4、对第3步获取的字符串 + 公共密钥再进行一次MD5加密 
	 * 
	 * @param paramMap
	 * 				请求参数
	 * @param secret
	 * 				公共密钥，配置中心获取
	 * @return
	 */
	public static String create(Map<String, String> paramMap, String secret) {
		return MD5Util.md5(MD5Util.md5(getSignData(paramMap)) + secret);
	}
	
	/**
	 * 验证签名
	 * 
	 * @param paramMap
	 * 				请求参数
	 * @param secret
	 * 				公共密钥，配置中心获取
	 * @param signature
	 * 				签名字符串
	 * @return
	 */
	public static boolean verify(Map<String, String> paramMap, String secret, String signature) {
		String ciphertext = MD5SignUtil.create(paramMap, secret);
		return ciphertext.equals(signature);
	}
	
	/**
	 * 生成加密明文
	 * 
	 * @param paramMap
	 * 				请求参数
	 * @return
	 */
	public static String getSignData(Map<String,String> paramMap){
		// 按首字母排序
		List<String> keys = new ArrayList<String>(paramMap.keySet());
		
		Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);
		
		// 拼接签名明文 
		StringBuffer content = new StringBuffer();
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			if (MD5SignUtil.signatureFieldName.equals(key)) {
				continue;
			}
			String value = paramMap.get(key);
			// 空串不参与签名
			if (StringUtils.isEmpty(value)) {
				continue;
			}
			content.append((i == 0 ? "" : CONCATENATION_CHARACTER) + key + "=" + value);
		}

		// 剔除开头的连接符
		String signSrc = content.toString();
		if (signSrc.startsWith(CONCATENATION_CHARACTER)) {
			signSrc = signSrc.replaceFirst(CONCATENATION_CHARACTER, "");
		}
		return signSrc;
	}
	
	public static void setSignatureFieldName(String signatureFieldName) {
		MD5SignUtil.signatureFieldName = signatureFieldName;
	}
	
	public static void main(String[] args) {
		String secret = "xxxxxxxxxxxxx";
		
		Map<String, String> paramMap = new HashMap<String, String>();
		
		paramMap.put("userId", "1231312");
		paramMap.put("orderNo", "MD234234324");
		
		System.out.println(MD5SignUtil.create(paramMap, secret));
		
		System.out.println(MD5SignUtil.verify(paramMap, secret, MD5SignUtil.create(paramMap, secret)));
	}
}