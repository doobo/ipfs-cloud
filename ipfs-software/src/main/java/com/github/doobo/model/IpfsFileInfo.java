package com.github.doobo.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 单个文件上传后返回信息
 */
@Data
@Accessors(chain = true)
public class IpfsFileInfo {

	/**
	 * 文件hash值
	 */
	@NotNull(message = "文件路径不能为空-hash")
	private String hash;

	/**
	 * ipfs的cid
	 */
	@NotNull(message = "文件的cid不能为空-ipfs")
	private String ipfs;

	/**
	 * 是否加锁
	 */
	private Integer locked;

	/**
	 * 文件类型
	 */
	private String mime;

	/**
	 * 文件目录
	 */
	private String name;

	/**
	 * 文件上传的目录
	 */
	private String phash;

	/**
	 * 可读权限
	 */
	private Integer read;

	/**
	 * 文件大小
	 */
	private Long size;

	/**
	 * 文件http地址
	 */
	private String tmb;

	/**
	 * 时间戳
	 */
	private Long ts;

	private Integer write;
}
