package com.github.doobo.handler;

import com.github.doobo.bo.PlatformInitResponse;
import com.github.doobo.bo.PlatformStartRequest;
import com.github.doobo.factory.PlatformInitFactory;
import com.github.doobo.vbo.ResultTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;

import java.util.Optional;

/**
 * IPFS启动抽象类
 *
 * @Description: ipfs-cloud
 * @User: diding
 * @Time: 2022-08-04 17:47
 */
@Slf4j
public abstract class AbstractIpfsInitLifecycle implements SmartLifecycle {

	private volatile boolean isRunning;

	@Override
	public int getPhase() {
		return PlatformInitHandler.DEFAULT_PHASE;
	}

	@Override
	public void start() {
		isRunning = true;
		log.info("ipfs init server start...");
		PlatformStartRequest request = new PlatformStartRequest();
		request.setOsName(System.getProperty("os.name"));
		ResultTemplate<PlatformInitResponse> result = AbstractPlatformInitHandler.initAndStartIpfs(request);
		Optional.ofNullable(result).filter(f -> !f.isSuccess()).ifPresent(m -> log.error("initAndStartIpfsError:{}", m));
	}

	@Override
	public void stop() {
		isRunning = false;
		log.info("ipfs init server stop...");
		PlatformStartRequest request = new PlatformStartRequest();
		request.setOsName(System.getProperty("os.name"));
		ResultTemplate<Boolean> template = PlatformInitFactory.executeHandler(request, handler -> handler.stopIpfs(request));
		Optional.ofNullable(template).filter(f -> !f.isSuccess()).ifPresent(m -> log.error("stopIpfs:{}", m));
	}

	@Override
	public boolean isRunning() {
		return isRunning;
	}
}
