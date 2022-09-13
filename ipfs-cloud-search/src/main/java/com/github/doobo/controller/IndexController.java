package com.github.doobo.controller;

import com.github.doobo.utils.ResultUtils;
import com.github.doobo.vbo.ResultTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 搜索控制器
 */
@Slf4j
@RestController
public class IndexController{

	@GetMapping
	public ResultTemplate<Boolean> index(){
		return ResultUtils.of(true);
	}
}
