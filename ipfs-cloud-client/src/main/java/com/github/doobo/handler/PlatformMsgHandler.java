package com.github.doobo.handler;

import com.github.doobo.bo.PubMessageBO;
import com.github.doobo.bo.PubMsgEncodeBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 消息处理器
 *
 * @Description: ipfs-cloud
 * @User: doobo
 * @Time: 2022-09-09 22:18
 */
@Slf4j
@Component
public class PlatformMsgHandler extends AbstractPlatformMsgHandler {


	@Override
	public void handler(PubMessageBO body, PubMsgEncodeBO msg) {
		if(Objects.isNull(msg) || Objects.isNull(body)){
			log.error("handler body or msg is null");
			return;
		}
		log.info("handler body:{}", body);
	}
}
