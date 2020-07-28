package com.github.doobo.params;

/**
 * 常用字符串参数
 */
public enum StringParams {
	OS("os.name"),
	MAC_OS("Mac OS"),
	Windows("Windows"),
	Linux("Linux"),
	JSON_TYPE("application/json"),
	CONTENT_TYPE("Content-Type");

	private String value;

	StringParams(String value) {
		this.value = value;
	}

	public String str() {
		return value;
	}
}
