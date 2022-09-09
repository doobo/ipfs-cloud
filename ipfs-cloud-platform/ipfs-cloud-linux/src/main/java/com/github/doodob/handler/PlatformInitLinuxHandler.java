package com.github.doodob.handler;

import com.github.doobo.bo.PlatformInitRequest;
import com.github.doobo.bo.PlatformInitResponse;
import com.github.doobo.bo.PlatformStartRequest;
import com.github.doobo.handler.AbstractPlatformInitHandler;
import com.github.doobo.vbo.ResultTemplate;

import java.util.Objects;

/**
 * Linux系统处理器
 *
 * @Description: ipfs-cloud
 * @User: doobo
 * @Time: 2022-08-03 16:42
 */
public class PlatformInitLinuxHandler extends AbstractPlatformInitHandler {

	@Override
	public boolean matching(PlatformInitRequest request) {
		if(Objects.isNull(request) || Objects.isNull(request.getOsName())){
			return Boolean.FALSE;
		}
		return !request.getOsName().startsWith("Mac OS") && !request.getOsName().startsWith("Windows");
	}

	@Override
	public ResultTemplate<PlatformInitResponse> initIpfs(PlatformInitRequest request) {
		return null;
	}

	@Override
	public ResultTemplate<PlatformInitResponse> startIpfs(PlatformStartRequest request) {
		return null;
	}

	@Override
	public ResultTemplate<Boolean> startTopic(PlatformStartRequest request) {
		return null;
	}
}
