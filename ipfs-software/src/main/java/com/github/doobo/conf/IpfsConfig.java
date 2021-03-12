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
}
