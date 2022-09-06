package com.github.doobo.script;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.LogOutputStream;

import java.io.IOException;

/**
 * 收集byte流
 */
@Slf4j
public class CollectingByte extends LogOutputStream {

	/**
	 * 消息体标识
	 */
	private String tag = "byte";

	public CollectingByte() {
	}

	public CollectingByte(String tag) {
		this.tag = tag;
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		super.write(b, off, len);
	}

	@Override
	protected void processLine(String line, int logLevel) {
	}
}
