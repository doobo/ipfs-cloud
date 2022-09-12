package com.github.doobo.handler;

import com.github.doobo.bo.PlatformInitRequest;
import com.github.doobo.bo.PlatformInitResponse;
import com.github.doobo.bo.PlatformStartRequest;
import com.github.doobo.vbo.ResultTemplate;

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

	default String getName(){
		return this.getClass().getName();
	}

	/**
	 * 简单判断处理器是否匹配
	 */
	default boolean matching(PlatformInitRequest request){
		return Boolean.FALSE;
	}

	/**
	 * 初始化IPFS
	 */
	ResultTemplate<PlatformInitResponse> initIpfs(PlatformInitRequest request);

	/**
	 * 启动IPFS
	 */
	ResultTemplate<PlatformInitResponse> startIpfs(PlatformStartRequest request);

	/**
	 * 	停止IPFS
	 */
	ResultTemplate<Boolean> stopIpfs(PlatformStartRequest request);

	/**
	 * 重启IPFS
	 */
	ResultTemplate<PlatformInitResponse> restartIpfs(PlatformStartRequest request);

	/**
	 * 开启监听
	 */
	ResultTemplate<Boolean> startTopic(PlatformStartRequest request);

	/**
	 * 停止监听
	 */
	ResultTemplate<Boolean> stopTopic(PlatformInitRequest request);

	/**
	 * 执行IPFS命令,并返回结果
	 */
	ResultTemplate<String> execIpfsCmd(PlatformInitRequest request);
}
