package com.github.doobo.api;

import com.github.doobo.model.IpfsFileInfo;
import com.github.doobo.params.ResultTemplate;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * ipfs文件备份相关API
 */
public interface IpfsBackupControllerApi {

	@PostMapping("/ipfs/backup")
	ResultTemplate<Boolean> backUpFile(IpfsFileInfo info);
}
