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
public class CmdObserved extends Observable {

    private static boolean IS_OBSERVE;

    private List<Observer> observerList;

    @PostConstruct
    public void observerRegister() {
        if(observerList != null && !observerList.isEmpty()) {
            observerList.stream().filter(f->f instanceof CmdObserver).forEach(this::addObserver);
            if(observerList.stream().anyMatch(f -> f instanceof CmdObserver)){
                observerList = observerList.stream().filter(f->f instanceof CmdObserver).collect(Collectors.toList());
                IS_OBSERVE = true;
            }
        }
    }

    /**
     * 广播信息
     * @param vo
     */
    public synchronized void sendCmdResult(ObServerVO vo){
        this.setChanged();
        this.notifyObservers(vo);
    }

    /**
     * 是否有观察者
     */
    public boolean isObserver(){
        return IS_OBSERVE;
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
