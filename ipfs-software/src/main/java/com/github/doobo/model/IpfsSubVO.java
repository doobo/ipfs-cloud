package com.github.doobo.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * ipfs广播数据格式
 */
@Data
@Accessors(chain = true)
public class IpfsSubVO extends  IpfsPubSub {

	/**
	 * 全局自增ID
	 */
	private Long id;

	/**
	 * 简单报文
	 */
	private String msg;

}
