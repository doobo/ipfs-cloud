package com.github.doobo.params;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @description 返回模板
 */
@Data
@Accessors(chain = true)
public class ResultTemplate<T> {
	/**
	 * 是否成功
	 */
	private Boolean ok;

	/**
	 * 错误信息
	 */
    private ErrorInfo err;

	/**
	 * 改变数
	 */
    private Long changes;

	/**
	 * 当前页
	 */
    private Long index;

	/**
	 * 分页长度
	 */
	private Long size;

	/**
	 * 分页的页数
	 */
    private Long pages;

	/**
	 * 总记录数
	 */
    private Long total;

	/**
	 * 数据集合
	 */
    private T data;

	/**
	 * 成功提示
	 */
    private String okMessage;

	/**
	 * 详细信息
	 */
    private Object detail;

    private int code;
}

