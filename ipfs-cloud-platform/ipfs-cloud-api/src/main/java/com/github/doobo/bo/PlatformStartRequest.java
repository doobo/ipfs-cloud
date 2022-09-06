package com.github.doobo.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ipfs启动入参
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PlatformStartRequest extends PlatformInitRequest{

	/**
	 * 配置目录:.ipfs
	 */
	private String configDir;

	/**
	 * 程序路径
	 */
	private String exePath;

	/**
	 * 节点信息
	 */
	private IpfsNodeInfo info;

	@Override
	public String toString() {
		return "PlatformStartRequest{" +
			"osName='" + this.getOsName() + '\'' +
			", extParams=" + this.getExtParams() +
			'}';
	}
}
