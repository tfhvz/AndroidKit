package com.snicesoft.viewbind.base;

import android.content.Context;

import com.snicesoft.viewbind.annotation.AutoController;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by zhuzhe on 16/1/7.
 */
class AutoControllerUtils {
    public static void loadController(Context context, Class clazz, Object object) {
        Field[] fs = clazz.getDeclaredFields();
        for (Field f : fs) {
            if (f.getAnnotation(AutoController.class) == null)
                continue;
            f.setAccessible(true);
            try {
                Class fType = (Class) f.getGenericType();
                Constructor constructor = fType.getConstructor(Context.class);
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