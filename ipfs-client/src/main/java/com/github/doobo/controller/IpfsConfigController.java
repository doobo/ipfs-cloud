package com.github.doobo.controller;

import com.github.doobo.conf.IpfsConfig;
import com.github.doobo.model.IpfsFileInfo;
import com.github.doobo.model.SearchVO;
import com.github.doobo.params.ResultTemplate;
import com.github.doobo.service.IpfsConfigApiService;
import com.github.doobo.service.IpfsSearchApiService;
import com.github.doobo.soft.InitUtils;
import com.github.doobo.utils.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class IpfsConfigController {

	@Resource
	IpfsConfigApiService ipfsConfigApiService;

	@Resource
	IpfsSearchApiService ipfsSearchApiService;

	/**
	 * 跳转到文件管理界面
	 */
	@GetMapping("")
	public ModelAndView IndexPage(){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/fm.html#elf_A_");
		return mv;
	}

	/**
	 * 当前的ipfs节点基础信息
	 * @return
	 */
	@GetMapping("/ipfs/nodes")
	public List<IpfsConfig> queryNodesConfig(){
		return ipfsConfigApiService.queryNodeConfigList();
	}

	/**
	 * 文件搜索
	 * @param vo
	 */
	@PostMapping("ipfs/search")
	public ResultTemplate<List<IpfsFileInfo>> search(SearchVO vo){
		return ipfsSearchApiService.search(vo);
	}

	@GetMapping("/ipfs")
	public ResultTemplate<Boolean> exitFile(String cid){
		return ResultUtils.of(InitUtils.existIpfsFile(cid));
	}
}
