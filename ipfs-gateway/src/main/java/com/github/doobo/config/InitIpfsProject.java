package com.github.doobo.config;

import com.github.doobo.utils.OsUtils;
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
		switch (OsUtils.getSystemType()){
			case MAC_OS:
				initMac64Ipfs();
				break;
			case Linux:
				initLinux64Ipfs();
				break;
			case Windows:
				initWin64Ipfs();
		}
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
