package com.github.doobo.service.impl;

import com.github.doobo.model.IpfsFileInfo;
import com.github.doobo.model.IpfsSearchDocument;
import com.github.doobo.model.SearchVO;
import com.github.doobo.params.ResultTemplate;
import com.github.doobo.service.IpfsSearchService;
import com.github.doobo.utils.DateUtils;
import com.github.doobo.utils.EsDataUtils;
import com.github.doobo.utils.ResultUtils;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.frameworkset.elasticsearch.client.ClientOptions;
import org.frameworkset.elasticsearch.entity.ESDatas;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * search相关逻辑
 */
@Service
public class IpfsSearchServiceImpl implements IpfsSearchService {

	@Resource(name = "ipfsSearch")
	private ClientInterface clientInterface;

	@Resource
	private ClientOptions clientOptions;

	private final static String IPFS_SEARCH_INDEX = "ipfs_search_document/_search";

	/**
	 * 保存记录到ES
	 * @param fileInfo
	 */
	@Override
	public ResultTemplate<Boolean> saveFileInfo(IpfsFileInfo fileInfo) {
		IpfsSearchDocument document = new IpfsSearchDocument();
		BeanUtils.copyProperties(fileInfo, document);
		document.setCreateTime(DateUtils.getCurDate()).setUpdateTime(DateUtils.getCurDate());
		clientInterface.addDocument(document, clientOptions);
		return ResultUtils.of(Boolean.TRUE);
	}

	/**
	 * ES结果搜索
	 * @param vo
	 */
	@Override
	public ResultTemplate<List<IpfsFileInfo>> search(SearchVO vo) {
		ESDatas<IpfsFileInfo> eData = clientInterface
			.searchList(IPFS_SEARCH_INDEX,"ipfsSearch", BeanMap.create(vo), IpfsFileInfo.class);
		return EsDataUtils.ofESDataAndPage(eData, vo);
	}
}
