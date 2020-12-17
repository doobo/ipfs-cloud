package com.github.doobo.controller;

import com.github.doobo.api.IpfsSearchControllerApi;
import com.github.doobo.model.IpfsFileInfo;
import com.github.doobo.model.SearchVO;
import com.github.doobo.params.ResultTemplate;
import com.github.doobo.service.IpfsSearchService;
import com.github.doobo.utils.DateUtils;
import com.github.doobo.utils.ResultUtils;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
public class IndexController implements IpfsSearchControllerApi {

	@Resource(name = "ipfsSearch")
	ClientInterface clientInterface;

	@Resource
	IpfsSearchService ipfsSearchService;

	//@GetMapping("")
	public ResultTemplate<Object> indexPage(){
		return ResultUtils.of(clientInterface.existIndice("ipfs_search_document"));
	}

	@Override
	public ResultTemplate<Boolean> saveFileInfo(@RequestBody IpfsFileInfo fileInfo) {
		return ipfsSearchService.saveFileInfo(fileInfo);
	}

	@Override
	public ResultTemplate<List<IpfsFileInfo>> search(@RequestBody SearchVO vo) {
		return ipfsSearchService.search(vo);
	}

	@GetMapping("date")
	public ResultTemplate<Date> indexDate(){
		return ResultUtils.of(DateUtils.getCurDate());
	}

}
