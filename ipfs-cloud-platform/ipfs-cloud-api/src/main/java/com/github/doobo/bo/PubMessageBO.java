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
	private String origin;

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
}
