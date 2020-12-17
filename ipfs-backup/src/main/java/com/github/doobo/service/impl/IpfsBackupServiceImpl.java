package com.github.doobo.service.impl;

import cn.yours.elfinder.core.Volume;
import cn.yours.elfinder.service.ElfinderStorage;
import com.alibaba.fastjson.JSON;
import com.github.doobo.model.IpfsFileInfo;
import com.github.doobo.params.StringParams;
import com.github.doobo.service.IpfsBackupService;
import com.github.doobo.soft.InitUtils;
import com.github.doobo.utils.DateUtils;
import com.github.doobo.utils.FileUtils;
import com.github.doobo.utils.TerminalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.io.File;
import java.util.Objects;

import static com.github.doobo.soft.InitUtils.IPFS;

/**
 * 文件备份处理服务
 */
@Slf4j
@Service
public class IpfsBackupServiceImpl implements IpfsBackupService {

	@Resource
	ElfinderStorage elfinderStorage;

	/**
	 * 备份文件服务
	 * 根据需要,可记录失败告警日志等
	 * @param info
	 */
	@Override
	@Async("customServiceExecutor")
	public void backUpFile(IpfsFileInfo info) {
		String backPath = DateUtils.formatDateByFormat(null, DateUtils.DateFormat.yMdH.getFt());
		if(elfinderStorage == null || elfinderStorage.getVolumes() == null
			|| elfinderStorage.getVolumes().isEmpty()){
			return;
		}
		boolean exit = InitUtils.existIpfsFile(info.getIpfs());
		if(!exit){
			log.error("file not found,cid:{}", info.getIpfs());
		}
		Volume volume = Objects.requireNonNull(elfinderStorage.getVolumes().get(0));
		backPath = volume.getRoot().toString() + File.separator + backPath;
		FileUtils.createDirIfAbsent(backPath);
		TerminalUtils.syncExecute(new File(backPath), IPFS, "get", info.getIpfs());
		FileUtils.writFile(JSON.toJSONString(info), backPath+File.separator
			+info.getIpfs()+ StringParams.JSON.str());
	}
}
