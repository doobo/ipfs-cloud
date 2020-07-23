package com.github.doobo.config;

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

	}

	/**
	 * ipfs命令
	 */
	public static String IPFS;

	/**
	 * 初始化windows环境
	 * @return
	 */
	public boolean initWin64Ipfs(){
		return false;
	}

	/**
	 * 初始化macOs环境
	 * @return
	 */
	public boolean initMac64Ipfs(){
		return false;
	}

	/**
	 * 初始化Linux环境
	 */
	public boolean initLinux64Ipfs(){
		return false;
	}
}
