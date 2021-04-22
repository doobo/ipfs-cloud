package com.github.doobo.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * ipfs广播数据格式
 */
@Data
@Accessors(chain = true)
public class IpfsPubVO extends  IpfsPubSub {

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

}
