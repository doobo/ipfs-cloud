package com.github.doobo.factory;


import com.github.doobo.bo.PlatformInitRequest;
import com.github.doobo.handler.PlatformInitHandler;
import com.github.doobo.utils.ResultUtils;
import com.github.doobo.vbo.HookTuple;
import com.github.doobo.vbo.ResultTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 平台初始化工厂
 *
 * @Description: ipfs-cloud-union
 * @User: doobo
 * @Time: 2022-067-29 16:34
 */
@Slf4j
@Component
public class PlatformInitFactory {

    /**
     * 导入处理器列表
     */
    private static List<PlatformInitHandler> handlerList;

    /**
     * 添加字段处理器
     */
    public static synchronized void addHandler(PlatformInitHandler handler){
        if(Objects.isNull(handlerList)){
            handlerList = new CopyOnWriteArrayList<>();
        }
        if(Objects.nonNull(handler)){
            handlerList.add(handler);
            handlerList.sort(Comparator.comparing(PlatformInitHandler::getPhase));
        }
    }

    /**
     * 添加字段处理器列表
     */
    public static synchronized void addHandlerList(List<PlatformInitHandler> handlers){
        if(Objects.isNull(handlerList)){
            handlerList = new CopyOnWriteArrayList<>();
        }
        if(Objects.nonNull(handlers) && !handlers.isEmpty()){
            handlerList.addAll(handlers);
            handlerList.sort(Comparator.comparing(PlatformInitHandler::getPhase));
        }
    }

    /**
     * 移除指定类型的处理器
     */
    public static synchronized <T> boolean removeHandler(Class<T> cls){
        PlatformInitHandler handler = null;
        for(PlatformInitHandler item : handlerList){
            if(Objects.nonNull(item) && item.getClass().getName().equals(cls.getName())){
                handler = item;
                break;
            }
        }
        if(Objects.nonNull(handler)){
            return handlerList.remove(handler);
        }
        return false;
    }

    /**
     * 具体执行器
     */
    public static <T> ResultTemplate<T> executeHandler(PlatformInitRequest request, Function<PlatformInitHandler, ResultTemplate<T>> fun){
        PlatformInitHandler handler = getInstanceHandler(request);
        return execute(handler, fun, request, null);
    }

    /**
     * 具体执行器
     */
    public static <T> ResultTemplate<T> executeHandler(PlatformInitRequest request, Function<PlatformInitHandler, ResultTemplate<T>> fun, HookTuple tuple){
        PlatformInitHandler handler = getInstanceHandler(request);
        return execute(handler, fun, request, tuple);
    }

    /**
     * 获取处理器
     */
    public static PlatformInitHandler getInstanceHandler(PlatformInitRequest request){
        if(isEmpty(handlerList)){
            return null;
        }
        for(PlatformInitHandler item : handlerList){
            if (matching(request, item::matching)){
                return item;
            }
        }
        return null;
    }

    /**
     * 判断,并具体执行方法
     */
    private static <T> ResultTemplate<T> execute(PlatformInitHandler handler, Function<PlatformInitHandler, ResultTemplate<T>> fun, PlatformInitRequest request, HookTuple tuple) {
        if(Objects.isNull(handler)){
            return ResultUtils.ofFail("未匹配到执行器");
        }
        try {
            Optional.ofNullable(tuple)
                    .ifPresent(c -> Optional.ofNullable(c.beforeTuple(handler))
                        .ifPresent(n -> n.accept(request)));
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
            Optional.ofNullable(tuple)
                    .ifPresent(c -> Optional.ofNullable(c.endTuple(handler))
                        .ifPresent(n->n.accept(request)));
        }
        return ResultUtils.ofFail("方法执行异常");
    }

    /**
     * 匹配执行器
     */
    private static boolean matching(PlatformInitRequest request, Predicate<PlatformInitRequest> predicate){
        return predicate.test(request);
    }

    @Autowired(required = false)
    public void setHandlerList(List<PlatformInitHandler> handlerList){
		PlatformInitFactory.addHandlerList(handlerList);
    }

    public static List<PlatformInitHandler> getHandlerList() {
        return handlerList;
    }

	public static boolean isEmpty(Collection<?> coll) {
		return coll == null || coll.isEmpty();
	}

	/**
	 * SPI注册所有实现类
	 */
	public static synchronized List<PlatformInitHandler> registerHandlerList() {
		ServiceLoader<PlatformInitHandler> filtersImplements = ServiceLoader.load(PlatformInitHandler.class);
		List<PlatformInitHandler> receiptHandlerList = new ArrayList<>();
		//把找到的所有的Filter的实现类放入List中
		for (PlatformInitHandler filtersImplement : filtersImplements) {
			receiptHandlerList.add(filtersImplement);
		}
		if(isEmpty(receiptHandlerList)){
			return receiptHandlerList;
		}
		receiptHandlerList.sort(Comparator.comparing(PlatformInitHandler::getPhase));
		addHandlerList(receiptHandlerList);
		return receiptHandlerList;
	}
}
