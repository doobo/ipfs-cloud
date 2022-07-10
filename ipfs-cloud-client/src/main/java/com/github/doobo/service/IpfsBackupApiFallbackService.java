package com.github.doobo.service;

import com.github.doobo.api.IpfsBackupControllerApi;
import com.github.doobo.model.IpfsFileInfo;
import com.github.doobo.params.ResultTemplate;
import com.github.doobo.utils.ResultUtils;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 远程备份服务调用异常
 */
@Component
public class IpfsBackupApiFallbackService implements FallbackFactory<IpfsBackupControllerApi> {

	@Override
	public IpfsBackupControllerApi create(Throwable throwable) {
		return new IpfsBackupControllerApi() {
			@Override
			public ResultTemplate<Boolean> backUpFile(IpfsFileInfo info) {
				return ResultUtils.ofThrowable(throwable);
			}
		};
	}
}
