package com.github.doobo.api;

import com.github.doobo.conf.IpfsConfig;
import com.github.doobo.model.IpfsPubVO;
import com.github.doobo.params.ResultTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface IpfsControllerApi {

	/**
	 * 查询结点
	 */
	@GetMapping("/ipfs")
	ResultTemplate<Boolean> exitFile(String cid);

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

	/**
	 * 广播消息
	 */
	@PostMapping("/ipfs/pubMsg")
	ResultTemplate<Boolean> pubMsg(IpfsPubVO vo);


}
