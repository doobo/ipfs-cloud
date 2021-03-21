package com.github.doobo.service;

import com.github.doobo.api.IpfsControllerApi;
import com.github.doobo.conf.IpfsConfig;
import com.github.doobo.model.IpfsPubVO;
import com.github.doobo.params.ResultTemplate;
import com.github.doobo.utils.ResultUtils;
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
	private IpfsConfigService ipfsConfigService;


	@Override
	public IpfsControllerApi create(Throwable throwable) {
		return new IpfsControllerApi() {
			@Override
			public ResultTemplate<Boolean> exitFile(String cid) {
				return ResultUtils.of(Boolean.FALSE);
			}

			@Override
			public IpfsConfig getIpfsConfig() {
				return ipfsConfigService.queryIpfsConfig();
			}

			@Override
			public List<IpfsConfig> queryNodeConfigList() {
				return ipfsConfigService.queryNodeConfigList();
			}

			@Override
			public ResultTemplate<Boolean> pubMsg(IpfsPubVO vo) {
				return ResultUtils.of(Boolean.FALSE);
			}
		};
	}
}
