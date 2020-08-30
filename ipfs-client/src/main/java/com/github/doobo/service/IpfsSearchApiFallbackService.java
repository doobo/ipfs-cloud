package com.github.doobo.service;

import com.github.doobo.api.IpfsSearchControllerApi;
import com.github.doobo.model.IpfsFileInfo;
import com.github.doobo.model.SearchVO;
import com.github.doobo.params.ResultTemplate;
import com.github.doobo.utils.ResultUtils;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 熔断错误处理
 */
@Component
public class IpfsSearchApiFallbackService implements FallbackFactory<IpfsSearchControllerApi> {
	@Override
	public IpfsSearchControllerApi create(Throwable throwable) {
		return new IpfsSearchApiService() {
			@Override
			public ResultTemplate<Boolean> saveFileInfo(IpfsFileInfo fileInfo) {
				return  ResultUtils.ofThrowable(throwable);
			}

			@Override
			public ResultTemplate<List<IpfsFileInfo>> search(SearchVO vo) {
				return  ResultUtils.ofError(throwable, ResultUtils.singleList(IpfsFileInfo.class));
			}
		};
	}
}
