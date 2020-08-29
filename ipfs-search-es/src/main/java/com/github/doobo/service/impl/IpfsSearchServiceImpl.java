package com.github.doobo.service.impl;

import com.github.doobo.model.IpfsFileInfo;
import com.github.doobo.model.IpfsSearchDocument;
import com.github.doobo.model.SearchVO;
import com.github.doobo.params.ResultTemplate;
import com.github.doobo.service.IpfsSearchService;
import com.github.doobo.utils.DateUtils;
import com.github.doobo.utils.ResultUtils;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.frameworkset.elasticsearch.client.ClientOptions;
import org.springframework.beans.BeanUtils;
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

	@Override
	public ResultTemplate<Boolean> saveFileInfo(IpfsFileInfo fileInfo) {
		IpfsSearchDocument document = new IpfsSearchDocument();
		BeanUtils.copyProperties(fileInfo, document);
		document.setCreateTime(DateUtils.getCurDate()).setUpdateTime(DateUtils.getCurDate());
		clientInterface.addDocument(document, clientOptions);
		return ResultUtils.of(Boolean.TRUE);
	}

	@Override
	public ResultTemplate<List<IpfsFileInfo>> search(SearchVO vo) {
		return null;
	}
}
