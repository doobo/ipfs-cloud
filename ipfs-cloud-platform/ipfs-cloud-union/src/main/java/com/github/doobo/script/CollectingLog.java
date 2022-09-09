package com.github.doobo.script;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.LogOutputStream;

/**
 * 按行收集日志
 */
@Slf4j
public class CollectingLog extends LogOutputStream {

	/**
	 * 消息体标识,topic
	 */
	private String tag = "line";

	public CollectingLog() {
	}

	public CollectingLog(String tag) {
		this.tag = tag;
	}

	@Override
	protected void processLine(String line, int level) {
		log.debug("script {}: {}", tag, line);
	}
}
