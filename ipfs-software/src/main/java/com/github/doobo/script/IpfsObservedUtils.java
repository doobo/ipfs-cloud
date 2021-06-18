package com.github.doobo.script;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 被观察者工具类
 */
@Component
public class IpfsObservedUtils {

    private static IpfsObserved INSTANCE;

    /**
     * 获取被观察者实例
     */
    public static IpfsObserved getInstance(){
        if(INSTANCE == null){
            throw new IllegalArgumentException("IpfsObserved is undefined");
        }
        return INSTANCE;
    }

    @Autowired
    public void setObserved(IpfsObserved observed) {
        INSTANCE = observed;
    }

	/**
	 * 是否有观察者
	 */
	public static boolean isObserver(){
		return IpfsObserved.isObserver();
	}
}
