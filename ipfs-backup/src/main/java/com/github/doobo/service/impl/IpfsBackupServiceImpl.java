package com.github.doobo.service.impl;

import cn.yours.elfinder.core.Volume;
import cn.yours.elfinder.service.ElfinderStorage;
import cn.yours.elfinder.service.VolumeHandler;
import com.alibaba.fastjson.JSON;
import com.github.doobo.model.IpfsFileInfo;
import com.github.doobo.params.ResultTemplate;
import com.github.doobo.params.StringParams;
import com.github.doobo.service.IpfsBackupService;
import com.github.doobo.utils.DateUtils;
import com.github.doobo.utils.FileUtils;
import com.github.doobo.utils.ResultTemplateUtil;
import com.github.doobo.utils.TerminalUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.io.File;
import java.util.Objects;

import static com.github.doobo.soft.InitUtils.IPFS;

/**
 * 文件备份处理服务
 */
@Service
public class IpfsBackupServiceImpl implements IpfsBackupService {

	@Resource
	ElfinderStorage elfinderStorage;

	/**
	 * 备份文件服务
	 * @param info
	 */
	@Override
	public ResultTemplate<Boolean> backUpFile(IpfsFileInfo info) {
		String backPath = DateUtils.formatDateByFormat(null, DateUtils.DateFormat.yMdH.getFt());
		if(elfinderStorage == null || elfinderStorage.getVolumes() == null
			|| elfinderStorage.getVolumes().isEmpty()){
			return ResultTemplateUtil.ofThrowable(null);
		}
		Volume volume = Objects.requireNonNull(elfinderStorage.getVolumes().get(0));
		backPath = volume.getRoot().toString() + File.separator + backPath;
		FileUtils.createDirIfAbsent(backPath);
		TerminalUtils.syncExecute(new File(backPath), IPFS, "get", info.getIpfs());
		FileUtils.writFile(JSON.toJSONString(info), backPath+File.separator
			+info.getIpfs()+ StringParams.JSON.str());
		return ResultTemplateUtil.ofThrowable(null);
	}
}
