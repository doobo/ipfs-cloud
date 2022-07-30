package com.github.doobo.handler;

import com.github.doobo.bo.PlatformInitRequest;

/**
 * 平台初始化处理器
 *
 * @Description: ipfs-cloud
 * @User: doobo
 * @Time: 2022-07-29 11:24
 */
public interface PlatformInitHandler {

	int DEFAULT_PHASE = 1024;

	/**
	 * 优先级
	 */
	default int getPhase() {
		return DEFAULT_PHASE;
	}

	/**
	 * 简单判断处理器是否匹配
	 */
	default boolean matching(PlatformInitRequest request){
		return Boolean.TRUE;
	}
}
