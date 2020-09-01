package com.github.doobo.utils;

import com.github.doobo.params.CustomException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 常用类工具
 * @author qpc
 */
public class ClassUtils {

	private ClassUtils() {
	}

	/**
     * 判断一个类型是Java本身的类型，还是用户自定义的类型
     * @param clz
     * @return
     */
    public static boolean isJavaClass(Class<?> clz) {
        return clz != null && clz.getClassLoader() == null;
    }

    /**
     * 判断是基本类还是封装类
     * .isPrimitive()是用来判断是否是基本类型的：void.isPrimitive() //true;
     * @param clz
     */
    public static boolean isWrapClass(Class clz) {
        try {
            return ((Class) clz.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     *
     * 获取除去Object的类继承关系
     * @param clz
     */
    public static List<Class<?>> getSuperClass(Class<?> clz){
        Class<?> superclass = clz.getSuperclass();
        List<Class<?>> all = new ArrayList<>();
        all.add(clz);
        while (superclass != null) {
            if(superclass.isAssignableFrom(Object.class)){
                break;
            }
            all.add(superclass);
            superclass = superclass.getSuperclass();
        }
        return all;
    }

    /**
     * 判断你一个类是否存在某个属性（字段）
     * @param fieldName 字段
     * @param obj   类对象
     * @return true:存在，false:不存在
     */
    public static boolean isExistFieldName(String fieldName, Object obj) {
        if (obj == null ||fieldName.isEmpty()) {
        	throw new CustomException("参数异常");
        }
        //获取这个类的所有属性
        Field[] fields = obj.getClass().getDeclaredFields();
        boolean flag = false;
        //循环遍历所有的fields
		for (Field field : fields) {
			if (field.getName().equals(fieldName)) {
				flag = true;
				break;
			}
		}
        return flag;
    }
}
