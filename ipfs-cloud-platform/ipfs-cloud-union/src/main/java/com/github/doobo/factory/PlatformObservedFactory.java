package com.github.doobo.factory;


import com.github.doobo.abs.AbstractPlatformObserver;
import com.github.doobo.bo.PlatformObserverRequest;
import com.github.doobo.bo.PlatformTopicObserved;
import com.github.doobo.utils.ResultUtils;
import com.github.doobo.vbo.HookTuple;
import com.github.doobo.vbo.ResultTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 平台观察者模型工厂
 *
 * @Description: ipfs-cloud-union
 * @User: doobo
 * @Time: 2022-09-09 13:30
 */
@Slf4j
@Component
public class PlatformObservedFactory {

    /**
     * 导入处理器列表
     */
    private static List<Observer> handlerList;

	/**
	 * 平台消息触发器
	 */
	private static PlatformTopicObserved platformTopicObserved;

    /**
     * 添加处理器
     */
    public static synchronized void addHandler(Observer handler){
        if(Objects.isNull(handlerList)){
            handlerList = new CopyOnWriteArrayList<>();
        }
        if(Objects.nonNull(handler) && handler instanceof AbstractPlatformObserver){
            handlerList.add(handler);
        }
    }

    /**
     * 添加理器列表
     */
    public static synchronized void addHandlerList(List<Observer> handlers){
        if(Objects.isNull(handlerList)){
            handlerList = new CopyOnWriteArrayList<>();
        }
        if(Objects.nonNull(handlers) && !handlers.isEmpty()){
			List<Observer> collect = handlers.stream().filter((f) -> f instanceof AbstractPlatformObserver).collect(Collectors.toList());
			if(!collect.isEmpty()){
				handlerList.addAll(collect);
			}
        }
    }

    /**
     * 移除指定类型的处理器
     */
    public static synchronized <T> boolean removeHandler(Class<T> cls){
		Observer handler = null;
        for(Observer item : handlerList){
            if(Objects.nonNull(item) && item.getClass().getName().equals(cls.getName())){
                handler = item;
                break;
            }
        }
        if(Objects.nonNull(handler)){
			if(handler instanceof AbstractPlatformObserver){
				((AbstractPlatformObserver) handler).setLeave(true);
			}
            return handlerList.remove(handler);
        }
        return false;
    }

	/**
     * 具体执行器
     */
    public static <T> ResultTemplate<T> executeHandler(PlatformObserverRequest request, Function<AbstractPlatformObserver, ResultTemplate<T>> fun){
		AbstractPlatformObserver handler = getInstanceHandler(request);
        return execute(handler, fun, request, null);
    }

    /**
     * 具体执行器
     */
    public static <T> ResultTemplate<T> executeHandler(PlatformObserverRequest request, Function<AbstractPlatformObserver, ResultTemplate<T>> fun, HookTuple tuple){
		AbstractPlatformObserver handler = getInstanceHandler(request);
        return execute(handler, fun, request, tuple);
    }

    /**
     * 获取处理器
     */
    public static AbstractPlatformObserver getInstanceHandler(PlatformObserverRequest request){
        if(isEmpty(handlerList)){
            return null;
        }
        for(Observer item : handlerList){
            if (item instanceof AbstractPlatformObserver ){
				AbstractPlatformObserver platformObserver = (AbstractPlatformObserver) item;
				if(matching(request, platformObserver::matching)){
					return platformObserver;
				}
            }
        }
        return null;
    }

    /**
     * 判断,并具体执行方法
     */
    private static <T> ResultTemplate<T> execute(AbstractPlatformObserver handler, Function<AbstractPlatformObserver, ResultTemplate<T>> fun, PlatformObserverRequest request, HookTuple tuple) {
        if(Objects.isNull(handler)){
            return ResultUtils.ofFail("未匹配到执行器");
        }
        try {
            Optional.ofNullable(tuple).flatMap(c -> Optional.ofNullable(c.beforeTuple(handler))).ifPresent(n -> n.accept(request));
            return fun.apply(handler);
        }catch (Throwable e){
            if(Objects.isNull(tuple)){
                throw e;
            }
            Consumer<Object> orElse = Optional.ofNullable(tuple.errorTuple(e)).orElse(null);
            if(Objects.isNull(orElse)){
                throw e;
            }
            orElse.accept(request);
        }finally {
            Optional.ofNullable(tuple).flatMap(c -> Optional.ofNullable(c.endTuple(handler))).ifPresent(n -> n.accept(request));
        }
        return ResultUtils.ofFail("方法执行异常");
    }

    /**
     * 匹配执行器
     */
    private static boolean matching(PlatformObserverRequest request, Predicate<PlatformObserverRequest> predicate){
        return predicate.test(request);
    }

    public static List<Observer> getHandlerList() {
        return handlerList;
    }

	/**
	 * 初始化事件触发者
	 */
	@Bean
	@ConditionalOnMissingBean(PlatformTopicObserved.class)
	public PlatformTopicObserved platformTopicObserved(@Autowired(required = false) List<Observer> list) {
		PlatformObservedFactory.addHandlerList(list);
		PlatformObservedFactory.platformTopicObserved = new PlatformTopicObserved(getHandlerList());
		return PlatformObservedFactory.platformTopicObserved;
	}

	public void setPlatformTopicObserved(PlatformTopicObserved platformTopicObserved) {
		PlatformObservedFactory.platformTopicObserved = platformTopicObserved;
	}

	public static PlatformTopicObserved getPlatformTopicObserved() {
		return platformTopicObserved;
	}

	public static boolean isEmpty(Collection<?> coll) {
		return coll == null || coll.isEmpty();
	}

	/**
	 * SPI注册所有实现类
	 */
	public static synchronized void registerHandlerList() {
		ServiceLoader<Observer> filtersImplements = ServiceLoader.load(Observer.class);
		List<Observer> receiptHandlerList = new ArrayList<>();
		//把找到的所有的Filter的实现类放入List中
		for (Observer filtersImplement : filtersImplements) {
			receiptHandlerList.add(filtersImplement);
		}
		if(isEmpty(receiptHandlerList)){
			return;
		}
		addHandlerList(receiptHandlerList);
	}

	//注册处理器
	static {
		registerHandlerList();
	}
}
