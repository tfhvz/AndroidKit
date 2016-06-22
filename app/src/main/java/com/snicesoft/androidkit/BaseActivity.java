package com.snicesoft.androidkit;

import com.snicesoft.framework.AKActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

/**
 * Created by zhe on 16/6/22.
 */
@EActivity
public abstract class BaseActivity<D> extends AKActivity<D> {
    @Override
    public boolean isProxy() {
        return true;
    }

    @AfterViews
    protected void autoBind() {
        bindAll();
    }
}
