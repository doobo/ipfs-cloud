package com.github.doobo.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 字符类工具
 */
public class WordUtils {

	private final static Pattern linePattern = Pattern.compile("_(\\w)");

	private final static Pattern humpPattern = Pattern.compile("[A-Z]");

	private final static String IP_V4 = "\\A(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\z";

	/**
	 * 下划线转驼峰
	 * @param str
	 */
	public static String lineToHump(String str) {
		if(str == null || str.isEmpty() || !str.contains("_")){
			return str;
		}
		str = str.toLowerCase();
		Matcher matcher = linePattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 驼峰转下划线
	 * @param str
	 */
	public static String humpToLine(String str) {
		Matcher matcher = humpPattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 首字母大写
	 * @param string
	 */
	public static String toUpperCaseIndex(String string) {
		char[] methodName = string.toCharArray();
		methodName[0] = toUpperCase(methodName[0]);
		return String.valueOf(methodName);
	}

	/**
	 * 首字母小写
	 * @param string
	 * @return
	 */
	public static String toLowerCaseIndex(String string) {
		char[] methodName = string.toCharArray();
		methodName[0] = toLowerCase(methodName[0]);
		return String.valueOf(methodName);
	}

	/**
	 * 字符转成大写
	 * @param chars
	 */
	public static char toUpperCase(char chars) {
		if (97 <= chars && chars <= 122) {
			chars ^= 32;
		}
		return chars;
	}

	/**
	 * 字符转成小写
	 * @param chars
	 */
	public static char toLowerCase(char chars) {
		if (65 <= chars && chars <= 90) {
			chars += 32;
		}
		return chars;
	}

	/**
	 * 移除多余空行和空格,只保留一个空行
	 */
	public static String dealRedundantSpaceAndBlankLine(String content) {
		if (content == null || content.length() == 0) {
			return "";
		}
		StringBuilder strAfterRemoveCRSB = new StringBuilder();
		for (int i = 0; i < content.length(); i++) {
			if (content.charAt(i) != '\r')
				strAfterRemoveCRSB.append(content.charAt(i));
		}
		String strAfterRemoveCR = strAfterRemoveCRSB.toString();
		if (strAfterRemoveCR.length() == 0) {
			return "";
		}
		StringBuilder resultSB = new StringBuilder();
		String[] lines = strAfterRemoveCR.split("\n");
		int blankCount = 0;
		for (String line : lines) {
			if (line == null) {
				continue;
			}
			String lineTrim = line.replace("\t","").trim();
			if ("".equals(lineTrim)) {
				blankCount++;
				if(blankCount < 2) {
					resultSB.append("\n");
				}
			} else {
				blankCount = 0;
				resultSB.append(line).append("\n");
			}
		}
		resultSB.deleteCharAt(resultSB.length() - 1);
		return resultSB.toString();
	}

	/**
	 * 移除1行中的多余空格
	 */
	public static String dealSpace4OneLine(String line) {
		if (line == null || "".equals(line)) {
			return "";
		}
		int spaceCount = 0;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < line.length(); i++) {
			char curChar = line.charAt(i);
			if (curChar == ' ')
			{
				spaceCount++;
				if (spaceCount <= 5) {
					sb.append(' ');
				}
			} else {
				spaceCount = 0;
				sb.append(curChar);
			}
		}
		return sb.toString();
	}

	/**
	 * 正则全局替换字符
	 * @param regex
	 * @param replacement
	 */
	public static String replaceAllStr(String regex, String src, String replacement){
		if(src == null || src.isEmpty()){
			return src;
		}
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(src);
		while (matcher.find()){
			src = src.replace(matcher.group(), replacement);
		}
		return src;
	}

	/**
	 * 正则过滤替换字符
	 * @param regex
	 * @param replacement
	 */
	public static String filterAllStr(String regex, String src, String replacement){
		if(src == null || src.isEmpty()){
			return src;
		}
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(src);
		return matcher.replaceAll(replacement).trim();
	}

	/**
	 * 保留word字符,其它多替换成指定字符
	 * @param str
	 * @param word
	 */
	public static String plusOut(String str, String word, String replacement) {
		return str.replaceAll("\\G((?:" + word + ")*+).", "$1"+replacement);
	}

	/**
	 * 返回字符串后面的数字
	 * @param str
	 * @return
	 */
	public static String getStrEndNumber(String str){
		Pattern pattern = Pattern.compile("\\d+$");
		Matcher matcher = pattern.matcher(str);
		if(matcher.find()){
			return matcher.group();
		}
		return null;
	}

	/**
	 * 判断是否是IPV4地址
	 * @param ip
	 */
	public static boolean isIpV4Address(String ip){
		if(ip == null){
			return false;
		}
		return ip.matches(IP_V4);
	}

	/**
	 * 模拟JS的encodeURIComponent
	 * @param str
	 */
	public static String encodeURIComponent(String str){
		try {
			return URLEncoder.encode(str, UTF_8.name())
				.replaceAll("\\+", "%20")
				.replaceAll("\\!", "%21")
				.replaceAll("\\'", "%27")
				.replaceAll("\\(", "%28")
				.replaceAll("\\)", "%29")
				.replaceAll("\\~", "%7E");
		} catch (UnsupportedEncodingException e) {
			return str;
		}
	}

	/**
	 * 模拟JS使用的encodeURI
	 * @param str
	 */
	public static String encodeURI(String str) {
		String isoStr = null;
		try {
			isoStr = new String(str.getBytes(UTF_8.name()), "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			return str;
		}
		char[] chars = isoStr.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			if ((chars[i] <= 'z' && chars[i] >= 'a')
				|| (chars[i] <= 'Z' && chars[i] >= 'A') || chars[i] == '-'
				|| chars[i] == '_' || chars[i] == '.' || chars[i] == '!'
				|| chars[i] == '~' || chars[i] == '*' || chars[i] == '\''
				|| chars[i] == '(' || chars[i] == ')' || chars[i] == ';'
				|| chars[i] == '/' || chars[i] == '?' || chars[i] == ':'
				|| chars[i] == '@' || chars[i] == '&' || chars[i] == '='
				|| chars[i] == '+' || chars[i] == '$' || chars[i] == ','
				|| chars[i] == '#') {
				sb.append(chars[i]);
			} else {
				sb.append("%");
				sb.append(Integer.toHexString(chars[i]));
			}
		}
		return sb.toString();
	}

}
