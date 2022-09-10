package com.github.doobo.utils;

import java.util.Base64;
import java.util.regex.Pattern;

public abstract class Base64Utils {

	/**
	 * BASE64Encoder 加密
	 * @param data 要加密的数据
	 * @return 加密后的字符串
	 */
	public static String encryptBASE64(byte[] data) {
		// BASE64Encoder encoder = new BASE64Encoder();
		// String encode = encoder.encode(data);
		// 从JKD 9开始rt.jar包已废除，从JDK 1.8开始使用java.util.Base64.Encoder
		Base64.Encoder encoder = Base64.getEncoder();
		return encoder.encodeToString(data);
	}

	/**
	 * BASE64Decoder 解密
	 * @param data 要解密的字符串
	 * @return 解密后的byte[]
	 */
	public static byte[] decryptBASE64(String data) {
		// BASE64Decoder decoder = new BASE64Decoder();
		// byte[] buffer = decoder.decodeBuffer(data);
		// 从JKD 9开始rt.jar包已废除，从JDK 1.8开始使用java.util.Base64.Decoder
		Base64.Decoder decoder = Base64.getDecoder();
		return decoder.decode(data);
	}

	/**
	 * BASE64Encoder 加密,1.7及以下版本
	 * @param data 要加密的数据
	 * @return 加密后的字符串
	 */
	public static String encrypt7BASE64(byte[] data) {
		Base64.Encoder encoder = Base64.getMimeEncoder();
		return encoder.encodeToString(data);
	}

	/**
	 * BASE64Decoder 解密,1.7及以下版本
	 * @param data 要解密的字符串
	 * @return 解密后的byte[]
	 */
	public static byte[] decrypt7BASE64(String data) {
		Base64.Decoder decoder = Base64.getMimeDecoder();
		return decoder.decode(data);
	}


	/**
	 * 判断是不是Base64编码
	 */
	public static boolean isBase64(String str) {
		String base64Pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
		return Pattern.matches(base64Pattern, str);
	}
}
