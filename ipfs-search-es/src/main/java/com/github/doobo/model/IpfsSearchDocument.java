package com.github.doobo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.frameworkset.orm.annotation.ESId;
import com.frameworkset.orm.annotation.ESIndex;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * ES保存的文档文档
 */
@Data
@Accessors(chain = true)
@ESIndex(name="ipfs_search_document")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IpfsSearchDocument extends IpfsFileInfo{

	/**
	 * 文档ID
	 */
	@ESId
	private String ipfs;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 更新时间
	 */
	private Date updateTime;

	/**
	 * 删除标记
	 */
	private int deleteMark;
}
