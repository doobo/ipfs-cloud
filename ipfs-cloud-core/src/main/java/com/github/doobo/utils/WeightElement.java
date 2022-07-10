package com.github.doobo.utils;

import java.io.Serializable;
import java.util.UUID;

/**
 * 随机元素
 *
 * @Description: analysis-ai-core
 * @User: diding
 * @Time: 2021-11-09 16:57
 */
public class WeightElement<T extends Serializable> implements Serializable,Cloneable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一键
     */
    private String uniqueKey;

    /**
     * 权重
     */
    private int count = 1;

    /**
     * 最小边界
     */
    private int min;

    /**
     * 最大边界
     */
    private int max;

    /**
     * 结果物
     */
    private transient T t;

    public WeightElement() {
        uniqueKey = UUID.randomUUID().toString();
    }

    public WeightElement(T t, int count) {
        uniqueKey = UUID.randomUUID().toString();
        this.t = t;
        this.count = count;
    }

    public WeightElement(int count) {
        this.count = count;
    }

    public WeightElement(T t) {
        this.t = t;
    }

    public int getCount() {
        return count;
    }

    public WeightElement<T> setCount(int count) {
        this.count = count;
        return this;
    }

    public int getMin() {
        return min;
    }

    public WeightElement<T> setMin(int min) {
        this.min = min;
        return this;
    }

    public int getMax() {
        return max;
    }

    public WeightElement<T> setMax(int max) {
        this.max = max;
        return this;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public T getT() {
        return t;
    }

    public WeightElement<T> setT(T t) {
        this.t = t;
        return this;
    }

    public static <T extends Serializable> WeightElement<T> newInstance(WeightElement<T> policy) {
        WeightElement<T> copyPolicy = new WeightElement<>();
        copyPolicy.setT(policy.getT())
            .setMax(policy.getMax())
            .setMin(policy.getMin())
            .setCount(policy.getCount());
        return copyPolicy;
    }
}
