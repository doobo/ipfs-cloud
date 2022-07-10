package com.github.doobo.start;

import com.github.doobo.model.IpfsConfig;
import com.github.doobo.soft.UnionUtils;
import com.github.doobo.utils.TerminalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static com.github.doobo.soft.UnionUtils.IPFS_EXTEND;

/**
 * ipfs环境初始化
 */
@Slf4j
public abstract class AbstractIpfsInitClient implements SmartLifecycle {

	private volatile boolean isRunning = false;

	@Resource
	private IpfsConfig ipfsConfig;

	@Override
	public void start()  {
		isRunning = true;
		//初始化Ipfs环境
		if(!UnionUtils.initIpfsEnv(ipfsConfig.getPath())){
			return;
		}
		if(!UnionUtils.isIpfsInit()){
			TerminalUtils.syncExecute(IPFS_EXTEND + " init", null, TimeUnit.MINUTES.toMillis(1));
			log.info("IPFS is already initialized.");
		}
		if(!ipfsConfig.isStartDaemon()){
			return;
		}
		//检测端口是否被占用,占用后递增端口号
		UnionUtils.configAvailablePort(ipfsConfig);
		//修改默认端口号
		UnionUtils.updateConfig(ipfsConfig);
		//是否是私有网络
		if(ipfsConfig.isPrivateNetwork()){
			if(UnionUtils.createIpfsPrivateNetwork(ipfsConfig.getBootstrap(), ipfsConfig.getSwarmKey())){
				log.info("IPFS is private network.");
			}
		}else{
			UnionUtils.delSwarmKey();
		}
		//添加其它网关节点
		//List<IpfsConfig> nodeConfigList = ipfsConfigApiService.queryNodeConfigList();
		//UnionUtils.updateBootstrap(nodeConfigList);
		UnionUtils.startDaemon();
		log.info("IPFS守护程序启动成功....");
	}

	@Override
	public int getPhase() {
		return 0;
	}

	@Override
	public void stop() {
		isRunning = false;
		UnionUtils.closePool();
		log.info("IpfsInitClient Stop");
	}

	@Override
	public boolean isRunning() {
		return isRunning;
	}
}

