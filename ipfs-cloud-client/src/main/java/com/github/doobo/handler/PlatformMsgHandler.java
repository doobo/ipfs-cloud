package com.github.doobo.handler;

import com.github.doobo.abs.AbstractPlatformObserver;
import com.github.doobo.bo.PlatformObserverRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Observable;

/**
 * 消息处理器
 *
 * @Description: ipfs-cloud
 * @User: doobo
 * @Time: 2022-09-09 22:18
 */
@Slf4j
@Component
public class PlatformMsgHandler extends AbstractPlatformObserver {

	@Override
	public boolean matching(PlatformObserverRequest request) {
		return Objects.equals("topic", request.getType());
	}

	@Override
	public void executor(PlatformObserverRequest request, Observable o) {
		log.info("msg server:{}", request.getMsg());
	}
}
