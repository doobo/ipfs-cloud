package com.github.doobo.jms;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;
/**
 * 消息体
 */
@Data
@Accessors(chain = true)
public class ExchangeBody implements java.io.Serializable{

	/**
	 * 文本消息
	 */
	private String textMsg;

	/**
	 * 键值对消息
	 */
	private Map<String,Object> mapMsg;

	/**
	 * 消息属性
	 */
	private Map<String,Object> properties;

	/**
	 * 加密消息体
	 */
	private String cipherMsg;

	public ExchangeBody addProperty(String key, Object value){
		if(properties == null){
			properties = new HashMap<>();
		}
		properties.put(key, value);
		return this;
	}
}
