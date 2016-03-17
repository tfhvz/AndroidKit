package com.snicesoft.viewbind.utils;

import com.snicesoft.viewbind.annotation.Context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by zhuzhe on 16/1/7.
 */
public class AutoUtils {
    public static void loadContext(android.content.Context context, Class clazz, Object object) {
        Field[] fs = clazz.getDeclaredFields();
        for (Field f : fs) {
            if (f.getAnnotation(Context.class) == null)
                continue;
            f.setAccessible(true);
            try {
                Class fType = (Class) f.getGenericType();
                Constructor constructor = fType.getConstructor(android.content.Context.class);
                if (constructor != null) {
                    f.set(object, constructor.newInstance(context));
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}