package com.github.doobo.script;

import com.github.doobo.utils.SmUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PwdUtils {

	/**
	 * 加密
	 * @param origin
	 * @param pwd
	 */
	public static String encode(String origin, String pwd){
		return SmUtils.encryptBySM2(pwd, origin);
	}

	/**
	 * 解密
	 * @param origin
	 * @param pwd
	 */
	public static String decode(String origin, String pwd){
		return SmUtils.decryptBySM2(pwd, origin);
	}
}
