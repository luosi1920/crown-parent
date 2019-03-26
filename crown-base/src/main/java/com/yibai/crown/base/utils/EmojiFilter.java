package com.yibai.crown.base.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Elliott
 * @ClassName: EmojiFilter
 * @Description: 用于过滤手机端特殊表情输入 Emoji
 * @date Jan 17, 2015 6:25:57 PM
 */

public class EmojiFilter {
    /**
     * 检测是否有emoji字符
     *
     * @param source
     * @return 一旦含有就抛出
     */
    public static boolean containsEmoji(String source) {
        if (StringUtils.isBlank(source)) {
            return false;
        }

        int len = source.length();

        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);

            if (isNotEmojiCharacter(codePoint)) {
                // do nothing，判断到了这里表明，确认有表情字符
                return true;
            }
        }

        return false;
    }

    /**
     * @param codePoint 待验证字符
     * @return boolean 返回类型，true 表示不包含。false 包含
     * @Title: isEmojiCharacter
     * @Description: 验证是否是 emoji字符
     */
    private static boolean isNotEmojiCharacter(char codePoint) {
        return codePoint == 0x0 || codePoint == 0x9 || codePoint == 0xA
                || codePoint == 0xD
                || codePoint >= 0x20 && codePoint <= 0xD7FF || codePoint >= 0xE000
                && codePoint <= 0xFFFD || codePoint >= 0x10000
                && codePoint <= 0x10FFFF;
    }

    /**
     * 过滤emoji 或者 其他非文字类型的字符
     *
     * @param source
     * @return
     */
    public static String filterEmoji(String source) {

        if (!containsEmoji(source)) {
            return source;// 如果不包含，直接返回
        }
        // 到这里铁定包含
        StringBuilder buf = null;
        int len = source.length();

        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);

            if (isNotEmojiCharacter(codePoint)) {
                if (buf == null) {
                    buf = new StringBuilder(source.length());
                }

                buf.append(codePoint);
            } else {
                // 使用*代替 emoji
                buf.append("*");
            }
        }

        if (buf != null && buf.length() == len) {
            return buf.toString();
        } else {
            return ""; // 如果没有找到 emoji表情
        }
    }

    public static String emojiConvert(String str) {
        String returnStr = "";
        try {
            String patternString = "([\\x{10000}-\\x{10ffff}\ud800-\udfff])";
            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher = pattern.matcher(str);
            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                try {
//                  matcher.appendReplacement(sb, "[[" + URLEncoder.encode(matcher.group(1), "UTF-8") + "]]");
                    matcher.appendReplacement(sb, "*");
                } catch (Exception e) {
                }
            }
            matcher.appendTail(sb);
            returnStr = sb.toString();
        }catch (Exception e){}
        return returnStr;
    }

}
