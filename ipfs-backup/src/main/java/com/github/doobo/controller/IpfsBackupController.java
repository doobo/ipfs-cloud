package com.github.doobo.controller;

import com.github.doobo.api.IpfsBackupControllerApi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IpfsBackupController implements IpfsBackupControllerApi {

	@GetMapping("")
	public Boolean index(){
		return Boolean.TRUE;
	}
}
