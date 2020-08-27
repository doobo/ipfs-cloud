package com.github.doobo.controller;

import com.github.doobo.api.IpfsBackupControllerApi;
import com.github.doobo.model.IpfsFileInfo;
import com.github.doobo.params.ResultTemplate;
import com.github.doobo.utils.ResultTemplateUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IpfsBackupController implements IpfsBackupControllerApi {

	@GetMapping("")
	public Boolean index(){
		return Boolean.TRUE;
	}

	@Override
	public ResultTemplate<Boolean> backUpFile(@Validated @RequestBody IpfsFileInfo info) {
		return ResultTemplateUtil.of(true);
	}
}
