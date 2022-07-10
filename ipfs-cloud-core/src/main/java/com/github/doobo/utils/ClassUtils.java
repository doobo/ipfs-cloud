package com.github.doobo.utils;


/**
 * 常用类工具
 *
 * @Description: ipfs-cloud-core
 * @User: doobo
 * @Time: 2022-04-08 16:40
 */
public abstract class ClassUtils {

    /**
     * 基本类型判断:boolean,char,double,void,Long,Character等,String不是
     */
    public static boolean isBasisPrimitive(Class<?> clazz) {
        try {
            if (clazz.isPrimitive()) {
                return true;
            }
            return ((Class<?>) clazz.getField("TYPE").get(null)).isPrimitive();
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            return false;
        }
    }
}
