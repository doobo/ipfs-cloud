package com.github.doobo.utils;

import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.github.doobo.utils.WeightRandomUtils.*;


/**
 * 权重随机算法
 *
 * @Description: ipfs-cloud-core
 * @User: doobo
 * @Time: 2021-11-09 16:56
 */
public class WeightRandom<T extends Serializable> {

    private List<WeightElement<T>> origin;

    private int sum = 0;

    public WeightRandom(List<WeightElement<T>> list){
        Assert.notEmpty(list,"数据源不能为空");
        origin = list;
        setMinAndMaxAndSum();
    }


    /**
     * 概率恒定获取多个随机元素
     */
    public synchronized List<T> getElementsByFixed(int amount){
      return getElementsByFixedUnsafe(amount);
    }

    /**
     * 概率恒定获取多个随机元素,不调用getNoRepeatElementsByDecrement方法,线程安全
     */
    public List<T> getElementsByFixedUnsafe(int amount){
        List<WeightElement<T>> list = new ArrayList<>(amount);
        for(int i = 0; i < amount; i++) {
            WeightElement<T> weightElement = getRandomElementByBinary();
            if (weightElement == null) {
                continue;
            }
            list.add(WeightElement.newInstance(weightElement));
        }
        return list.stream().map(WeightElement::getT).collect(Collectors.toList());
    }

    /**
     * 递减获取随机值,不重复
     */
    public synchronized List<T> getNoRepeatElementsByDecrement(int amount){
        long count = origin.stream().filter(Objects::nonNull).filter(f -> f.getCount() > 0).count();
        isTrue(count >= amount, "数据源不够！");
        List<WeightElement<T>> backup = null;
        try {
            backup = WeightRandomUtils.cloneCopy(origin);
        } catch (Exception e) {
            isNull(e, "备份数据源失败，请检测克隆方法");
        }
        List<WeightElement<T>> list = new ArrayList<>(amount);
        for (int i = 0; i < amount; i++) {
            WeightElement<T> tmp = getRandomElementByBinary();
            if (tmp == null) {
                continue;
            }
            list.add(WeightElement.newInstance(tmp));
            tmp.setCount(0);
            setMinAndMaxAndSum();
        }
        resetData(backup);
        return list.stream().map(WeightElement::getT).collect(Collectors.toList());
    }

    /**
     * 恒定概率获取不重复的多个元素
     */
    public synchronized List<T> getNoRepeatElementsByFixed(int amount){
       return getNoRepeatElementsByFixedUnsafe(amount);
    }

    /**
     * 恒定概率获取不重复的多个元素,不调用getNoRepeatElementsByDecrement方法,线程安全
     */
    public List<T> getNoRepeatElementsByFixedUnsafe(int amount){
        isTrue(WeightRandomUtils.hasValue(origin),"无数据源");
        long count = origin.stream().filter(Objects::nonNull).filter(f->f.getCount()>0).count();
        isTrue(count>=amount,"数据源不够！");
        HashMap<String, WeightElement<T>> result = new HashMap<>(amount);
        int i = 0;
        int max = amount*amount*amount;
        while (result.size() < amount && i < max){
            WeightElement<T> tmp = getRandomElementByBinary();
            if(tmp == null){
                i++;
                continue;
            }
            result.put(tmp.getUniqueKey(), WeightElement.newInstance(tmp));
            i++;
        }
        List<WeightElement<T>> list = new ArrayList<>(amount);
        list.addAll(result.values());
        Collections.shuffle(list);
        return list.stream().map(WeightElement::getT).collect(Collectors.toList());
    }

    /**
     * 获取原始值
     */
    public synchronized List<WeightElement<T>> getOrigin() {
        return origin;
    }

    /**
     * 获取一个随机元素
     */
    public synchronized T getOneElement(){
        return getOneUnsafe();
    }

    /**
     * 获取一个随机元素,不调用getNoRepeatElementsByDecrement方法，线程安全
     */
    public T getOneUnsafe(){
        long count = origin.stream().filter(Objects::nonNull).filter(f->f.getCount()>0).count();
        isTrue(count > 0, "数据源不够");
        WeightElement<T> weightElement = getRandomElementByBinary();
        notNull(weightElement,"数据拷贝失败，请检测克隆方法");
        return WeightElement.newInstance(weightElement).getT();
    }

    /**
     * 重置元素
     */
    private void resetData(List<WeightElement<T>> preTmp){
        if(WeightRandomUtils.hasValue(preTmp)){
            this.origin = preTmp;
            resetSum();
        }
    }

    /**
     * 重新计算和
     */
    private void resetSum(){
        if(WeightRandomUtils.hasValue(origin)){
            sum = origin.stream().filter(Objects::nonNull).mapToInt(WeightElement::getCount).sum();
        }
    }

    /**
     * 全部循环获取随机元素
     */
    private WeightElement<T> getWeightElementByAll(){
        int n,m;
        if (sum <= 0) {
            return null;
        }
        // n in [0, weightSum)
        n = ThreadLocalRandom.current().nextInt(sum);
        m = 0;
        for (WeightElement<T> o : origin) {
            if (m <= n && n < m + o.getCount()) {
                return o;
            }
            m += o.getCount();
        }
        return null;
    }

    /**
     * 二分法获取随机预测值
     */
    private WeightElement<T> getRandomElementByBinary(){
        int n;
        if (sum <= 0) {
            return null;
        }
        // n in [0, weightSum)
        n = ThreadLocalRandom.current().nextInt(sum);
        return binarySearch(origin, n);
    }

    /**
     * 设置边界值
     */
    private void setMinAndMaxAndSum(){
        int m = 0;
        if(WeightRandomUtils.hasValue(origin)) {
            for (WeightElement<T> o : origin) {
                o.setMin(m);
                m += o.getCount();
                o.setMax(m);
            }
            sum = m;
        }
    }

    /**
     * 二分法查找值
     */
    private WeightElement<T> binarySearch(List<WeightElement<T>> list, int key){
        isTrue(WeightRandomUtils.hasValue(list),"参数异常");
        int lo = 0;
        int hi = list.size() - 1;
        while(lo<=hi){
            int mid = lo + (hi-lo)/2;
            if(key < list.get(mid).getMin()) hi = mid - 1;
            else if(key >= list.get(mid).getMax()) lo = mid + 1;
            else return list.get(mid);
        }
        return null;
    }
}

