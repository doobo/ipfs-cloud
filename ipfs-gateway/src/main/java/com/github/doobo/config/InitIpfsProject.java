package com.github.doobo.config;

import com.github.doobo.soft.InitUtils;
import com.github.doobo.utils.TerminalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * ipfs环境初始化
 */
@Slf4j
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
		//初始化Ipfs环境
		if(!InitUtils.isIpfsInit()){
			String str = TerminalUtils.execCmd(InitUtils.IPFS + " init");
			log.info("IPFs is already initialized.");
		}
	}
}
