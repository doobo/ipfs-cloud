package com.github.doobo.controller;

import com.alibaba.fastjson.JSON;
import com.github.doobo.api.IpfsBackupControllerApi;
import com.github.doobo.model.IpfsFileInfo;
import com.github.doobo.params.ResultTemplate;
import com.github.doobo.utils.ResultTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class IpfsBackupController implements IpfsBackupControllerApi {

	@GetMapping("")
	public Boolean index(){
		return Boolean.TRUE;
	}

	@Override
	public ResultTemplate<Boolean> backUpFile(@Validated @RequestBody IpfsFileInfo info) {
		log.info(JSON.toJSONString(info));
		return ResultTemplateUtil.of(true);
	}
}
