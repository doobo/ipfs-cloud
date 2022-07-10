package com.github.doobo.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Objects;

@Data
@Accessors(chain = true)
public class PathVO {

	/**
	 * 目录ID
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
		PathVO pathVO = (PathVO) o;
		return Objects.equals(hash, pathVO.hash);
	}

	@Override
	public int hashCode() {
		return Objects.hash(hash);
	}
}
