package com.github.doobo.service;

import com.github.doobo.api.IpfsSearchControllerApi;
import com.github.doobo.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;


/**
 * ipfs搜索服务
 */
@FeignClient(name = "ipfs-search", configuration = FeignConfig.class
	, fallbackFactory = IpfsSearchApiFallbackService.class)
public interface IpfsSearchApiService extends IpfsSearchControllerApi {
}
