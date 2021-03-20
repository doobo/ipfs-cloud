package com.github.doobo.controller;

import com.github.doobo.api.IpfsControllerApi;
import com.github.doobo.conf.IpfsConfig;
import com.github.doobo.model.IpfsPubVO;
import com.github.doobo.params.ResultTemplate;
import com.github.doobo.service.IpfsConfigService;
import com.github.doobo.soft.InitUtils;
import com.github.doobo.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author doobo
 */
@Slf4j
@RestController
public class IpfsController implements IpfsControllerApi {

	@Resource
	private IpfsConfigService ipfsConfigService;

	@Override
	public ResultTemplate<Boolean> exitFile(String cid){
		return ResultUtils.of(InitUtils.existIpfsFile(cid));
	}

	@Override
	public IpfsConfig getIpfsConfig() {
		return ipfsConfigService.queryIpfsConfig();
	}

	@Override
	public List<IpfsConfig> queryNodeConfigList() {
		return ipfsConfigService.queryNodeConfigList();
	}

	/**
	 * 发送广播消息
	 */
	@Override
	public ResultTemplate<Boolean> pubMsg(@RequestBody IpfsPubVO vo){
		return ipfsConfigService.pubMsg(vo);
	}
}


