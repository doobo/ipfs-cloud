package com.github.doobo.controller;

import com.github.doobo.api.IpfsControllerApi;
import com.github.doobo.conf.IpfsConfig;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
public class IpfsController implements IpfsControllerApi {

	@Resource
	IpfsConfig ipfsConfig;

	/**
	 * 获取Ipfs的基础配置
	 */
	public IpfsConfig getIpfsConfig(){
		return ipfsConfig;
	}
}
