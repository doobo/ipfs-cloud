package com.github.doobo.scan;

import cn.yours.elfinder.ElFinderConstants;
import cn.yours.elfinder.configuration.CmdObserver;
import cn.yours.elfinder.param.ObServerVO;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;



@Slf4j
@Component
public class FileUploadConsumer extends CmdObserver {

	@Override
	public void handleObserver(ObServerVO vo) {
		if(!"upload".equalsIgnoreCase(vo.getCmd())){
			return;
		}
		JSONObject jsonObject = vo.getResult();
		JSONArray array = jsonObject.optJSONArray(ElFinderConstants.ELFINDER_JSON_RESPONSE_ADDED);
		//分片文件上传完成前,不进行ipfs相关处理
		if(array == null || array.length() == 0){
			return;
		}
		//目标目录
		/*String target = ContextHolderUtil.getRequest().getParameter(ElFinderConstants.ELFINDER_PARAMETER_TARGET);
		if(StringUtils.isNotBlank(target)){
			handler = vo.getVolumeHandlerByHash(target);
			target = handler.getTarget().toString();
		}
		for(int i = 0 ;i < array.length(); i++){
			obj = array.getJSONObject(i);
			hash = obj.optString(ElFinderConstants.ELFINDER_PARAMETER_HASH,null);
			pHash = obj.optString(ElFinderConstants.ELFINDER_PARAMETER_PARENTHASH,null);
			if(hash == null) continue;
			handler = vo.getVolumeHandlerByHash(hash);
			name = handler.getTarget().toString();
			if(pHash != null && pHash.length() > 2){
				PathVO po = new PathVO();
				po.setHash(pHash);
				po.setTarget(target);
				po.setPath(vo.getVolumeHandlerByHash(pHash).getTarget().toString());
				path = noRepeatPath(po, path);
			}
			obj.put(StringParams.IPFS.str(), ipfsUpload(name));
			if(ipfsBackHandler.backUpFile(obj).getOk()){
				log.info("文件备份成功,{},{}", name, obj.getString(StringParams.IPFS.str()));
			}
			if(ipfsSearchHandler.saveFileInfo(obj).getOk()){
				log.info("添加到搜索服务,{},{}", name, obj.getString(StringParams.IPFS.str()));
			}
		}
		//目录形式的ipfs返回
		jsonObject.put("path", uploadIpfsPath(path));*/
	}
}
