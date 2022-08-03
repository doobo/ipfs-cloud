package com.github.doobo.handler;

import com.github.doobo.bo.PlatformInitResponse;
import com.github.doobo.bo.PlatformStartRequest;
import com.github.doobo.config.IpfsInitConfig;
import com.github.doobo.factory.PlatformInitFactory;
import com.github.doobo.utils.ResultUtils;
import com.github.doobo.vbo.ResultTemplate;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ForkJoinPool;

/**
 * 平台初始化抽象类
 *
 * @Description: ipfs-cloud
 * @User: doobo
 * @Time: 2022-07-29 13:47
 */
public abstract class AbstractPlatformInitHandler implements PlatformInitHandler{

	private static ForkJoinPool startPool;

	/**
	 * 初始化并启动ipfs
	 */
	public static synchronized ResultTemplate<PlatformInitResponse> initAndStartIpfs(PlatformStartRequest request){
		if(Objects.isNull(request)){
			return ResultUtils.ofFail("入参异常:request");
		}
		request.setOsName(Optional.ofNullable(request.getOsName()).filter(StringUtils::isNotBlank)
			.orElseGet(()-> System.getProperty("os.name")));
		request.setProperties(Optional.ofNullable(request.getProperties()).orElseGet(IpfsInitConfig::getIpfsProperties));
		PlatformInitFactory.executeHandler(request, proxy->proxy.initIpfs(request));
		return null;
	}
}
