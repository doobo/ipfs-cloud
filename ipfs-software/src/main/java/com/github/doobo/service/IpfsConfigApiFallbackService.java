package com.github.doobo.service;

import com.github.doobo.conf.IpfsConfig;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 熔断错误处理
 */
@Component
public class IpfsConfigApiFallbackService implements FallbackFactory<IpfsConfigApiService> {

	@Resource
	private IpfsConfigService ipfsConfigService;


	@Override
	public IpfsConfigApiService create(Throwable throwable) {
		return new IpfsConfigApiService() {

			@Override
			public IpfsConfig getIpfsConfig() {
				return ipfsConfigService.queryIpfsConfig();
			}

			@Override
			public List<IpfsConfig> queryNodeConfigList() {
				return ipfsConfigService.queryNodeConfigList();
			}
		};
	}
}
