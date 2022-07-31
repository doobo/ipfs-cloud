package com.github.doobo.bo;

import lombok.Data;

import java.util.List;

/**
 * 平台初始化入参
 *
 * @Description: ipfs-cloud
 * @User: doobo
 * @Time: 2022-07-29 11:28
 */
@Data
public class PlatformInitRequest implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 系统名
	 */
	private String osName;

	/**
	 * IPFS初始化目录
	 */
	private String initDir;

	/**
	 * 启动扩展参数
	 */
	private List<String> extParams;

	/**
	 * 节点信息
	 */
	private IpfsNodeInfo info;
}
