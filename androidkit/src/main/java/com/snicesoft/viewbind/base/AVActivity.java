package com.snicesoft.viewbind.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.gson.internal.$Gson$Types;
import com.snicesoft.viewbind.AVKit;
import com.snicesoft.viewbind.ViewFinder;
import com.snicesoft.viewbind.pluginmgr.Proxy;
import com.snicesoft.viewbind.rule.IHolder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @param <H>
 * @param <D>
 * @author zhe
 */
@SuppressWarnings("rawtypes")
public class AVActivity<H extends IHolder, D> extends Activity implements IAv<H, D>, OnClickListener {
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
        _holder = newHolder();
        _data = newData();
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

    D newData() {
        Class dClass = $Gson$Types.getRawType(getType(1));
        if (dClass == Void.class)
            return null;
        try {
            return (D) dClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    H newHolder() {
        Class hClass = $Gson$Types.getRawType(getType(0));
        try {
            if (hClass == Void.class)
                return null;
            return (H) hClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
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
