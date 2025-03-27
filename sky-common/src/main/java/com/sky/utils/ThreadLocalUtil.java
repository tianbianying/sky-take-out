package com.sky.utils;

/**
 * @description: ThreadLocal的工具类
 * @author: Excell
 * @data 2025年03月17日 20:46
 */
public class ThreadLocalUtil {
    private static final ThreadLocal threadLocal = new ThreadLocal<Object>();

    public static <T> T get() {
        return (T) threadLocal.get();
    }

    public static void set(Object o) {
        threadLocal.set(o);
    }

    public static void remove() {
        threadLocal.remove();
    }


}
