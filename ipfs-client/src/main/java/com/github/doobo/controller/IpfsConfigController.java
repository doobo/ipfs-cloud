package com.github.doobo.controller;

import com.github.doobo.conf.IpfsConfig;
import com.github.doobo.service.IpfsConfigApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class IpfsConfigController {

	@Resource
	IpfsConfigApiService ipfsConfigApiService;

	@GetMapping("/ipfs/config")
	public IpfsConfig queryConfig(){
		return ipfsConfigApiService.getIpfsConfig();
	}
}
