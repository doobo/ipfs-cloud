package com.github.doobo.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.doobo.abs.AbstractPlatformObserver;
import com.github.doobo.bo.IpfsProperties;
import com.github.doobo.bo.PlatformObserverRequest;
import com.github.doobo.bo.PubMessageBO;
import com.github.doobo.bo.PubMsgEncodeBO;
import com.github.doobo.utils.Base64Utils;
import com.github.doobo.utils.SmUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.Observable;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 消息处理器
 *
 * @Description: ipfs-cloud
 * @User: doobo
 * @Time: 2022-09-09 22:18
 */
@Slf4j
public abstract class AbstractPlatformMsgHandler extends AbstractPlatformObserver {

	@Override
	public boolean matching(PlatformObserverRequest request) {
		return Objects.equals("topic", request.getType());
	}

	/**
	 * 实际执行入口
	 */
	@Override
	public void executor(PlatformObserverRequest request, Observable o) {
		PubMsgEncodeBO pubMsgEncodeBO = decodeMsg(request);
		handler(decodeBody(pubMsgEncodeBO), pubMsgEncodeBO);
	}

	/**
	 * 解码消息体
	 */
	protected PubMsgEncodeBO decodeMsg(PlatformObserverRequest request){
		PubMsgEncodeBO bo = new PubMsgEncodeBO();
		if(Objects.isNull(request) || StringUtils.isBlank(request.getMsg())){
			log.error("decodeMsg, msg is empty");
			return bo;
		}
		try{
			IpfsProperties ipfsConfig = AbstractPlatformInitHandler.RESPONSE.getIpfsConfig();
			if(Objects.isNull(ipfsConfig) || StringUtils.isBlank(ipfsConfig.getPrivateKey())){
				log.error("decodeMsg, ipfsConfig privateKey is empty");
				return bo;
			}
			String msg = request.getMsg();
			if(msg.startsWith("{") && msg.endsWith("}")) {
				JSONObject parseObject = JSON.parseObject(msg);
				String data = parseObject.getString("data");
				if(StringUtils.isBlank(data)){
					log.error("decodeMsg, msg.data is empty");
					return bo;
				}
				byte[] bytes = Base64Utils.decryptBASE64(data);
				if(Objects.isNull(bytes) || bytes.length == 0){
					log.error("decodeMsg, msg.data decrypt is empty");
					return bo;
				}
				String str = new String(bytes, UTF_8.name());
				String[] split = str.split(":");
				if(split.length != 5){
					log.error("decodeMsg, msg.data format is error");
					return bo;
				}
				bo.setType(split[0]);
				bo.setTarget(split[1]);
				bo.setPublicKey(split[3]);
				bo.setFrom(split[4]);
				bo.setMsg(SmUtils.decryptBySM2(ipfsConfig.getPrivateKey(), split[2]));
			}
		}catch (Throwable e){
			log.error("decodeMsgError:", e);
		}
		return bo;
	}

	/**
	 * 解析消息体
	 */
	protected PubMessageBO decodeBody(PubMsgEncodeBO encodeBO){
		PubMessageBO bo = new PubMessageBO();
		if(Objects.isNull(encodeBO) || StringUtils.isBlank(encodeBO.getMsg())){
			log.error("decodeBody msg is empty.");
			return bo;
		}
		try{
			bo = JSON.parseObject(encodeBO.getMsg(), PubMessageBO.class);
		}catch (Throwable e){
			log.error("parse msg error:", e);
		}
		return bo;
	}

	public abstract void handler(PubMessageBO body, PubMsgEncodeBO msg);
}
