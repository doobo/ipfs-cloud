package com.github.doobo.bo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 平台初始化入参
 *
 * @Description: ipfs-cloud
 * @User: doobo
 * @Time: 2022-07-29 11:28
 */
@Data
public class PlatformInitRequest implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 系统名
	 */
	private String osName;

	/**
	 * 启动扩展参数
	 */
	private List<String> extParams;

	/**
	 * 启动参数配置
	 */
	private IpfsProperties properties;

	/**
	 * 添加扩展参数
	 */
	public PlatformInitRequest addExtParam(String code){
		extParams = Optional.ofNullable(extParams).orElseGet(ArrayList::new);
		extParams.add(code);
		return this;
	}
}
