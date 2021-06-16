package com.github.doobo.start;

import com.github.doobo.conf.IpfsConfig;
import com.github.doobo.soft.InitUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;

import javax.annotation.Resource;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 消息订阅与分发webSocket
 */
@Slf4j
public abstract class AbstractIpfsSubStart implements SmartLifecycle {

	@Resource
	private IpfsConfig ipfsConfig;

	private volatile boolean isRunning = false;

	private ScheduledExecutorService service = null;

	/**
	 * 启动Ipfs订阅
	 */
	@Override
	public void start() {
		isRunning = true;
		//启用定时检测
		if(ipfsConfig.isCron()){
			service = Executors.newScheduledThreadPool(1);
			service.scheduleAtFixedRate(() -> {
				try {
					if(InitUtils.initSub(ipfsConfig)){
						log.info("Ipfs订阅消息成功:{}",  ipfsConfig.getTopic());
					}else{
						log.info("Ipfs订阅消息失败");
					}
				} catch (Exception e) {
					log.info("startIpfsSubError", e);
				}
			}, ipfsConfig.getDelay(), ipfsConfig.getFixedDelay(), TimeUnit.SECONDS);
			return;
		}
		//不启用定时检测
		try {
			if(InitUtils.initSub(ipfsConfig)){
				log.info("Ipfs订阅消息成功:{}",  ipfsConfig.getTopic());
			}else{
				log.info("Ipfs订阅消息失败");
			}
		} catch (Exception e) {
			log.info("startIpfsSubError", e);
		}
	}

	/**
	 * SmartLifecycle子类的才有的方法，当isRunning方法返回true时，该方法才会被调用。
	 */
	@Override
	public void stop(Runnable callback) {
		callback.run();
		isRunning = false;
		if(service != null){
			service.shutdownNow();
		}
		log.info("IpfsSubInit stop");
	}

	/**
	 * 接口Lifecycle的子类的方法，只有非SmartLifecycle的子类才会执行该方法
	 * 1. 该方法只对直接实现接口Lifecycle的类才起作用，对实现SmartLifecycle接口的类无效
	 * 2. 方法stop()和方法stop(Runnable callback)的区别只在于，后者是SmartLifecycle子类的专属。
	 */
	@Override
	public void stop() {
		isRunning = false;
		log.info("stop can't run");
	}

	@Override
	public int getPhase() {
		return 100;
	}

	/**
	 * 1. 只有该方法返回false时，start方法才会被执行
	 * 2. 只有该方法返回true时，stop(Runnable callback)或stop()方法才会被执行。
	 */
	@Override
	public boolean isRunning() {
		return isRunning;
	}
}
