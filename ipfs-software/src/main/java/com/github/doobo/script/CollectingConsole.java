package com.github.doobo.script;

import lombok.extern.slf4j.Slf4j;

/**
 * 按行收集日志
 */
@Slf4j
public class CollectingConsole extends CollectingLog {

	@Override
	protected void processLine(String line, int level) {
		log.info("日志级别{}：{}",level,line);
	}
}
