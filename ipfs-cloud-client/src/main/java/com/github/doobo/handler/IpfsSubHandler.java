package com.github.doobo.handler;

import com.alibaba.fastjson.JSON;
import com.github.doobo.conf.IpfsConfig;
import com.github.doobo.scan.WebSocketServer;
import com.github.doobo.jms.ExchangeMsg;
import com.github.doobo.jms.RequestTypeEnum;
import com.github.doobo.params.ResultTemplate;
import com.github.doobo.script.IpfsObserver;
import com.github.doobo.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 消息订阅与分发webSocket
 */
@Slf4j
@Component
public class IpfsSubHandler extends IpfsObserver  {

	@Resource
	private IpfsConfig ipfsConfig;

	@Override
	public IpfsConfig getCurConfig() {
		return ipfsConfig;
	}

	@Override
	public void handleObserver(ExchangeMsg msg) {
		if(msg == null){
			return;
		}
		sendMsg(msg);
	}

	/**
	 * 发送消息格式化
	 */
	public void sendMsg(ExchangeMsg msg){
		ResultTemplate<ExchangeMsg> res = ResultUtils.of(msg);
		if(msg.getRequestType() != null
			&& RequestTypeEnum.P2P.getType().equalsIgnoreCase(msg.getRequestType())){
			WebSocketServer.sendMessage(JSON.toJSONString(res), msg.getRequestId());
			return;
		}
		WebSocketServer.broadCastInfo(JSON.toJSONString(res));
	}
}
