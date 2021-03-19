package com.github.doobo.script;

import java.util.Observable;
import java.util.Observer;

/**
 * 命名执行后的观察者
 */
public abstract class IpfsObserver implements Observer {

    @Override
    public void update(Observable o, Object arg) {
        if(!(o instanceof IpfsObserved) || !(arg instanceof IpfsObserverVO)){
            return;
        }
        IpfsObserverVO vo = (IpfsObserverVO) arg;
        handleObserver(vo);
    }

    public abstract void handleObserver(IpfsObserverVO vo);
}
