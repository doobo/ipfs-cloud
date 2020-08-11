package com.github.doobo.config;

import com.github.doobo.soft.InitUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * ipfs环境初始化
 */
@Component
public class InitIpfsProject {

	/**
	 * 初始化系统环境
	 */
	@PostConstruct
	public void initIpfsEnv(){
		if(!InitUtils.initIpfsEnv()){
			return;
		}
		System.out.println(InitUtils.IPFS);
	}
}
