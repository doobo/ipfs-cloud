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
	private String tag = "string";

	@Override
	protected void processLine(String line, int level) {
		if(CmdObservedUtils.getInstance().isObserver()){
			ObServerVO vo = new ObServerVO();
			vo.setTag(tag).setLine(line).setLevel(level);
			CmdObservedUtils.getInstance().sendCmdResult(vo);
		}
		log.debug("日志级别{}：{}",level,line);
	}
}
