package com.github.doobo.script;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.github.doobo.conf.IpfsConfig;
import com.github.doobo.jms.ExchangeMsg;
import com.github.doobo.soft.InitUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Observable;
import java.util.Observer;

/**
 * 命名执行后的观察者
 */
@Slf4j
public abstract class IpfsObserver implements Observer {

	/**
	 * 获取公共解码密钥
	 */
	public abstract IpfsConfig getCurConfig();

	/**
	 * 返回当前的节点ID
	 */
	public String getNodeId(){
		return InitUtils.getNodeId();
	}

	/**
	 * 签名方法,默认不签名
	 */
	public boolean checkSign(ExchangeMsg msg){
		return Boolean.TRUE;
	}

    @Override
    public void update(Observable o, Object arg) {
        if(!(o instanceof IpfsObserved) || !(arg instanceof IpfsObserverVO)){
            return;
        }
        IpfsObserverVO vo = (IpfsObserverVO) arg;
		if(StringUtils.isBlank(vo.getLine())){
			return;
		}
		String msg;
		try {
			IpfsJsonVO ipo = JSON.parseObject(vo.getLine(), IpfsJsonVO.class);
			msg = ipo.getData();
			if(StringUtils.isBlank(msg)){
				return;
			}
			IpfsConfig curConfig = getCurConfig();
			if(curConfig == null){
				return;
			}
			msg = PwdUtils.decode(msg, curConfig.getSm2PrivateKey());
			if(msg == null || msg.isEmpty()){
				return;
			}
			ExchangeMsg exchangeMsg = JSON.parseObject(msg, ExchangeMsg.class);
			if(exchangeMsg == null){
				return;
			}
			//服务器节点通信,非当前节点不进入
			if(StringUtils.isNotBlank(exchangeMsg.getTargetCid())
				&& !exchangeMsg.getTargetCid().equals(getNodeId())){
				return;
			}
			//签名核对
			if(!checkSign(exchangeMsg)){
				log.warn("msg checkSign fail!");
				return;
			}
			//todo 组和区域以及topic和tag核对
			handleObserver(exchangeMsg);
		}catch (JSONException e){
			log.warn("data error", e);
		}
    }

    public abstract void handleObserver(ExchangeMsg msg);
}
