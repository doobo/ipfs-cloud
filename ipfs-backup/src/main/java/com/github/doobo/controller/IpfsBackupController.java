package com.github.doobo.controller;

import com.github.doobo.api.IpfsBackupControllerApi;
import com.github.doobo.model.IpfsFileInfo;
import com.github.doobo.params.ResultTemplate;
import com.github.doobo.service.IpfsBackupService;
import com.github.doobo.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * @author doobo
 */
@Slf4j
@RestController
public class IpfsBackupController implements IpfsBackupControllerApi {

	@Resource
	IpfsBackupService ipfsBackupService;

	/**
	 * 跳转到文件管理界面
	 */
	@GetMapping("")
	public ModelAndView IndexPage(){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/fm.html#elf_A_");
		return mv;
	}

	@Override
	public ResultTemplate<Boolean> backUpFile(@Validated @RequestBody IpfsFileInfo info) {
		ipfsBackupService.backUpFile(info);
		return ResultUtils.of(Boolean.TRUE);
	}
}
