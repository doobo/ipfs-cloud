package com.github.doobo.controller;

import com.github.doobo.conf.IpfsConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("ipfs")
@RestController
public class IpfsController {

	@Resource
	IpfsConfig ipfsConfig;

	/**
	 * 获取Ipfs的基础配置
	 */
	@GetMapping("config")
	public IpfsConfig getIpfsConfig(){
		return ipfsConfig;
	}
}
