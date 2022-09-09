package com.github.doobo.script;

import com.github.doobo.bo.PlatformObserverRequest;
import com.github.doobo.factory.PlatformObservedFactory;
import com.github.doobo.utils.SequenceUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.LogOutputStream;

import java.util.Optional;

/**
 * 执行日志事件触发器
 */
@Slf4j
public class CollectingObserved extends LogOutputStream {

	private PlatformObserverRequest request;

	public CollectingObserved() {
	}

	public CollectingObserved(PlatformObserverRequest request) {
		this.request = request;
	}

	@Override
	protected void processLine(String line, int level) {
		log.debug("script: {}", line);
		Optional.ofNullable(request).ifPresent(c ->{
			request.setMsg(line);
			request.setLevel(level);
			request.setId(SequenceUtils.nextId());
			Optional.ofNullable(PlatformObservedFactory.getPlatformTopicObserved())
				.ifPresent(p -> p.notifyChange(c));
		});
	}
}
