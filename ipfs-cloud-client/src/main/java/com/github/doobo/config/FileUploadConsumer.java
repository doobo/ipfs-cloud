package com.github.doobo.config;

import cn.yours.elfinder.ElFinderConstants;
import cn.yours.elfinder.obs.AbstractCmdObserver;
import cn.yours.elfinder.obs.ObServerRequest;
import cn.yours.elfinder.service.VolumeHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Observable;

@Slf4j
@Component
public class FileUploadConsumer extends AbstractCmdObserver {

	@Override
	public boolean matching(ObServerRequest request) {
		return "upload".equalsIgnoreCase(request.getCmd());
	}

	@Override
	public void executor(ObServerRequest request, Observable o) {
		JSONObject jsonObject = request.getResult();
		JSONArray array = jsonObject.optJSONArray(ElFinderConstants.ELFINDER_JSON_RESPONSE_ADDED);
		//分片文件上传完成前,不进行ipfs相关处理
		if(array == null || array.length() == 0){
			return;
		}
		//目标目录
		VolumeHandler handler;
		String target = SpringUtil.getRequest().getParameter(ElFinderConstants.ELFINDER_PARAMETER_TARGET);
		if(StringUtils.isNotBlank(target)){
			handler = request.getVolumeHandlerByHash(target);
			target = handler.getTarget().toString();
		}
		JSONObject obj;
		String hash,pHash,name;
		for(int i = 0 ;i < array.length(); i++){
			obj = array.getJSONObject(i);
			hash = obj.optString(ElFinderConstants.ELFINDER_PARAMETER_HASH,null);
			pHash = obj.optString(ElFinderConstants.ELFINDER_PARAMETER_PARENTHASH,null);
			if(hash == null) continue;
			handler = request.getVolumeHandlerByHash(hash);
			name = handler.getTarget().toString();
		}
		//目录形式的ipfs返回
		//jsonObject.put("path", uploadIpfsPath(path));
	}
}
