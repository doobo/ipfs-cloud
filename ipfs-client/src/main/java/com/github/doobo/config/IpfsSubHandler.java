package com.github.doobo.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.github.doobo.conf.IpfsConfig;
import com.github.doobo.model.IpfsPubVO;
import com.github.doobo.model.IpfsSubVO;
import com.github.doobo.params.ResultTemplate;
import com.github.doobo.script.IpfsJsonVO;
import com.github.doobo.script.IpfsObserver;
import com.github.doobo.script.IpfsObserverVO;
import com.github.doobo.script.PwdUtils;
import com.github.doobo.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 消息订阅与分发webSocket
 */
@Slf4j
@Component
public class IpfsSubHandler extends IpfsObserver  {

	@Resource
	private IpfsConfig ipfsConfig;

	@Override
	public void handleObserver(IpfsObserverVO vo) {
		try {
			if(StringUtils.isBlank(vo.getLine())){
				return;
			}
			IpfsJsonVO ipo;
			try {
				ipo = JSON.parseObject(vo.getLine(), IpfsJsonVO.class);
			}catch (JSONException e){
				log.info("异常数据", e);
				return;
			}
			String msg = ipo.getData();
			if(StringUtils.isBlank(msg)){
				return;
			}
			String data = PwdUtils.decode(msg, ipfsConfig.getMsgPwd());
			sendMsg(data, null);
		} catch (Exception e) {
			log.warn("SendBroadCastInfoError", e);
		}
	}

	/**
	 * 发送消息格式化
	 */
	public void sendMsg(String data, Throwable throwable){
		if(StringUtils.isBlank(data) && throwable != null){
			WebSocketServer.broadCastInfo(JSON.toJSONString(ResultUtils.ofThrowable(throwable)));
		}
		IpfsSubVO iso = JSON.parseObject(data,  IpfsSubVO.class);
		if(iso == null){
			return;
		}
		IpfsPubVO ipo = new IpfsPubVO();
		BeanUtils.copyProperties(iso, ipo);
		ResultTemplate<IpfsPubVO> res = ResultUtils.of(ipo);
		if(StringUtils.isNotBlank(iso.getMsg())){
			res.setOkMessage(iso.getMsg());
		}
		if(iso.getToSessionId() != null){
			WebSocketServer.sendMessage(JSON.toJSONString(res), iso.getToSessionId());
			return;
		}
		WebSocketServer.broadCastInfo(JSON.toJSONString(ResultUtils.of(ipo)));
	}
}
