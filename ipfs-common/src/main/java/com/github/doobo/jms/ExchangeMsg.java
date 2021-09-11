package com.github.doobo.jms;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 消息交换体
 */
@Data
@Accessors(chain = true)
public class ExchangeMsg implements java.io.Serializable{

	/**
	 * 请求ID，原则上原样返回，标识一个链路来回
	 */
	private Long requestId;

	/**
	 * 消息唯一ID,原则上递增
	 */
	private Long msgId;

	/**
	 * 保留字段,可以用来标识同一个端
	 */
	private String sessionId;

	/**
	 * 当前时间戳
	 */
	private Long timeStamp;

	/**
	 * 消息主题
	 */
	private String topic;

	/**
	 * 消息tag
	 */
	private List<String> tags;

	/**
	 * 所在的区域ID
	 */
	private String regionId;

	/**
	 * 组名称
	 */
	private String groupName;

	/**
	 * 过期时间，long，保留
	 */
	private Integer priority;

	/**
	 * 应用ID，保留
	 */
	private String appId;

	/**
	 * 当前时间戳
	 */
	private Long expiration;

	/**
	 * 请求类型，可选值：P2P(点对点消息),UDP(广播消息)
	 * {@link com.github.doobo.jms.RequestTypeEnum}
	 */
	private String requestType;

	/**
	 * 消息类型，txt(文本)，map(键值对)，find（寻址，topic，tags，regionId，groupName匹配）
	 * {@link com.github.doobo.jms.MsgTypeEnum}
	 */
	private String msgType;

	/**
	 * 消息索引key，根据该key可快速检索消息
	 */
	private List<String> keys;

	/**
	 * 消息体的长度,txt的length或map的size
	 */
	private Long bodyLength;

	/**
	 * 发送源Cid
	 */
	private String originCid;

	/**
	 * 目标源Cid，P2P时需要
	 */
	private String targetCid;

	/**
	 * 内容消息体加密使用的公钥
	 */
	private String publicKey;

	/**
	 * 消息签名，timeStamp,originCid,topic,texMsg or mapMsg,properties,msgType
	 */
	private String sign;

	/**
	 * 消息体
	 */
	private ExchangeBody body;
}
