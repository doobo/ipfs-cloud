package com.github.doobo.config;

import cn.yours.elfinder.ElFinderConstants;
import cn.yours.elfinder.configuration.CmdObserver;
import cn.yours.elfinder.param.ObServerVO;
import cn.yours.elfinder.service.VolumeHandler;
import com.github.doobo.conf.IpfsConfig;
import com.github.doobo.handler.IpfsBackHandler;
import com.github.doobo.handler.IpfsSearchHandler;
import com.github.doobo.model.PathVO;
import com.github.doobo.params.StringParams;
import com.github.doobo.soft.ContextHolderUtil;
import com.github.doobo.utils.FileUtils;
import com.github.doobo.utils.OsUtils;
import com.github.doobo.utils.TerminalUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.doobo.soft.InitUtils.*;

@Slf4j
@Component
public class FileUploadConsumer extends CmdObserver {

	@Resource
	IpfsBackHandler ipfsBackHandler;

	@Resource
	IpfsSearchHandler ipfsSearchHandler;

	@Resource
	IpfsConfig ipfsConfig;

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
		String hash,pHash,name;
		Set<PathVO> path = null;
		VolumeHandler handler;
		//目标目录
		String target = ContextHolderUtil.getRequest().getParameter(ElFinderConstants.ELFINDER_PARAMETER_TARGET);
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
		jsonObject.put("path", uploadIpfsPath(path));
	}

	/**
	 *  目录去重,取外层目录
	 */
	private Set<PathVO> noRepeatPath(PathVO path,  Set<PathVO> st){
		st = st == null ? new HashSet<>(1): st;
		if(StringUtils.isBlank(path.getPath())){
			return st;
		}
		//非目录形式的文件上传,不解析目录
		if(path.getPath() != null &&  path.getPath().equals(path.getTarget())){
			return st;
		}
		if(st.isEmpty()){
			st.add(path);
			return st;
		}
		String curPath = path.getPath(), iph;
		if(curPath == null){
			return st;
		}
		for(PathVO item :  st){
			iph = item.getPath();
			if(iph.length() > curPath.length() && iph.startsWith(curPath)){
				BeanUtils.copyProperties(path,  item);
				return st;
			}else if(iph.length() <= curPath.length() && curPath.startsWith(iph)){
				return  st;
			}
		}
		st.add(path);
		return st;
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
		String result;
		if(OsUtils.getSystemType() == StringParams.Windows){
			result = TerminalUtils.syncExecuteStr(IPFS_EXTEND, "add", String.format("\"%s\"", name));
		}else{
			result = TerminalUtils.syncMainExecute(IPFS, IPFS_CONF_ARRAY[0], IPFS_CONF_ARRAY[1], "add", name);
		}
		name = name.replace(StringParams.SLASH_DOUBLE.str(),StringParams.BACKSLASH.str())
			.replace(StringParams.SLASH.str(),StringParams.BACKSLASH.str());
		String fileName = name.substring(name.lastIndexOf(StringParams.BACKSLASH.str())+1);
		result = ipfsCid(result);
		if(result != null && ipfsConfig.isWriteFile()){
			FileUtils.writFile(result, name.replace(fileName, fileName + StringParams.IPFS_SUFFIX.str()));
		}
		return result;
	}

	/**
	 * 目录上传ipfs
	 * @param ps
	 */
	public List<PathVO> uploadIpfsPath(Set<PathVO> ps){
		if(ps == null || ps.isEmpty()){
			return null;
		}
		String result,cid;
		File file;
		List<PathVO> rs = new ArrayList<>();
		for(PathVO item : ps){
			file = new File(item.getPath());
			if(file.exists() && file.isDirectory()){
				item.setName(file.getName());
				rs.add(item);
				if(OsUtils.getSystemType() == StringParams.Windows){
					result = TerminalUtils.syncExecuteStr(IPFS_EXTEND, "add", "-r", String.format("\"%s\"", item.getPath()));
				}else{
					result = TerminalUtils.syncMainExecute(IPFS, IPFS_CONF_ARRAY[0], IPFS_CONF_ARRAY[1], "add", "-r", item.getPath());
				}
				cid = ipfsPathCid(result, item.getName());
				if(cid != null && !cid.isEmpty()){
					item.setIpfs(cid);
					item.setRemarks(Collections.singletonList(result));
					item.setTs(Calendar.getInstance().getTimeInMillis());
					if(ipfsSearchHandler.saveFileInfo(new JSONObject(item)).getOk()){
						log.info("添加到搜索服务,{},{}", item.getPath(), item.getIpfs());
					}
					if(ipfsConfig.isWriteFile()) {
						FileUtils.writFile(cid, item.getPath().replace(item.getName(), item.getName() + StringParams.IPFS_SUFFIX.str()));
					}
				}
			}
		}
		return rs;
	}

	/**
	 * 截取cid
	 * @param rs
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
