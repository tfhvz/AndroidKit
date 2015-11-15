package com.snicesoft.viewbind.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.internal.$Gson$Types;
import com.snicesoft.viewbind.AVKit;
import com.snicesoft.viewbind.ViewFinder;
import com.snicesoft.viewbind.rule.IHolder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@SuppressWarnings({"unchecked", "rawtypes"})
public class AvFragment<H extends IHolder, D, FA extends FragmentActivity> extends Fragment
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
        View root = inflater.inflate(LayoutUtils.getLayoutId(getClass()), null);
        _holder = newHolder();
        _data = newData();
        finder = new ViewFinder(root);
        AVKit.initHolder(_holder, finder);
        dataBindAll();
        return root;
    }

    D newData() {
        Class dClass = $Gson$Types.getRawType(getType(1));
        try {
            if (dClass == Void.class)
                return null;
            return (D) dClass.newInstance();
        } catch (java.lang.InstantiationException e) {
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
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
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
    public void onClick(View view) {

    }
}