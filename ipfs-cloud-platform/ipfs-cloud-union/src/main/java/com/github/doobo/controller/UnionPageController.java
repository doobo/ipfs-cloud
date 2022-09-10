package com.github.doobo.controller;

import com.github.doobo.bo.IpfsProperties;
import com.github.doobo.bo.PubMessageBO;
import com.github.doobo.bo.PubMsgEncodeBO;
import com.github.doobo.config.IpfsInitConfig;
import com.github.doobo.config.IpfsInitDefaultConfig;
import com.github.doobo.factory.PlatformInitFactory;
import com.github.doobo.handler.AbstractPlatformInitHandler;
import com.github.doobo.handler.PlatformInitHandler;
import com.github.doobo.utils.ResultUtils;
import com.github.doobo.vbo.ResultTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ipfs/union")
public class UnionPageController {

	/**
	 * 获取IPFS默认属性
	 */
	@GetMapping("/default")
	public ResultTemplate<IpfsProperties> getDefaultProperties(){
		return ResultUtils.of(IpfsInitDefaultConfig.getDefaultIpfsProperties());
	}

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

	/**
	 * 发送消息
	 */
	@PostMapping("pub")
	public ResultTemplate<PubMsgEncodeBO> pubMessage(@RequestBody PubMessageBO bo){
		return AbstractPlatformInitHandler.pubMessage(bo);
	}
}
