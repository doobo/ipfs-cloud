package com.github.doobo.config;

import com.github.doobo.conf.IpfsConfig;
import com.github.doobo.service.IpfsConfigApiService;
import com.github.doobo.soft.InitUtils;
import com.github.doobo.utils.TerminalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.github.doobo.soft.InitUtils.IPFS_EXTEND;

/**
 * ipfs环境初始化
 */
@Slf4j
@Component
public class IpfsInitBackup implements CommandLineRunner {

	@Resource
	IpfsConfigApiService ipfsConfigApiService;

	@Resource
	private IpfsConfig ipfsConfig;

	@Override
	public void run(String... args) throws Exception {
		if(!InitUtils.initIpfsEnv()){
			return;
		}
		//初始化Ipfs环境
		if(!InitUtils.isIpfsInit()){
			TerminalUtils.syncExecute(IPFS_EXTEND + " init", null, 60000);
			log.info("IPFS is already initialized.");
		}
		if(!ipfsConfig.isStartDaemon()){
			return;
		}
		//修改默认端口号
		InitUtils.updateConfig(ipfsConfig);
		//是否是私有网络
		if(ipfsConfig.isPrivateNetwork()){
			if(InitUtils.createIpfsPrivateNetwork(ipfsConfig.getBootstrap(), ipfsConfig.getSwarmKey())){
				log.info("IPFS is private network.");
			}
		}else{
			InitUtils.delSwarmKey();
		}
		//添加其它网关节点
		List<IpfsConfig> nodeConfigList = ipfsConfigApiService.queryNodeConfigList();
		InitUtils.updateBootstrap(nodeConfigList);
		InitUtils.startDaemon();
		log.info("IPFS守护程序启动成功....");
	}
}
