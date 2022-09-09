package com.github.doobo.bo;

import com.github.doobo.vbo.ResultMergeBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 被观察的入参数
 *
 * @Description: ipfs-cloud
 * @User: doobo
 * @Time: 2022-09-09 13:47
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PlatformObserverRequest extends ResultMergeBo {

	/**
	 * 消息唯一ID
	 */
	private Long id;

	/**
	 * 执行级别
	 */
	private int level;

	/**
	 * start-启动
	 */
	private String type;

	/**
	 * 监听到到消息体
	 */
	private String msg;

	/**
	 * 配置目录:.ipfs
	 */
	private String configDir;

	/**
	 * 程序路径
	 */
	private String exePath;
}
