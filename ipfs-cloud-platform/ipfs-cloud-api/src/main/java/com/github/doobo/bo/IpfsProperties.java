package com.github.doobo.bo;

import lombok.Data;

import java.util.List;

/**
 * IPFS基本配置
 *
 * @Description: ipfs-cloud
 * @User: doobo
 * @Time: 2022-07-30 21:26
 */
@Data
public class IpfsProperties implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * ipfs默认寻址地址
	 */
	private List<String> bootstrap;

	/**
	 * ipfs的通信端口号
	 */
	private Integer port;

	/**
	 * ipfs的管理员端口号
	 */
	private Integer adminPort;

	/**
	 * ipfs的http端口号
	 */
	private Integer httpPort;

	/**
	 * 绑定的本机IP
	 */
	private String bindIp;

	/**
	 * 是否私有网络
	 */
	private Boolean privateNetwork;

	/**
	 * 私有秘钥
	 */
	private String swarmKey;

	/**
	 * 是否启动go-ipfs
	 */
	private Boolean startDaemon;

	/**
	 * 是否写.ipfs文件
	 */
	private Boolean writeFile;

	/**
	 * 广播节点
	 */
	private String topic;

	/**
	 * ipfs 初始化路径
	 */
	private String initDir;

	/**
	 * 启用定时任务
	 */
	private Boolean startTopic;

	private String publicKey;

	private String privateKey;

	private String ownPublicKey;

	private String ownPrivateKey;
}
