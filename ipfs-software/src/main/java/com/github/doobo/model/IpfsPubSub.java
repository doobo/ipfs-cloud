package com.github.doobo.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * 广播和订阅数据集合
 */
@Data
@Accessors(chain = true)
public class IpfsPubSub {

	/**
	 * 全局自增ID
	 */
	private Long id;

	/**
	 * 用户标识ID
	 */
	private String fromSessionId;

	/**
	 * 去的用户标识
	 */
	private Long toSessionId;

	/**
	 * 信息标记
	 */
	private String sign;

	/**
	 * 时间戳
	 */
	private Long time;

	/**
	 * 自定义内容
	 */
	private Map<String,Object> body;

}
