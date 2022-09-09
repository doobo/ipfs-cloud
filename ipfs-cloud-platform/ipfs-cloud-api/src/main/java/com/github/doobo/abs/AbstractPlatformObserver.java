package com.github.doobo.abs;

import com.github.doobo.bo.PlatformObserverRequest;

import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

/**
 * 观察者抽象类(主动),实际执行者
 *
 * @Description: ipfs-cloud
 * @User: diding
 * @Time: 2022-09-09 13:45
 */
public abstract class AbstractPlatformObserver implements Observer {

	private boolean isLeave;

	@Override
	public void update(Observable o, Object arg) {
		//如果是已经废弃的观察者,直接退出执行
		if(isLeave()){
			return;
		}
		if (isType(o, arg)) {
			PlatformObserverRequest request = (PlatformObserverRequest) arg;
			Optional.of(request).filter(this::matching).ifPresent(c -> this.executor(c, o));
		}
	}

	/**
	 * 类型是否匹配
	 */
	protected boolean isType(Observable o, Object arg){
		return o instanceof AbstractPlatformObserved && arg instanceof PlatformObserverRequest;
	}

	public void setLeave(boolean leave) {
		isLeave = leave;
	}

	public boolean isLeave() {
		return isLeave;
	}

	/**
	 * 执行器是否匹配,用于单个方法执行,与事件触发者无关
	 */
	public boolean matching(PlatformObserverRequest request){
		return Boolean.TRUE;
	}

	public abstract void executor(PlatformObserverRequest request, Observable o);
}
