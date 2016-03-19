package com.snicesoft.viewbind.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by zhuzhe on 16/1/7.
 */
public class AutoUtils {
    public static void loadContext(android.content.Context context, Field field, Object object) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        field.setAccessible(true);
        Class fType = (Class) field.getGenericType();
        Constructor constructor = fType.getConstructor(android.content.Context.class);
        if (constructor != null) {
            field.set(object, constructor.newInstance(context));
        }
    }
}