package com.snicesoft.viewbind.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.gson.internal.$Gson$Types;
import com.snicesoft.viewbind.AVKit;
import com.snicesoft.viewbind.ViewFinder;
import com.snicesoft.viewbind.pluginmgr.Proxy;
import com.snicesoft.viewbind.rule.IHolder;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * FragmentActivity基类
 *
 * @param <H>
 * @param <D>
 * @author zhe
 */
@SuppressWarnings("rawtypes")
@SuppressLint("NewApi")
public class AvAppCompatActivity<H extends IHolder, D> extends AppCompatActivity implements IAv<H, D>, OnClickListener {
    private ViewFinder finder;
    protected D _data;
    protected H _holder;

    @Override
    public final void dataBindAll() {
        AVKit.dataBind(_data, finder);
    }

    @Override
    public final void dataBindTo(String fieldName) {
        AVKit.dataBindTo(_data, finder, fieldName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LayoutUtils.getLayoutId(getThisClass()));
        try {
            _holder = newHolder();
            _data = newData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finder = new ViewFinder(this);
        AVKit.initHolder(_holder, finder);
        dataBindAll();
    }

    public Class<?> getThisClass() {
        Class<?> clazz = getClass();
        if (Proxy.PROXY_ACTIVITY.equals(clazz.getName())) {
            clazz = getClass().getSuperclass();
        }
        return clazz;
    }

    D newData() throws Exception {
        Class dClass = $Gson$Types.getRawType(getType(1));
        if (dClass == Void.class)
            return null;
        if (dClass.getName().contains(getThisClass().getName() + "$")) {
            if (Modifier.isStatic(dClass.getModifiers()))
                return (D) dClass.newInstance();
            return (D) dClass.getConstructors()[0].newInstance(this);
        }
        return (D) dClass.newInstance();
    }

    H newHolder() throws Exception {
        Class hClass = $Gson$Types.getRawType(getType(0));
        if (hClass == IHolder.class)
            return null;
        if (hClass.getName().contains(getThisClass().getName() + "$")) {
            if (Modifier.isStatic(hClass.getModifiers()))
                return (H) hClass.newInstance();
            return (H) hClass.getConstructors()[0].newInstance(this);
        }
        return (H) hClass.newInstance();
    }

    Type getType(int index) {
        Type superclass = getThisClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[index]);
    }

    @Override
    public void onClick(View v) {
        if (v == null)
            return;
    }
}