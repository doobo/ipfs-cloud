package com.github.doobo.config;

import com.github.doobo.script.IpfsObserver;
import com.github.doobo.script.IpfsObserverVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * 消息订阅与分发webSocket
 */
@Slf4j
@Component
public class IpfsObserverHandler extends IpfsObserver {

	@PostConstruct
	public void init(){
	}

	@Override
	public void handleObserver(IpfsObserverVO vo) {
		try {
			WebSocketServer.BroadCastInfo(vo.getLine());
		} catch (IOException e) {
			log.warn("SendBroadCastInfoError", e);
		}
	}

}
