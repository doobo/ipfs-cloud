package com.github.doobo.script;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.util.text.BasicTextEncryptor;

@Slf4j
public class PwdUtils {

	/**
	 * 加密
	 * @param origin
	 * @param pwd
	 */
	public static String encode(String origin, String pwd){
		BasicTextEncryptor en = new BasicTextEncryptor();
		en.setPassword(pwd);
		return en.encrypt(origin);
	}

	/**
	 * 解密
	 * @param origin
	 * @param pwd
	 */
	public static String decode(String origin, String pwd){
		BasicTextEncryptor en = new BasicTextEncryptor();
		en.setPassword(pwd);
		return en.decrypt(origin);
	}
}
