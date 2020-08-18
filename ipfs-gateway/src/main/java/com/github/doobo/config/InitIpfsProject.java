package com.github.doobo.config;

import com.github.doobo.conf.IpfsConfig;
import com.github.doobo.soft.InitUtils;
import com.github.doobo.utils.TerminalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * ipfs环境初始化
 */
@Slf4j
@Component
public class InitIpfsProject implements InitializingBean {

	@Resource
	private IpfsConfig ipfsConfig;

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
			TerminalUtils.execCmd(InitUtils.IPFS + " init");
			log.info("IPFS is already initialized.");
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if(ipfsConfig.isPrivateNetwork()){
			if(InitUtils.createIpfsPrivateNetwork(ipfsConfig.getBootstrap())){
				log.info("IPFS is private network.");
			}
		}
		InitUtils.startDaemon();
		log.info("IPFS守护程序启动成功....");
	}
}
