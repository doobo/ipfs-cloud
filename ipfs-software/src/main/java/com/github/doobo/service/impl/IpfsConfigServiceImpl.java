package com.github.doobo.service.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.doobo.conf.IpfsConfig;
import com.github.doobo.model.IpfsPubVO;
import com.github.doobo.params.ResultTemplate;
import com.github.doobo.script.PwdUtils;
import com.github.doobo.service.IpfsConfigService;
import com.github.doobo.soft.InitUtils;
import com.github.doobo.soft.SequenceUtils;
import com.github.doobo.soft.SystemClock;
import com.github.doobo.utils.CommonUtils;
import com.github.doobo.utils.OsUtils;
import com.github.doobo.utils.ResultUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import vip.ipav.okhttp.OkHttpClientTools;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * ipfs基础配置服务
 */
@Slf4j
@Service
public class IpfsConfigServiceImpl implements IpfsConfigService {

	@Resource
	private IpfsConfig ipfsConfig;

	@Value("${spring.application.name}")
	private String instance;

	@Resource
	private DiscoveryClient discoveryClient;

	/**
	 *  获取单个Ipfs网关基础配置
	 */
	@Override
	public IpfsConfig queryIpfsConfig() {
		IpfsConfig config = new IpfsConfig();
		BeanUtils.copyProperties(ipfsConfig, config);
		config.setTopic(null);
		config.setSwarmKey(null);
		config.setPath(null);
		config.setSm2PublicKey(config.getSm2PublicKey());
		config.setSm2PrivateKey(null);
		config.setSelfSm2PrivateKey(null);
		config.setNodes(Collections.singletonList(InitUtils.getIpfsNodeInfo()));
		return config;
	}

	/**
	 * 获取Ipfs所有节点配置
	 */
	@Override
	public List<IpfsConfig> queryNodeConfigList() {
		List<ServiceInstance> ls = discoveryClient.getInstances(instance);
		List<IpfsConfig> result = Lists.newArrayList();
		ObjectMapper objectMapper = new ObjectMapper();
		if(CommonUtils.hasValue(ls)){
			IpfsConfig config;
			String res;
			for(ServiceInstance item : ls){
				//检测端口号是否可访问
				if(!OsUtils.checkIpPortOpen(item.getHost(), item.getPort())){
					continue;
				}
				try {
					res = Objects.requireNonNull(OkHttpClientTools.getInstance()
						.get()
						.url(item.getUri() + "/ipfs/config")
						.execute().body()).string();
					config = objectMapper.readValue(res, IpfsConfig.class);
					if(config != null && config.getNodes() != null){
						config.getNodes().forEach(m->{
							m.setIp(item.getHost());
						});
						result.add(config);
					}
				} catch (Exception e) {
					log.error("queryNodeConfigListError", e);
				}
			}
		}
		return result;
	}


	/**
	 * 广播信息
	 */
	@Override
	public ResultTemplate<Boolean> pubMsg(IpfsPubVO vo){
		if(vo == null){
			return ResultUtils.of(Boolean.FALSE);
		}
		vo.setId(SequenceUtils.nextId());
		vo.setIpfs(InitUtils.getNodeId());
		vo.setTime(SystemClock.now());
		if(StringUtils.isBlank(vo.getTopic())) {
			vo.setTopic(ipfsConfig.getTopic());
		}
		String msg = PwdUtils.encode(JSON.toJSONString(vo),  ipfsConfig.getSm2PublicKey());
		InitUtils.pubMsg(vo.getTopic(), msg);
		return ResultUtils.of(Boolean.TRUE);
	}
}
