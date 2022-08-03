package com.github.doobo.bo;

/**
 * 启动返回参数
 *
 * @Description: ipfs-cloud
 * @User: doobo
 * @Time: 2022-07-29 11:28
 */
public class PlatformInitResponse implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 配置目录:.ipfs
	 */
	private String ipfsDir;

	/**
	 * 程序路径
	 */
	private String ipfsPath;

	/**
	 * 节点信息
	 */
	private IpfsNodeInfo info;
}
