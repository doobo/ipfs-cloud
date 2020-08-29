package com.github.doobo.controller;

import com.github.doobo.api.IpfsControllerApi;
import com.github.doobo.conf.IpfsConfig;
import com.github.doobo.service.IpfsConfigService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;


@RestController
public class IpfsController implements IpfsControllerApi {

	@Resource
	IpfsConfigService ipfsConfigService;

	public IpfsConfig getIpfsConfig(){
		return ipfsConfigService.queryIpfsConfig();
	}

	/**
	 * 获取Ipfs的基础配置
	 */
	@GetMapping("/next")
	public Mono<IpfsConfig> getIpfsConfig2(){
		return Mono.just(ipfsConfigService.queryIpfsConfig());
	}

	/**
	 * 获取Ipfs所有节点配置
	 */
	@Override
	public List<IpfsConfig> queryNodeConfigList() {
		return ipfsConfigService.queryNodeConfigList();
	}

}
