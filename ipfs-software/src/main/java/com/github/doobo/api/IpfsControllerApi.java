package com.github.doobo.api;

import com.github.doobo.conf.IpfsConfig;
import org.springframework.web.bind.annotation.GetMapping;

public interface IpfsControllerApi {

	@GetMapping("/ipfs/config")
	IpfsConfig getIpfsConfig();
}
