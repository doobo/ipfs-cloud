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
	CONTENT_TYPE("Content-Type"),
	IPFS("ipfs"),
	IPFS_SUFFIX(".ipfs"),
	UPLOAD("upload"),
	JSON(".json"),
	/*斜杠*/
	SLASH("\\"),
	/*双斜杠*/
	SLASH_DOUBLE("\\\\"),
	/*反斜杠*/
	BACKSLASH("/"),
	DOT("."),
	QUOTES_SINGLE("'"),
	QUOTES_DOUBLE("\"");

	private final String value;

	StringParams(String value) {
		this.value = value;
	}

	public String str() {
		return value;
	}
}
