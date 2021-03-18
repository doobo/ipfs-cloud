package com.github.doobo.script;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 广播信息载体
 */
@Data
@Accessors(chain = true)
public class ObServerVO implements Serializable {

	/**
	 * ipfs标识
	 */
	private String ipfs;

	/**
	 * 消息标识
	 */
	private String tag;

	/**
	 * 行内容
	 */
	private String line;

	/**
	 * byte内容
	 */
	private byte[] out;

	private int off;

	private int len;

	private int level;
}
