package com.github.doobo.service;

import com.github.doobo.api.IpfsControllerApi;
import com.github.doobo.conf.IpfsConfig;
import com.google.common.collect.Lists;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 熔断错误处理
 */
@Component
public class IpfsConfigApiFallbackService implements FallbackFactory<IpfsControllerApi> {

	@Resource
	IpfsConfig ipfsConfig;

	@Override
	public IpfsControllerApi create(Throwable throwable) {
		return new IpfsControllerApi() {
			@Override
			public IpfsConfig getIpfsConfig() {
				return ipfsConfig;
			}

			@Override
			public List<IpfsConfig> queryNodeConfigList() {
				return Lists.newArrayList(ipfsConfig);
			}
		};
	}
}
