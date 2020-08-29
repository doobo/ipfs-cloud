package com.github.doobo.controller;

import com.github.doobo.api.IpfsSearchControllerApi;
import com.github.doobo.model.IpfsFileInfo;
import com.github.doobo.model.SearchVO;
import com.github.doobo.params.ResultTemplate;
import com.github.doobo.service.IpfsSearchService;
import com.github.doobo.utils.ResultUtils;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class IndexController implements IpfsSearchControllerApi {

	@Resource(name = "ipfsSearch")
	ClientInterface clientInterface;

	@Resource
	IpfsSearchService ipfsSearchService;

	@GetMapping("")
	public ResultTemplate<Object> indexPage(){
		return ResultUtils.of(clientInterface.existIndice("IpfsSearchDocument"));
	}

	@Override
	public ResultTemplate<Boolean> saveFileInfo(IpfsFileInfo fileInfo) {
		return ipfsSearchService.saveFileInfo(fileInfo);
	}

	@Override
	public ResultTemplate<List<IpfsFileInfo>> search(SearchVO vo) {
		return ipfsSearchService.search(vo);
	}
}
