package com.github.doobo.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 搜索入参模型
 */
@Data
@Accessors(chain = true)
public class SearchVO extends PageParam{

	/**
	 * 综合搜索,文件名
	 */
	private String query;

	/**
	 * cid精确搜索
	 */
	private String cid;

	/**
	 * 文件类型
	 */
	private String mime;
}
