package com.github.doobo.handler;

import com.alibaba.fastjson.JSON;
import com.github.doobo.conf.Node;
import com.github.doobo.model.IpfsFileInfo;
import com.github.doobo.params.CustomException;
import com.github.doobo.params.ResultTemplate;
import com.github.doobo.service.IpfsSearchApiService;
import com.github.doobo.soft.InitUtils;
import com.github.doobo.utils.ResultUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class IpfsSearchHandler {

	@Resource
	IpfsSearchApiService ipfsSearchApiService;

	/**
	 * 调用备份服务
	 * 根据需要,可以改成广播，失败重发等
	 * @param obj
	 */
	public ResultTemplate<Boolean> saveFileInfo(JSONObject obj){
		IpfsFileInfo info = JSON.parseObject(obj.toString(), IpfsFileInfo.class);
		if(info == null){
			return ResultUtils.ofThrowable(new CustomException("保存的文件信息为空"));
		}
		Node node = InitUtils.getIpfsNodeInfo();
		if(node != null) {
			info.setNodeId(node.getCid());
			info.setNodes(node.getIpfs());
		}
		return ipfsSearchApiService.saveFileInfo(info);
	}
}
