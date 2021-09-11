package com.github.doobo.controller;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.github.doobo.conf.IpfsConfig;
import com.github.doobo.jms.ExchangeMsg;
import com.github.doobo.params.ResultTemplate;
import com.github.doobo.service.IpfsConfigService;
import com.github.doobo.soft.InitUtils;
import com.github.doobo.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
public class IpfsController {

	@Resource
	private IpfsConfigService ipfsConfigService;

	@Resource
	private IpfsConfig ipfsConfig;

	@GetMapping("ipfs")
	public ResultTemplate<Boolean> exitFile(String cid){
		return ResultUtils.of(InitUtils.existIpfsFile(cid));
	}

	@GetMapping("ipfs/config")
	public IpfsConfig getIpfsConfig() {
		return ipfsConfigService.queryIpfsConfig();
	}

	@GetMapping("ipfs/nodes")
	public List<IpfsConfig> queryNodeConfigList() {
		return ipfsConfigService.queryNodeConfigList();
	}

	/**
	 * 发送广播消息
	 */
	@PostMapping("ipfs/pubMsg")
	public ResultTemplate<Boolean> pubMsg(@RequestBody ExchangeMsg vo){
		return ipfsConfigService.pubMsg(vo);
	}

	/**
	 *  启动消息订阅
	 */
	@GetMapping("ipfs/startSub")
	public ResultTemplate<Boolean> startSub() throws InterruptedException {
		return ResultUtils.of(InitUtils.initSub(ipfsConfig));
	}

	/**
	 *  动态设置日志级别
	 * @param rootLevel root,全局级别:ALL,TRACE,DEBUG,INFO,WARN,ERROR,OFF
	 * @param singleLevel 单独设置类日志级别:ALL,TRACE,DEBUG,INFO,WARN,ERROR,OFF
	 * @param singlePath 单独类路径:com.chinasofti.cloudeasy.api.web.LogController
	 */
	@GetMapping("ipfs/changeLevel")
	public ResultTemplate<Boolean> changeLevel(String rootLevel, String singleLevel, String singlePath){
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		if (!StringUtils.isEmpty(rootLevel)) {
			// 设置全局日志级别
			ch.qos.logback.classic.Logger logger = loggerContext.getLogger("root");
			logger.setLevel(Level.toLevel(rootLevel));
		}
		if (!StringUtils.isEmpty(singleLevel)) {
			// 设置某个类日志级别-可以实现定向日志级别调整
			ch.qos.logback.classic.Logger vLogger = loggerContext.getLogger(singlePath);
			if (vLogger != null) {
				vLogger.setLevel(Level.toLevel(singleLevel));
			}
		}
		log.warn("set log rootLevel:{},singleLevel:{},singlePath:{}", rootLevel, singleLevel, singlePath);
		return ResultUtils.of(Boolean.TRUE);
	}
}


