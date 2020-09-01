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
     */
    public static boolean hasValue(Collection<?> collection){
        return collection != null && !collection.isEmpty();
    }

    public static boolean hasValue(Map map){
        return map != null && !map.isEmpty();
    }

    public static boolean hasValue(Object[] arr){
        return arr != null && arr.length != 0;
    }

    public static boolean hasValue(Object obj){
        return obj != null;
    }

    public static boolean hasValue(String str){
        return str != null && !str.isEmpty();
    }

  /**
   * List实现深拷贝，拷贝对象需要实现了序列化
   * @param src
   * @param <T>
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
   * @param src
   */
  public static <T extends Serializable> List<WeightParent<T>> cloneCopy(List<WeightParent<T>> src) {
    if(!hasValue(src)){
      return null;
    }
    List<WeightParent<T>> result = new ArrayList<>(src.size());
    for(WeightParent<T> t: src){
      result.add(WeightParent.newInstance(t));
    }
    return result;
  }
}
