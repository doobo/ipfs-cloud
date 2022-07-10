package com.github.doobo.utils;

import org.springframework.lang.Nullable;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 权重随机数工具
 *
 * @Description: ipfs-cloud-core
 * @User: doobo
 * @Time: 2021-11-09 16:59
 */
public abstract class WeightRandomUtils {

    /**
     * 是否有值
     */
    public static boolean hasValue(Collection<?> collection){
        return collection != null && !collection.isEmpty();
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(@Nullable Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isNull(@Nullable Object object, String message) {
        if (object != null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * List实现深拷贝，拷贝对象需要实现了序列化
     */
    public static <T extends Serializable> List<T> deepCopy(List<T> src) throws Exception {
        try(ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut)) {
            out.writeObject((Serializable)src);
            try(ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
                ObjectInputStream in = new ObjectInputStream(byteIn)) {
                return (List<T>) in.readObject();
            }
        }
    }

    /**
     * WeightParent 对象克隆
     */
    public static <T extends Serializable> List<WeightElement<T>> cloneCopy(List<WeightElement<T>> src) {
        if(!hasValue(src)){
            return null;
        }
        List<WeightElement<T>> result = new ArrayList<>(src.size());
        for(WeightElement<T> t: src){
            result.add(WeightElement.newInstance(t));
        }
        return result;
    }

    /**
     * 通过List获取随机数
     */
    public static <T extends Serializable> WeightRandom<T> ofList(List<T> rst){
        isTrue(hasValue(rst), "入参异常");
        List<WeightElement<T>> origin = new ArrayList<>();
        rst.forEach(m->{
            origin.add(new WeightElement<T>(m, 1));
        });
        return new WeightRandom<>(origin);
    }

    /**
     * 通过List和指定概率获取随机数
     */
    public static <T extends Serializable> WeightRandom<T> ofList(List<T> rst, List<Integer> ws){
        isTrue(hasValue(rst), "入参异常");
        List<Integer> wgs = new ArrayList<>();
        if(hasValue(ws)){
            wgs.addAll(ws);
        }
        wgs = wgs.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if(!hasValue(wgs)){
            wgs = new ArrayList<>();
        }
        if(wgs.size() < rst.size()){
            int dx = rst.size() - wgs.size();
            for(int i = 0; i < dx; i ++){
                wgs.add(1);
            }
        }
        List<WeightElement<T>> origin = new ArrayList<>();
        int i = 0;
        for (T m : rst) {
            origin.add(new WeightElement<T>(m, wgs.get(i)));
            i += 1;
        }
        return new WeightRandom<>(origin);
    }
}

