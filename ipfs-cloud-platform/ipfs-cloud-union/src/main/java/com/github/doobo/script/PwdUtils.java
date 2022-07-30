package com.github.doobo.script;

import com.github.doobo.utils.SmUtils;

public abstract class PwdUtils {

	/**
	 * 加密
	 */
	public static String encode(String origin, String pwd){
		return SmUtils.encryptBySM2(pwd, origin);
	}

	/**
	 * 解密
	 */
	public static String decode(String origin, String pwd){
		return SmUtils.decryptBySM2(pwd, origin);
	}
}
