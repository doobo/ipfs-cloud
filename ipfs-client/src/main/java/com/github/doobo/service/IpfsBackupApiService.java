package com.github.doobo.service;

import com.github.doobo.api.IpfsBackupControllerApi;
import com.github.doobo.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 远程备份服务调用
 */
@FeignClient(name = "ipfs-backup", configuration = FeignConfig.class
	, fallbackFactory = IpfsBackupApiFallbackService.class)
public interface IpfsBackupApiService extends IpfsBackupControllerApi {
}
