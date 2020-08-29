package com.github.doobo.config;

import com.github.doobo.conf.IpfsConfig;
import com.github.doobo.conf.Node;
import com.github.doobo.service.IpfsConfigApiService;
import com.github.doobo.soft.InitUtils;
import com.github.doobo.utils.CommonUtils;
import com.github.doobo.utils.OsUtils;
import com.github.doobo.utils.TerminalUtils;
import com.github.doobo.utils.WordUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

import static com.github.doobo.soft.InitUtils.IPFS;

/**
 * ipfs环境初始化
 */
@Slf4j
@Component
public class IpfsInitSearchEs implements CommandLineRunner {

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
			TerminalUtils.syncExecute(IPFS + " init", null, 60000);
			log.info("IPFS is already initialized.");
		}
		if(!ipfsConfig.isStartDaemon()){
			return;
		}
		//修改默认端口号
		InitUtils.updateConfig(ipfsConfig);
		//是否是私有网络
		if(ipfsConfig.isPrivateNetwork()){
			if(InitUtils.createIpfsPrivateNetwork(ipfsConfig.getBootstrap())){
				log.info("IPFS is private network.");
			}
		}

		//添加其它网关节点
		List<IpfsConfig> nodeConfigList = ipfsConfigApiService.queryNodeConfigList();
		if(nodeConfigList != null && !nodeConfigList.isEmpty()){
			nodeConfigList.forEach(m->{
				if(CommonUtils.hasValue(m.getNodes())){
					Node node = m.getNodes().get(0);
					node.setPort(node.getPort()==null?m.getPort().toString():node.getPort());
					if(OsUtils.checkIpPortOpen(node.getIp(), Integer.parseInt(node.getPort()))){
						String ipfs;
						if(WordUtils.isIpV4Address(node.getIp())){
							ipfs = String.format("/ip4/%s/tcp/%s/ipfs/%s", node.getIp(), node.getPort(), node.getCid());
						}else {
							ipfs = String.format("/dnsaddr/%s/tcp/%s/ipfs/%s", node.getIp(), node.getPort(), node.getCid());
						}
						TerminalUtils.syncExecute(IPFS, "bootstrap add", ipfs);
					}
				}
			});
		}
		InitUtils.startDaemon();
		log.info("IPFS守护程序启动成功....");
	}
}
