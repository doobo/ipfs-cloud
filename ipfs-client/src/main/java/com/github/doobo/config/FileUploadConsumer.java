package com.github.doobo.config;

import cn.yours.elfinder.ElFinderConstants;
import cn.yours.elfinder.param.ObServerVO;
import cn.yours.elfinder.service.VolumeHandler;
import cn.yours.web.consume.CmdObserver;
import com.github.doobo.params.StringParams;
import com.github.doobo.utils.FileUtils;
import com.github.doobo.utils.OsUtils;
import com.github.doobo.utils.TerminalUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.doobo.soft.InitUtils.IPFS;

@Slf4j
public class FileUploadConsumer extends CmdObserver {

	@Override
	public void handleObserver(ObServerVO vo) {
		if(!StringParams.UPLOAD.str().equalsIgnoreCase(vo.getCmd())){
			return;
		}
		JSONObject jsonObject = vo.getResult();
		JSONArray array = jsonObject.optJSONArray(ElFinderConstants.ELFINDER_JSON_RESPONSE_ADDED);
		//分片文件上传完成前,不进行ipfs相关处理
		if(array == null || array.length() == 0){
			return;
		}
		JSONObject obj;
		String hash;
		for(int i = 0 ;i < array.length(); i++){
			obj = array.getJSONObject(i);
			hash = obj.optString(ElFinderConstants.ELFINDER_PARAMETER_HASH,null);
			if(hash == null) continue;
			VolumeHandler handler = vo.getVolumeHandlerByHash(hash);
			String name = handler.getTarget().toString();
			obj.put("ipfs", ipfsUpload(name));
		}
	}

	/**
	 * ipfs文件分发
	 * @param name
	 */
	public String ipfsUpload(String name){
		File file = new File(name);
		if(!file.exists()){
			return null;
		}
		String result = null;
		if(OsUtils.getSystemType() == StringParams.Windows){
			result = TerminalUtils.syncExecuteStr(IPFS, "add", String.format("\"%s\"", name));
		}else{
			result = TerminalUtils.syncMainExecute(IPFS, "add", name);
		}
		name = name.replace(StringParams.SLASH_DOUBLE.str(),StringParams.BACKSLASH.str())
			.replace(StringParams.SLASH.str(),StringParams.BACKSLASH.str());
		String fileName = name.substring(name.lastIndexOf(StringParams.BACKSLASH.str())+1);
		result = ipfsCid(result, fileName);
		if(result != null){
			FileUtils.writFile(result, name.replace(fileName, fileName + StringParams.IPFS_SUFFIX.str()));
		}
		return result;
	}

	/**
	 * 截取cid
	 * @param rs
	 */
	public String ipfsCid(String rs, String fileName){
		if(rs == null || rs.isEmpty()){
			return null;
		}
		Pattern pattern = Pattern.compile("( )(Qm.*?)( "+fileName+")", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(rs);
		if(matcher.find()){
			return matcher.group(2);
		}
		return null;
	}
}
