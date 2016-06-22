package com.snicesoft.androidkit;

import android.support.v4.app.FragmentActivity;

import com.snicesoft.framework.AVFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

/**
 * Created by zhe on 16/6/22.
 */
@EFragment
public abstract class BaseFragment<D, A extends FragmentActivity> extends AVFragment<D, A> {
    @Override
    public boolean isProxy() {
        return true;
    }

    @AfterViews
    void load() {
        bindAll();
    }
}
