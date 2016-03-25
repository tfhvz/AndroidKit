package com.snicesoft.framework;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.internal.$Gson$Types;
import com.snicesoft.viewbind.AVKit;
import com.snicesoft.viewbind.ViewFinder;
import com.snicesoft.viewbind.base.IAv;
import com.snicesoft.viewbind.utils.LayoutUtils;

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

    @Override
    public int layout() {
        return 0;
    }

    public final FA fa() {
        return (FA) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layout = layout();
        if (layout == 0)
            layout = LayoutUtils.getLayoutId(fa(), getClass());
        View root = inflater.inflate(layout, null);
        finder = new ViewFinder(root);
        try {
            _hd = newHD();
        } catch (Exception e) {
            e.printStackTrace();
        }
        bindAll();
        onLoaded();
        loadNetData();
        return root;
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
        if (hClass.getName().contains(getClass().getName() + "$")) {
            if (Modifier.isStatic(hClass.getModifiers()))
                return (HD) hClass.newInstance();
            return (HD) hClass.getConstructors()[0].newInstance(this);
        }
        return (HD) hClass.newInstance();
    }

    Type getType(int index) {
        Type superclass = getClass().getGenericSuperclass();
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
    public void onDestroy() {
        super.onDestroy();
        _hd = null;
        finder = null;
    }
}
