package com.github.doobo.params;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class ErrorInfo {

	/**
	 * 错误编码
	 */
	private int code;

	/**
	 * 错误详细信息
	 */
	private String msg;

	/**
	 * 出错文件
	 */
	private String file;

	/**
	 * 出错行号
	 */
	private Long line;

	public ErrorInfo(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public ErrorInfo(String msg) {
		this.msg = msg;
	}
}
