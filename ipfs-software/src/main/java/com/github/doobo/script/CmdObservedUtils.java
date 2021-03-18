package com.github.doobo.script;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Observable;

/**
 * 被观察者
 */
@Component
public class CmdObservedUtils extends Observable {

    private static CmdObserved INSTANCE;

    /**
     * 获取被观察者实例
     */
    public static CmdObserved getInstance(){
        if(INSTANCE == null){
            throw new IllegalArgumentException("CmdObserved is Undefined");
        }
        return INSTANCE;
    }

    @Autowired
    public void setObserved(CmdObserved observed) {
        INSTANCE = observed;
    }
}
