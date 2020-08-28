package com.github.doobo.handler;

import com.alibaba.fastjson.JSON;
import com.github.doobo.model.IpfsFileInfo;
import com.github.doobo.params.ResultTemplate;
import com.github.doobo.service.IpfsBackupApiService;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class IpfsBackHandler {

	@Resource
	IpfsBackupApiService ipfsBackupApiService;

	/**
	 * 调用备份服务
	 * 根据需要,可以改成广播，失败重发等
	 * @param obj
	 */
	public ResultTemplate<Boolean> backUpFile(JSONObject obj){
		IpfsFileInfo info = JSON.parseObject(obj.toString(), IpfsFileInfo.class);
		return ipfsBackupApiService.backUpFile(info);
	}
}
