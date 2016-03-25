package com.snicesoft.framework;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.gson.internal.$Gson$Types;
import com.snicesoft.viewbind.AVKit;
import com.snicesoft.viewbind.ViewFinder;
import com.snicesoft.viewbind.base.IAv;
import com.snicesoft.viewbind.base.Proxy;
import com.snicesoft.viewbind.utils.LayoutUtils;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @param <HD>
 * @author zhe
 */
@SuppressWarnings("rawtypes")
public abstract class AVActivity<HD> extends Activity implements IAv, OnClickListener {
    private ViewFinder finder;
    protected HD _hd;

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
    public int layout() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LayoutUtils.getLayoutId(this, getThisClass()));
        finder = new ViewFinder(this);
        try {
            _hd = newHD();
        } catch (Exception e) {
            e.printStackTrace();
        }
        bindAll();
        onLoaded();
        loadNetData();
    }

    public Class<?> getThisClass() {
        Class<?> clazz = getClass();
        if (Proxy.PROXY_ACTIVITY.equals(clazz.getName())) {
            clazz = getClass().getSuperclass();
        }
        return clazz;
    }

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
