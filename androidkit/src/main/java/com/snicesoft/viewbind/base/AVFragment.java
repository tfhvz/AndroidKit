package com.snicesoft.viewbind.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.internal.$Gson$Types;
import com.snicesoft.viewbind.AVKit;
import com.snicesoft.viewbind.LayoutUtils;
import com.snicesoft.viewbind.ViewFinder;
import com.snicesoft.viewbind.rule.IHolder;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@SuppressWarnings({"unchecked", "rawtypes"})
public class AVFragment<H extends IHolder, D, FA extends FragmentActivity> extends Fragment
        implements IAv<H, D>, View.OnClickListener {
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

    public final FA fa() {
        return (FA) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(LayoutUtils.getLayoutId(fa(), getClass()), null);
        try {
            _holder = newHolder();
            _data = newData();
            AutoControllerUtils.loadController(fa(), getClass(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finder = new ViewFinder(root);
        AVKit.initHolder(_holder, finder);
        dataBindAll();
        return root;
    }

    D newData() throws Exception {
        Class dClass = $Gson$Types.getRawType(getType(1));
        if (dClass == Void.class)
            return null;
        if (dClass.getName().contains(getClass().getName() + "$")) {
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
        if (hClass.getName().contains(getClass().getName() + "$")) {
            if (Modifier.isStatic(hClass.getModifiers()))
                return (H) hClass.newInstance();
            return (H) hClass.getConstructors()[0].newInstance(this);
        }
        return (H) hClass.newInstance();
    }

    Type getType(int index) {
        Type superclass = getClass().getGenericSuperclass();
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
