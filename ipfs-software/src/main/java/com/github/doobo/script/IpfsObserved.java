package com.github.doobo.script;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;

/**
 * 被观察者
 */
@Component
public class IpfsObserved extends Observable {

    private static boolean IS_OBSERVE;

    private List<Observer> observerList;

    @PostConstruct
    public void observerRegister() {
        if(observerList == null || observerList.isEmpty()) {
			return;
        }
		List<Observer> collect = observerList.stream().filter(f -> f instanceof IpfsObserver).collect(Collectors.toList());
		if(collect.isEmpty()){
			return;
		}
		collect.forEach(this::addObserver);
		observerList = collect;
		IS_OBSERVE = true;
    }

    /**
     * 广播信息
     */
    public synchronized void sendCmdResult(IpfsObserverVO vo){
        this.setChanged();
        this.notifyObservers(vo);
    }

    /**
     * 是否有观察者
     */
    public static boolean isObserver(){
        return IS_OBSERVE;
    }

	/**
	 * 设置是否有观察者
	 * @param flag
	 */
	public void setObserver(Boolean flag){
    	IS_OBSERVE = flag;
	}

    /**
     * 获取所有观察者
     */
    public List<Observer> getObserverList() {
        return observerList;
    }

	@Autowired(required = false)
	public void setObserverList(List<Observer> observerList) {
		this.observerList = observerList;
	}
}
