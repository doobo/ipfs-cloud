package com.github.doobo.config;

import cn.yours.elfinder.ElFinderConstants;
import cn.yours.elfinder.obs.AbstractCmdObserver;
import cn.yours.elfinder.obs.ElfinderConfigurationUtils;
import cn.yours.elfinder.obs.ObServerRequest;
import cn.yours.elfinder.service.VolumeHandler;
import com.github.doobo.bo.PlatformInitRequest;
import com.github.doobo.handler.AbstractPlatformInitHandler;
import com.github.doobo.utils.FileUtils;
import com.github.doobo.vbo.PathRequest;
import com.github.doobo.vbo.ResultTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Observable;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
		Set<PathRequest> path = new LinkedHashSet<>();
		String target = SpringUtil.getRequest().getParameter(ElFinderConstants.ELFINDER_PARAMETER_TARGET);
		if(StringUtils.isNotBlank(target)){
			handler = request.getVolumeHandlerByHash(target);
			target = handler.getTarget().toString();
		}
		JSONObject obj;
		String hash, pHash, name;
		for(int i = 0 ;i < array.length(); i++){
			obj = array.getJSONObject(i);
			hash = obj.optString(ElFinderConstants.ELFINDER_PARAMETER_HASH,null);
			pHash = obj.optString(ElFinderConstants.ELFINDER_PARAMETER_PARENTHASH,null);
			if(hash == null) continue;
			handler = request.getVolumeHandlerByHash(hash);
			name = handler.getTarget().toString();
			if(pHash != null && pHash.length() > 2){
				PathRequest po = new PathRequest();
				po.setHash(pHash);
				po.setTarget(target);
				po.setPath(request.getVolumeHandlerByHash(pHash).getTarget().toString());
				noRepeatPath(po, path);
			}
			obj.put("ipfs", ipfsUpload(name));
		}
		//目录形式的ipfs返回
		jsonObject.put("path", uploadIpfsPath(path));
	}

	/**
	 *  目录去重,取外层目录
	 */
	private void noRepeatPath(PathRequest path,  Set<PathRequest> st){
		if(StringUtils.isBlank(path.getPath()) || Objects.isNull(st)){
			return;
		}
		//非目录形式的文件上传,不解析目录
		if(path.getPath() != null &&  path.getPath().equals(path.getTarget())){
			return;
		}
		if(st.isEmpty()){
			st.add(path);
			return;
		}
		String curPath = path.getPath(), iph;
		if(curPath == null){
			return;
		}
		for(PathRequest item :  st){
			iph = item.getPath();
			if(iph.length() > curPath.length() && iph.startsWith(curPath)){
				BeanUtils.copyProperties(path,  item);
				return;
			}else if(iph.length() <= curPath.length() && curPath.startsWith(iph)){
				return;
			}
		}
		st.add(path);
	}

	/**
	 * ipfs文件分发
	 */
	public String ipfsUpload(String name){
		File file = new File(name);
		if(!file.exists()){
			return null;
		}
		String result = null;
		String filePath = ElfinderConfigurationUtils.treatPath(name);
		if(!filePath.isEmpty()){
			filePath = filePath.replace("file:", "");
		}
		PlatformInitRequest request = new PlatformInitRequest();
		request.addExtParam("add").addExtParam(filePath);
		ResultTemplate<String> template = AbstractPlatformInitHandler.execIpfsShell(request);
		if(Objects.nonNull(template) && template.isSuccess()) {
			result = template.getData();
			result = ipfsCid(result);
		}
		//写入IPFS默认后缀
		Optional.ofNullable(result).ifPresent(m ->{
			Optional.ofNullable(IpfsInitConfig.getIpfsProperties().getWriteFile())
				.filter(f -> f).ifPresent(c ->{
					FileUtils.writFile(m, String.format("%s.ipfs", name));
			});
		});
		return result;
	}

	/**
	 * 目录上传ipfs
	 */
	public List<PathRequest> uploadIpfsPath(Set<PathRequest> ps){
		if(ps == null || ps.isEmpty()){
			return null;
		}
		String result = null, cid;
		File file;
		List<PathRequest> rs = new ArrayList<>();
		for(PathRequest item : ps){
			file = new File(item.getPath());
			if(file.exists() && file.isDirectory()){
				item.setName(file.getName());
				rs.add(item);
				PlatformInitRequest request = new PlatformInitRequest();
				request.addExtParam("add").addExtParam("-r").addExtParam(item.getPath());
				ResultTemplate<String> template = AbstractPlatformInitHandler.execIpfsShell(request);
				if(Objects.nonNull(template) && template.isSuccess()) {
					result = template.getData();
				}
				cid = ipfsPathCid(result, item.getName());
				if(cid != null && !cid.isEmpty()){
					item.setIpfs(cid);
					if(StringUtils.isNotBlank(result)){
						List<String> collect = Arrays.stream(result.split("\n"))
							.filter(StringUtils::isNotBlank)
							.map(s -> s.replaceAll("\\s+", " ").trim())
							.collect(Collectors.toList());
						item.setRemarks(collect);
					}
					item.setTs(Calendar.getInstance().getTimeInMillis());
				}
				log.info("uploadFile:{}", item);
			}
		}
		return rs;
	}

	/**
	 * 截取cid
	 */
	public String ipfsCid(String rs){
		if(rs == null || rs.isEmpty()){
			return null;
		}
		Pattern pattern = Pattern.compile("( )(Qm.*?)( )", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(rs);
		if(matcher.find()){
			return matcher.group(2);
		}
		return null;
	}

	/**
	 * 截取目录的cid
	 */
	public String ipfsPathCid(String rs, String name){
		if(rs == null || rs.isEmpty()){
			return null;
		}
		Pattern pattern = Pattern.compile("( )(Qm.*?)( "+name+")", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(rs);
		String cid = null;
		while(matcher.find()){
			cid = matcher.group(2);
		}
		return cid;
	}
}
