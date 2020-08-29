package com.github.doobo.service;

import com.github.doobo.model.IpfsFileInfo;
import com.github.doobo.model.SearchVO;
import com.github.doobo.params.ResultTemplate;

import java.util.List;

public interface IpfsSearchService {

	/**
	 * 保存文档信息
	 * @param fileInfo
	 */
	ResultTemplate<Boolean> saveFileInfo(IpfsFileInfo fileInfo);

	/**
	 * 文件搜索
	 * @param vo
	 */
	ResultTemplate<List<IpfsFileInfo>> search(SearchVO vo);
}
