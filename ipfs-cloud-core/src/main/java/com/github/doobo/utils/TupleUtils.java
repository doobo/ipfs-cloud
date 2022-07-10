package com.github.doobo.utils;

import com.github.doobo.vbo.TupleThree;
import com.github.doobo.vbo.TupleTwo;

/**
 * Tuple元组类型工具
 *
 * @Description: ipfs-cloud-core
 * @User: doobo
 * @Time: 2022-02-11 16:42
 */
public abstract class TupleUtils {

    /**
     * 创建一个包含2个元素的元组
     */
    public static <A, B> TupleTwo<A, B> ofTuple(final A a, final B b) {
        return new TupleTwo<>(a, b);
    }

    /**
     * 创建一个包含3个元素的元组
     */
    public static <A, B, C> TupleThree<A, B, C> ofTupleThird(final A a, final B b, final C c) {
        return new TupleThree<>(a, b, c);
    }
}
