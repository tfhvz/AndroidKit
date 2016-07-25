package com.snicesoft.framework;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.gson.internal.$Gson$Types;
import com.snicesoft.viewbind.AVKit;
import com.snicesoft.viewbind.ViewFinder;
import com.snicesoft.viewbind.base.IAv;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class AVFragment<HD, FA extends FragmentActivity> extends Fragment
        implements IAv, View.OnClickListener {
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

    public final FA fa() {
        return (FA) getActivity();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        finder = new ViewFinder(getView());
        try {
            _hd = newHD();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ViewFinder getFinder() {
        return finder;
    }

    protected View getRoot() {
        return finder.getParent();
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

    protected final Class<?> getThisClass() {
        Class<?> clazz = getClass();
        if (isProxy()) {
            clazz = getClass().getSuperclass();
        }
        return clazz;
    }

    public boolean isProxy() {
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v == null)
            return;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _hd = null;
        finder = null;
    }
}
