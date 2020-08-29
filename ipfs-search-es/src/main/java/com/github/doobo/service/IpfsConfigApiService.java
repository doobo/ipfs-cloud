package com.github.doobo.service;

import com.github.doobo.api.IpfsControllerApi;
import com.github.doobo.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 远程服务调用
 */
@FeignClient(name = "ipfs-gateway", configuration = FeignConfig.class
	, fallbackFactory = IpfsConfigApiFallbackService.class)
public interface IpfsConfigApiService extends IpfsControllerApi {
}
