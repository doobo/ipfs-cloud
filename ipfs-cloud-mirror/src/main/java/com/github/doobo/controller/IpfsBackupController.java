package com.github.doobo.controller;

import com.github.doobo.utils.ResultUtils;
import com.github.doobo.vbo.ResultTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author doobo
 */
@Slf4j
@RestController
public class IpfsBackupController {

	/**
	 * 跳转到文件管理界面
	 */
	@GetMapping("")
	public ModelAndView IndexPage(){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/fm.html#elf_A_");
		return mv;
	}
}
