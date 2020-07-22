package com.github.doobo.utils;


import com.github.doobo.weight.WeightParent;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 对象基本工具
 */
public class CommonUtils {

    /**
     * 是否有值
     * @param collection
     * @return
     */
    public static boolean hasValue(Collection<?> collection){
        return (collection == null || collection.isEmpty())?false:true;
    }

    public static boolean hasValue(Map map){
        return (map == null || map.isEmpty())?false:true;
    }

    public static boolean hasValue(Object[] arr){
        return (arr == null || arr.length == 0)?false:true;
    }

    public static boolean hasValue(Object obj){
        return obj == null?false:true;
    }

    public static boolean hasValue(String str){
        return (str == null || str.isEmpty())?false:true;
    }

  /**
   * List实现深拷贝，拷贝对象需要实现了序列化
   * @param src
   * @param <T>
   * @return
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
    ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(byteOut);
    out.writeObject(src);

    ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
    ObjectInputStream in = new ObjectInputStream(byteIn);
    List<T> dest = (List<T>) in.readObject();
    return dest;
  }

  /**
   * WeightParent 对象克隆
   * @param src
   * @return
   * @throws Exception
   */
  public static <T> List<WeightParent<T>> cloneCopy(List<WeightParent<T>> src) throws CloneNotSupportedException {
    if(!hasValue(src)){
      return null;
    }
    List<WeightParent<T>> result = new ArrayList<>(src.size());
    for(WeightParent t: src){
      result.add((WeightParent<T>)t.clone());
    }
    return result;
  }
}
