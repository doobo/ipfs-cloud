package com.github.doobo.jms;

import lombok.Getter;
import lombok.Setter;

/**
 * 消息体内容
 */
public enum RequestTypeEnum {
	P2P("P2P","点对点消息"),
	UDP("UDP","广播消息")
	;

	RequestTypeEnum(String type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	@Getter
	@Setter
	private String type;

	@Getter
	@Setter
	private String desc;

}
