package com.github.doobo.vbo;

import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
public class PathRequest implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 标识别ID
	 */
	private String ipfs;

	/**
	 * 目录hash
	 */
	private String hash;

	/**
	 * 目录类型
	 */
	private String mime = "dir";

	/**
	 * 目录名
	 */
	private String name;

	/**
	 * 目录
	 */
	private String path;

	/**
	 * 目标目录
	 */
	private String target;

	/**
	 * 目录摘要,方便搜索
	 */
	private List<String> remarks;

	/**
	 * 时间戳
	 */
	private Long ts;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PathRequest that = (PathRequest) o;
		return hash.equals(that.hash);
	}

	@Override
	public int hashCode() {
		return Objects.hash(hash);
	}
}
