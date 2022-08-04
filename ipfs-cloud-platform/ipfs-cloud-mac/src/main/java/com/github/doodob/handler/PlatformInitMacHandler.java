package com.github.doodob.handler;

import com.github.doobo.bo.PlatformInitRequest;
import com.github.doobo.bo.PlatformInitResponse;
import com.github.doobo.bo.PlatformStartRequest;
import com.github.doobo.handler.AbstractPlatformInitHandler;
import com.github.doobo.vbo.ResultTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * Mac系统处理器
 *
 * @Description: ipfs-cloud
 * @User: doobo
 * @Time: 2022-08-03 16:48
 */
@Slf4j
public class PlatformInitMacHandler extends AbstractPlatformInitHandler {

	@Override
	public boolean matching(PlatformInitRequest request) {
		if(Objects.isNull(request) || Objects.isNull(request.getOsName())){
			return Boolean.FALSE;
		}
		return request.getOsName().startsWith("Mac OS");
	}

	@Override
	public ResultTemplate<PlatformInitResponse> initIpfs(PlatformInitRequest request) {
		log.info("initIpfs start:{}", request);
		return null;
	}

	@Override
	public ResultTemplate<PlatformInitResponse> startIpfs(PlatformStartRequest request) {
		log.info("startIpfs start:{}", request);
		return null;
	}

	@Override
	public ResultTemplate<PlatformInitResponse> startTopic(PlatformInitRequest request) {
		log.info("startTopic start:{}", request);
		return null;
	}
}
