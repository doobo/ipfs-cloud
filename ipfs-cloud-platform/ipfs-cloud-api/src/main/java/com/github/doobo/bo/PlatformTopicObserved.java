package com.github.doobo.bo;

import com.github.doobo.abs.AbstractPlatformObserved;

import java.util.List;
import java.util.Objects;
import java.util.Observer;

/**
 * 平台广播的事件触发者
 *
 * @Description: ipfs-cloud
 * @User: doobo
 * @Time: 2022-09-09 15:14
 */
public class PlatformTopicObserved extends AbstractPlatformObserved {

	private List<Observer> list;

	public PlatformTopicObserved() {
	}

	public PlatformTopicObserved(List<Observer> list) {
		this.list = list;
		if(Objects.nonNull(list) && !list.isEmpty()){
			list.forEach(this::addObserver);
		}
	}

	public List<Observer> getList() {
		return list;
	}

	public void setList(List<Observer> list) {
		this.list = list;
	}
}
