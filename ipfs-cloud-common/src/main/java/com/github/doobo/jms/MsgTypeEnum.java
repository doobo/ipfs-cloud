package com.github.doobo.jms;

import lombok.Getter;
import lombok.Setter;

/**
 * 消息体内容
 */
public enum  MsgTypeEnum {
	TXT("txt","文本"),
	MAP("map","键值对"),
	MIX("mix","混合体"),
	FIND("find","寻址")
	;

	MsgTypeEnum(String type, String desc) {
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
