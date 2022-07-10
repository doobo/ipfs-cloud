package com.github.doobo.service;

import com.github.doobo.model.IpfsFileInfo;

import java.io.IOException;

/**
 * 文件备份服务
 */
public interface IpfsBackupService {

	/**
	 * 备份文件服务
	 * @param info
	 */
	void backUpFile(IpfsFileInfo info) throws IOException;
}
