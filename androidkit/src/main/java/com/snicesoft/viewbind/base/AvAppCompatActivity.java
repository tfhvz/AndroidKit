package com.snicesoft.viewbind.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.gson.internal.$Gson$Types;
import com.snicesoft.framework.AVFragment;
import com.snicesoft.viewbind.AVKit;
import com.snicesoft.viewbind.ViewFinder;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * AppCompatActivity基类
 *
 * @param <HD>
 * @author zhe
 */
@SuppressWarnings("rawtypes")
@SuppressLint("NewApi")
public abstract class AvAppCompatActivity<HD> extends AppCompatActivity implements IAv, OnClickListener {
    private ViewFinder finder;
    protected HD _hd;

    public ViewFinder getFinder() {
        return finder;
    }

    @Override
    public final void bindAll() {
        bind(_hd);
    }

    @Override
    public final void bindTo(int id) {
        AVKit.bindTo(_hd, finder, id);
    }

    @Override
    public final void bind(Object holder) {
        AVKit.bind(holder, finder);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finder = new ViewFinder(this);
        try {
            _hd = newHD();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected final Fragment findFragmentById(int id) {
        return getSupportFragmentManager().findFragmentById(id);
    }

    protected final <F extends AVFragment> F findFragmentById(int id, Class<F> clazz) {
        return (F) getSupportFragmentManager().findFragmentById(id);
    }

    protected final Class<?> getThisClass() {
        Class<?> clazz = getClass();
        if (Proxy.PROXY_ACTIVITY.equals(clazz.getName()) || isProxy()) {
            clazz = getClass().getSuperclass();
        }
        return clazz;
    }

    public abstract boolean isProxy();

    HD newHD() throws Exception {
        Type type = getType(0);
        if (type == null)
            return null;
        Class hClass = $Gson$Types.getRawType(type);
        if (hClass == Void.class)
            return null;
        if (hClass.getName().contains(getThisClass().getName() + "$")) {
            if (Modifier.isStatic(hClass.getModifiers()))
                return (HD) hClass.newInstance();
            return (HD) hClass.getConstructors()[0].newInstance(this);
        }
        return (HD) hClass.newInstance();
    }

    Type getType(int index) {
        Type superclass = getThisClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            return null;
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[index]);
    }

    @Override
    public void onClick(View v) {
        if (v == null)
            return;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        _hd = null;
        finder = null;
    }
}