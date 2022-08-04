package com.github.doobo.handler;

import com.github.doobo.bo.PlatformInitRequest;
import com.github.doobo.bo.PlatformInitResponse;
import com.github.doobo.bo.PlatformStartRequest;
import com.github.doobo.config.IpfsInitConfig;
import com.github.doobo.config.IpfsInitDefaultConfig;
import com.github.doobo.factory.PlatformInitFactory;
import com.github.doobo.utils.ResultUtils;
import com.github.doobo.vbo.ResultTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * 平台初始化抽象类
 *
 * @Description: ipfs-cloud
 * @User: doobo
 * @Time: 2022-07-29 13:47
 */
@Slf4j
public abstract class AbstractPlatformInitHandler implements PlatformInitHandler{

	private static ForkJoinPool startPool;

	private static ForkJoinPool topicPool;

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
		ResultTemplate<PlatformInitResponse> result = PlatformInitFactory.executeHandler(request, proxy -> proxy.initIpfs(request));
		if(Objects.isNull(result) || !result.isSuccess()){
			return result;
		}
		request.setInfo(Optional.ofNullable(result.getData()).orElse(new PlatformInitResponse()).getInfo());
		startPool = Optional.ofNullable(startPool)
			.filter(startPool-> !startPool.isShutdown())
			.orElseGet(IpfsInitDefaultConfig::createForkJoinPool);
		startPool.execute(()-> PlatformInitFactory.executeHandler(request, handler-> handler.startIpfs(request)));
		return result;
	}

	/**
	 * 初始化并启动ipfs
	 */
	public static synchronized ResultTemplate<Boolean> startIpfsTopic(PlatformStartRequest request){
		if(Objects.isNull(request)){
			return ResultUtils.ofFail("入参异常:request");
		}
		request.setOsName(Optional.ofNullable(request.getOsName()).filter(StringUtils::isNotBlank)
			.orElseGet(()-> System.getProperty("os.name")));
		request.setProperties(Optional.ofNullable(request.getProperties()).orElseGet(IpfsInitConfig::getIpfsProperties));
		topicPool = Optional.ofNullable(topicPool)
			.filter(startPool-> !startPool.isShutdown())
			.orElseGet(IpfsInitDefaultConfig::createForkJoinPool);
		topicPool.execute(()-> PlatformInitFactory.executeHandler(request, handler-> handler.startTopic(request)));
		return ResultUtils.of(Boolean.TRUE);
	}

	/**
	 * 重启IPFS
	 */
	@Override
	public ResultTemplate<PlatformInitResponse> restartIpfs(PlatformStartRequest request) {
		try {
			ResultTemplate<Boolean> template = this.stopIpfs(request);
			log.info("stopIpfs:{}", template);
			TimeUnit.SECONDS.sleep(3);
		} catch (Exception e) {
			log.error("stopIpfs error:", e);
		}
		return AbstractPlatformInitHandler.initAndStartIpfs(request);
	}

	/**
	 * 	停止IPFS
	 */
	@Override
	public ResultTemplate<Boolean> stopIpfs(PlatformStartRequest request) {
		this.stopTopic(request);
		Optional.ofNullable(startPool).ifPresent(ForkJoinPool::shutdownNow);
		return ResultUtils.of(Boolean.TRUE);
	}


	/**
	 * 停止监听
	 */
	@Override
	public ResultTemplate<Boolean> stopTopic(PlatformInitRequest request) {
		Optional.ofNullable(topicPool).ifPresent(ForkJoinPool::shutdownNow);
		return ResultUtils.of(Boolean.TRUE);
	}
}
