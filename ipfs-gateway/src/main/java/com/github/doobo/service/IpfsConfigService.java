package com.github.doobo.service;

import com.github.doobo.conf.IpfsConfig;

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
	 * @return
	 */
	List<IpfsConfig> queryNodeConfigList();
}
