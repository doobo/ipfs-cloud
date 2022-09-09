package com.github.doobo.bo;

import lombok.Data;

/**
 * 消息编码模型
 *
 * @Description: ipfs-cloud
 * @User: doobo
 * @Time: 2022-09-09 22:58
 */
@Data
public class PubMsgEncodeBO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * all,group,single
	 */
	private String type;

	/**
	 * 消息源,cid
	 */
	private String from;

	/**
	 * 目标,*,groupName,cid
	 * 多个组,逗号隔开
	 */
	private String target;

	/**
	 * 消息体
	 */
	private String msg;

	public String formatMsg(){
		return String.format("%s|%s:%s:%s", type, target, msg, from);
	}
}
