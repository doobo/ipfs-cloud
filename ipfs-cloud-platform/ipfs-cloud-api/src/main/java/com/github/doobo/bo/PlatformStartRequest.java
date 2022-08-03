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
	 * 节点信息
	 */
	private IpfsNodeInfo info;
}
