package com.github.doobo.controller;

import com.github.doobo.api.IpfsSearchControllerApi;
import com.github.doobo.model.IpfsFileInfo;
import com.github.doobo.model.SearchVO;
import com.github.doobo.params.DirectReturnException;
import com.github.doobo.params.ResultTemplate;
import com.github.doobo.service.IpfsSearchService;
import com.github.doobo.soft.InitUtils;
import com.github.doobo.utils.CommonUtils;
import com.github.doobo.utils.DateUtils;
import com.github.doobo.utils.ResultUtils;
import com.github.doobo.utils.TerminalUtils;
import lombok.extern.slf4j.Slf4j;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Slf4j
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

	/**
	 * 保存文件信息
	 * @param fileInfo
	 */
	@Override
	public ResultTemplate<Boolean> saveFileInfo(@RequestBody IpfsFileInfo fileInfo) {
		return ipfsSearchService.saveFileInfo(fileInfo);
	}

	/**
	 * 搜索文件
	 * @param vo
	 */
	@Override
	public ResultTemplate<List<IpfsFileInfo>> search(@RequestBody SearchVO vo) {
		return ipfsSearchService.search(vo);
	}

	/**
	 * 通过IPFS下载文件,适合小文件
	 * @param cid
	 * @param response
	 * @throws IOException
	 */
	@GetMapping("/cat/{cid}")
	public void get(@PathVariable("cid") String cid, HttpServletResponse response) throws IOException {
		if(!InitUtils.existIpfsFile(cid)){
			throw new DirectReturnException(404, String.format("file:%s not found!", cid));
		}
		SearchVO info = new SearchVO().setCid(cid);
		ResultTemplate<List<IpfsFileInfo>> result = ipfsSearchService.search(info);
		if(result.getOk() && CommonUtils.hasValue(result.getData())){
			IpfsFileInfo ipfsFileInfo = result.getData().stream().findFirst().orElse(null);
			if(ipfsFileInfo != null) {
				if(ipfsFileInfo.getName() != null) {
					response.setHeader("Content-disposition", "attachment; filename="
						+ new String(ipfsFileInfo.getName().getBytes("gb2312"), "iso8859-1"));
				}
				if(ipfsFileInfo.getMime() != null) {
					response.setContentType(ipfsFileInfo.getMime());
				}
			}
		}
		try(ServletOutputStream outputStream = response.getOutputStream()){
			outputStream.write(TerminalUtils.syncExecute(InitUtils.IPFS, "cat", cid));
		}
	}

	@GetMapping("date")
	public ResultTemplate<Date> indexDate(){
		return ResultUtils.of(DateUtils.getCurDate());
	}

}
