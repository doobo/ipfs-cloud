package com.github.doobo.controller;

import com.github.doobo.bo.IpfsProperties;
import com.github.doobo.config.IpfsInitConfig;
import com.github.doobo.factory.PlatformInitFactory;
import com.github.doobo.handler.PlatformInitHandler;
import com.github.doobo.utils.ResultUtils;
import com.github.doobo.vbo.ResultTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ipfs/union")
public class UnionPageController {

	/**
	 * 获取IPFS基本属性
	 */
	@GetMapping("/properties")
	public ResultTemplate<IpfsProperties> getProperties(){
		return ResultUtils.of(IpfsInitConfig.getIpfsProperties());
	}

	/**
	 * 获取初始化实现类
	 */
	@GetMapping("/handlerList")
	public ResultTemplate<List<PlatformInitHandler>> getInitHandler(){
		return ResultUtils.ofUnsafeList(PlatformInitFactory.getHandlerList());
	}
}
