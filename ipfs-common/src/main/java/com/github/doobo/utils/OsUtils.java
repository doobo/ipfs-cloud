package com.github.doobo.utils;

import com.github.doobo.params.StringParams;

/**
 * 系统相关常用工具类
 */
public class OsUtils {

	/**
	 * 获取系统类型
	 * @return
	 */
	public static StringParams getSystemType() {
		String osName = System.getProperty(StringParams.OS.str());
		if (osName.startsWith(StringParams.MAC_OS.str())) {
			return StringParams.MAC_OS;
		} else if (osName.startsWith(StringParams.Windows.str())) {
			return StringParams.Windows;
		}
		return StringParams.Linux;
	}
}
