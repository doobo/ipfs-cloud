package com.github.doobo.script;

import java.util.Observable;
import java.util.Observer;

/**
 * 命名执行后的观察者
 */
public abstract class CmdObserver implements Observer {

    @Override
    public void update(Observable o, Object arg) {
        if(!(o instanceof CmdObserved) || !(arg instanceof ObServerVO)){
            return;
        }
        ObServerVO vo = (ObServerVO) arg;
        handleObserver(vo);
    }

    public abstract void handleObserver(ObServerVO vo);
}
