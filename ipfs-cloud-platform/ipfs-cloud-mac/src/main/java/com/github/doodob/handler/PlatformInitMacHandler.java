package com.github.doodob.handler;

import com.github.doobo.bo.PlatformInitRequest;
import com.github.doobo.bo.PlatformInitResponse;
import com.github.doobo.bo.PlatformStartRequest;
import com.github.doobo.handler.AbstractPlatformInitHandler;
import com.github.doobo.vbo.ResultTemplate;

public class PlatformInitMacHandler extends AbstractPlatformInitHandler {



	@Override
	public ResultTemplate<PlatformInitResponse> initIpfs(PlatformInitRequest request) {
		return null;
	}

	@Override
	public ResultTemplate<PlatformInitResponse> startIpfs(PlatformStartRequest request) {
		return null;
	}

	@Override
	public ResultTemplate<PlatformInitResponse> stopIpfs(PlatformStartRequest request) {
		return null;
	}

	@Override
	public ResultTemplate<PlatformInitResponse> restartIpfs(PlatformStartRequest request) {
		return null;
	}

	@Override
	public ResultTemplate<PlatformInitResponse> startTopic(PlatformInitRequest request) {
		return null;
	}

	@Override
	public ResultTemplate<PlatformInitResponse> stopTopic(PlatformInitRequest request) {
		return null;
	}
}
