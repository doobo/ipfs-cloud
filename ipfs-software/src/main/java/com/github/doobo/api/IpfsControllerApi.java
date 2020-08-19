package com.github.doobo.api;

import com.github.doobo.conf.IpfsConfig;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public interface IpfsControllerApi {

	/**
	 * 获取单个网关节点配置
	 * @return
	 */
	@GetMapping("/ipfs/config")
	IpfsConfig getIpfsConfig();

	/**
	 * 获取Ipfs所有节点配置
	 * @return
	 */
	@GetMapping("/ipfs/nodes")
	List<IpfsConfig> queryNodeConfigList();
}
