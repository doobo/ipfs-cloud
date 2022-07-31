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
	private boolean privateNetwork;

	/**
	 * 私有秘钥
	 */
	private String swarmKey;

	/**
	 * 是否启动go-ipfs
	 */
	private boolean startDaemon;

	/**
	 * 是否写.ipfs文件
	 */
	private boolean writeFile;

	/**
	 * 广播节点
	 */
	private String topic;

	/**
	 * ipfs 初始化路径
	 */
	private String path;

	/**
	 * 启用定时任务
	 */
	private boolean cron;

	/**
	 *  间隔多少时间执行，单位秒
	 */
	private Integer fixedDelay;

	/**
	 * 延迟多长时间启动定时任务,单位秒
	 */
	private Integer delay;

	private String publicKey;

	private String privateKey;

	private String ownPublicKey;

	private String ownPrivateKey;
}
