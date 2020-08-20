package com.github.doobo.controller;

import com.github.doobo.conf.IpfsConfig;
import com.github.doobo.service.IpfsConfigApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class IpfsConfigController {

	@Resource
	IpfsConfigApiService ipfsConfigApiService;

	@GetMapping("/ipfs/nodes")
	public List<IpfsConfig> queryNodesConfig(){
		return ipfsConfigApiService.queryNodeConfigList();
	}
}
