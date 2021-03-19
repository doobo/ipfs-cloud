package com.github.doobo.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
public class IpfsPubMsgVO {

	/**
	 * 全局自增ID
	 */
	private Long id;

	/**
	 * 发送广播的节点ID
	 */
	private String ipfs;

	/**
	 * 广播前缀
	 */
	private String topic;

	/**
	 * 广播信息
	 */
	private String msg;

	/**
	 * 时间戳
	 */
	private Long time;

	/**
	 * 自定义内容
	 */
	private Map<String,Object> body;
}
