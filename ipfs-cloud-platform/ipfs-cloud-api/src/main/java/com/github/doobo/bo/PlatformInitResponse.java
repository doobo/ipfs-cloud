package com.github.doobo.bo;

import lombok.Data;

/**
 * 启动返回参数
 *
 * @Description: ipfs-cloud
 * @User: doobo
 * @Time: 2022-07-29 11:28
 */
@Data
public class PlatformInitResponse implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 配置目录:.ipfs
	 */
	private String configDir;

	/**
	 * 程序路径
	 */
	private String exePath;

	/**
	 * 节点信息
	 */
	private IpfsNodeInfo info;
}
