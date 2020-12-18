package com.github.doobo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 搜索入参模型
 */
@Data
@Accessors(chain = true)
public class SearchVO extends PageParam {

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

	/**
	 * 节点id
	 */
	private String nodeId;

	/**
	 * 文件最大大小
	 */
	private Long gtSize;

	/**
	 * 文件最小大小
	 */
	private Long ltSize;

	/**
	 * 最小时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date minTime;

	/**
	 * 最大时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date maxTime;
}
