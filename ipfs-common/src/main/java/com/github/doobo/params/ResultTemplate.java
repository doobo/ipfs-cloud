package com.github.doobo.params;

import lombok.Data;
import lombok.NoArgsConstructor;
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
	 * 分页索引
	 */
    private Long pageIndex;

	/**
	 * 分页长度
	 */
    private Long pageCount;

	/**
	 * 分页总和
	 */
    private Long pageTotal;

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
}

