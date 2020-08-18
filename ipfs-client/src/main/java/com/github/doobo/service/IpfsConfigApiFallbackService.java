package com.github.doobo.service;

import com.github.doobo.api.IpfsControllerApi;
import com.github.doobo.conf.IpfsConfig;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 熔断错误处理
 */
@Component
public class IpfsConfigApiFallbackService implements FallbackFactory<IpfsControllerApi> {

	@Override
	public IpfsControllerApi create(Throwable throwable) {
		return new IpfsControllerApi() {
			@Override
			public IpfsConfig getIpfsConfig() {
				return new IpfsConfig().setPort(1);
			}
		};
	}
}
