package com.github.doobo.bo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 简单消息模型
 *
 * @Description: ipfs-cloud
 * @User: doobo
 * @Time: 2022-09-09 22:58
 */
@Data
public class PubMessageBO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 消息ID
	 */
	private Long id;

	/**
	 * 请求地址
	 */
	private String uri;

	/**
	 * 请求方法,get
	 */
	private String method;

	/**
	 * 参数
	 */
	private Map<String, List<Object>> params;

	/**
	 * 标头
	 */
	private Map<String, List<Object>> headers;

	/**
	 * 消息体
	 */
	private String body;

	/**
	 * 来源标识
	 */
	private String from;

	/**
	 * 内容格式
	 */
	private String contentType;

	/**
	 * 时间戳
	 */
	private Long timeStamp;

	/**
	 * 消息签名
	 */
	private String sign;

	/**
	 * 消息公钥
	 */
	private String publicKey;

	/**
	 * 请求过来返回的标识
	 */
	private String requestId;

	/**
	 * 返回类型:all-多返回 void-不返回 ok-成功放回 false-失败返回
	 */
	private String returnType;

	/**
	 * all,group,single
	 */
	private String type;

	/**
	 * 目标,*,groupName,cid
	 * 多个组,逗号隔开
	 */
	private String target;

	/**
	 * shi编码消息体
	 */
	public PubMsgEncodeBO instanceEncode(){
		PubMsgEncodeBO encodeBO = new PubMsgEncodeBO();
		encodeBO.setType(type);
		encodeBO.setTarget(target);
		return encodeBO;
	}
}
