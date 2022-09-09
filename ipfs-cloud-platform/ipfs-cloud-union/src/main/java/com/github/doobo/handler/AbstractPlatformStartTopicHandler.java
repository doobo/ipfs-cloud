package com.github.doobo.handler;

import com.github.doobo.abs.AbstractPlatformObserver;
import com.github.doobo.bo.PlatformObserverRequest;
import com.github.doobo.bo.PlatformStartRequest;
import com.github.doobo.vbo.ResultTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Observable;
import java.util.Optional;

/**
 * 平台消息处理器
 *
 * @Description: ipfs-cloud
 * @User: doobo
 * @Time: 2022-09-09 15:41
 */
@Slf4j
public abstract class AbstractPlatformStartTopicHandler extends AbstractPlatformObserver {

	/**
	 * ipfs启动完成后,执行Topic启动
	 */
	@Override
	public boolean matching(PlatformObserverRequest request) {
		return "start".equals(request.getType()) && "Daemon is ready".equals(request.getMsg());
	}

	@Override
	public void executor(PlatformObserverRequest request, Observable o) {
		log.info("ipfs:{}:{}",request.getType(), request.getMsg());
		PlatformStartRequest startRequest = new PlatformStartRequest();
		startRequest.setExePath(Optional.ofNullable(request.getExePath())
			.filter(StringUtils::isNotBlank)
			.orElse(AbstractPlatformInitHandler.RESPONSE.getExePath()));
		startRequest.setConfigDir(Optional.ofNullable(request.getConfigDir())
			.filter(StringUtils::isNotBlank)
			.orElse(AbstractPlatformInitHandler.RESPONSE.getConfigDir()));
		startRequest.setInfo(AbstractPlatformInitHandler.RESPONSE.getInfo());
		startRequest.setProperties(AbstractPlatformInitHandler.RESPONSE.getIpfsConfig());
		try {
			ResultTemplate<Boolean> template = AbstractPlatformInitHandler.startIpfsTopic(startRequest);
			Optional.ofNullable(template).filter(f -> !f.isSuccess()).ifPresent(m -> log.error("startIpfsTopicError:{}", m));
		}catch (Exception e){
			log.error("startIpfsTopicError:", e);
		}
	}
}
