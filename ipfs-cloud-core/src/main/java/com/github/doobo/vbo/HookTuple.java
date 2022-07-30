package com.github.doobo.vbo;

import java.util.function.Consumer;

/**
 * 钩子型元组
 *
 * @Description: ipfs-cloud-core
 * @User: diding
 * @Time: 2022-07-29 13:47
 */
public interface HookTuple {

    /**
     * 下一步执行钩子
     */
    default <K, V> Consumer<V> beforeTuple(K k){
        return null;
    }

    /**
     * 结束型钩子
     */
    default <K, V> Consumer<V> endTuple(K k){
        return null;
    }

    /**
     * 异常型钩子
     */
     default <T> Consumer<T> errorTuple(Throwable e){
         return null;
     };
}
