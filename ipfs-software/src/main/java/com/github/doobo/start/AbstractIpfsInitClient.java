package com.github.doobo.start;

import com.github.doobo.conf.IpfsConfig;
import com.github.doobo.service.IpfsConfigApiService;
import com.github.doobo.soft.InitUtils;
import com.github.doobo.utils.TerminalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.github.doobo.soft.InitUtils.IPFS_EXTEND;

/**
 * ipfs环境初始化
 */
@Slf4j
public abstract class AbstractIpfsInitClient implements SmartLifecycle {

	private volatile boolean isRunning = false;

	@Resource
	IpfsConfigApiService ipfsConfigApiService;

	@Resource
	private IpfsConfig ipfsConfig;

	@Override
	public void start()  {
		isRunning = true;
		//初始化Ipfs环境
		if(!InitUtils.initIpfsEnv(ipfsConfig.getPath())){
			return;
		}
		if(!InitUtils.isIpfsInit()){
			TerminalUtils.syncExecute(IPFS_EXTEND + " init", null, TimeUnit.MINUTES.toMillis(1));
			log.info("IPFS is already initialized.");
		}
		if(!ipfsConfig.isStartDaemon()){
			return;
		}
		//检测端口是否被占用,占用后递增端口号
		InitUtils.configAvailablePort(ipfsConfig);
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

	@Override
	public int getPhase() {
		return 0;
	}

	@Override
	public void stop() {
		isRunning = false;
		InitUtils.closePool();
		log.info("IpfsInitClient Stop");
	}

	@Override
	public boolean isRunning() {
		return isRunning;
	}
}

