package com.github.doobo.config;

import com.github.doobo.conf.IpfsConfig;
import com.github.doobo.soft.InitUtils;
import com.github.doobo.utils.TerminalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * ipfs环境初始化
 */
@Slf4j
@Component
public class InitIpfsProject implements CommandLineRunner {

	@Resource
	private IpfsConfig ipfsConfig;

	/**
	 * 初始化系统环境
	 */
	@Override
	public void run(String... args) throws Exception {
		if(!InitUtils.initIpfsEnv()){
			return;
		}
		//初始化Ipfs环境
		if(!InitUtils.isIpfsInit()){
			TerminalUtils.syncExecute(InitUtils.IPFS + " init", null, 60000);
			log.info("IPFS is already initialized.");
		}
		//修改默认端口号
		InitUtils.updateConfig(ipfsConfig);
		//是否是私有网络
		if(ipfsConfig.isPrivateNetwork()){
			if(InitUtils.createIpfsPrivateNetwork(ipfsConfig.getBootstrap())){
				log.info("IPFS is private network.");
			}
		}
		if(ipfsConfig.isStartDaemon()){
			InitUtils.startDaemon();
			log.info("IPFS守护程序启动成功....");
		}
	}
}
