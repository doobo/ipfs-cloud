package com.github.doobo.service;

import com.github.doobo.conf.IpfsConfig;
import com.github.doobo.model.IpfsPubVO;
import com.github.doobo.params.ResultTemplate;

import java.util.List;

/**
 * ipfs基础配置服务
 */
public interface IpfsConfigService {
	/**
	 *  获取Ipfs基础配置
	 */
	IpfsConfig queryIpfsConfig();

	/**
	 * 获取Ipfs所有节点配置
	 */
	List<IpfsConfig> queryNodeConfigList();

	/**
	 * 广播一条信息
	 */
	ResultTemplate<Boolean> pubMsg(IpfsPubVO vo);
}
