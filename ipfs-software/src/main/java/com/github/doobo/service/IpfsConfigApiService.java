package com.github.doobo.service;

import com.github.doobo.conf.FeignConfig;
import com.github.doobo.conf.IpfsConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 远程服务调用
 */
@FeignClient(name = "ipfs-gateway", configuration = FeignConfig.class
	, fallbackFactory = IpfsConfigApiFallbackService.class)
public interface IpfsConfigApiService {
	/**
	 * 获取单个网关节点配置
	 */
	@GetMapping("/ipfs/config")
	IpfsConfig getIpfsConfig();

	/**
	 * 获取Ipfs所有节点配置
	 */
	@GetMapping("/ipfs/nodes")
	List<IpfsConfig> queryNodeConfigList();
}
