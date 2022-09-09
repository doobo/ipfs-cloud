package com.github.doobo.abs;

import com.github.doobo.bo.PlatformObserverRequest;

import java.util.Observable;

/**
 * 被观察抽象类(可观察),事件触发者
 *
 * @Description: ipfs-cloud
 * @User: diding
 * @Time: 2022-09-09 13:53
 */
public abstract class AbstractPlatformObserved extends Observable {

	public synchronized void notifyChange(PlatformObserverRequest request) {
		this.setChanged();
		this.notifyObservers(request);
	}
}
