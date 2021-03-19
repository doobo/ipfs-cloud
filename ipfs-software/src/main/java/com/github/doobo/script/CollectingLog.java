package com.github.doobo.script;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.LogOutputStream;

/**
 * 按行收集日志
 */
@Slf4j
public class CollectingLog extends LogOutputStream {

	public CollectingLog() {
	}

	public CollectingLog(String tag) {
		this.tag = tag;
	}

	/**
	 * 消息体标识
	 */
	private String tag = "line";

	@Override
	protected void processLine(String line, int level) {
		if(IpfsObservedUtils.isObserver()){
			IpfsObserverVO vo = new IpfsObserverVO();
			vo.setTag(tag).setLine(line).setLevel(level);
			IpfsObservedUtils.getInstance().sendCmdResult(vo);
		}
		log.info("日志级别{}：{}",level,line);
	}
}
