package com.github.doobo.conf;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ipfs常用基本配置
 */
@Data
@Component
@Accessors(chain = true)
@ConfigurationProperties(prefix = "ipfs")
public class IpfsConfig {

	/**
	 * ipfs默认寻址地址
	 */
	private String[] bootstrap;

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
	 * 节点信息
	 */
	private List<Node> nodes;

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

	private String sm2PublicKey;

	private String sm2PrivateKey;

	private String selfSm2PublicKey;

	private String selfSm2PrivateKey;
}
