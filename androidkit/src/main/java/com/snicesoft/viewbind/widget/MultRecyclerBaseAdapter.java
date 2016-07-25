package com.snicesoft.viewbind.widget;

import android.content.Context;
import android.view.View;

import com.snicesoft.viewbind.rule.RecyclerHolder;

/**
 * Created by zhuzhe on 16/7/13.
 */
public abstract class MultRecyclerBaseAdapter<VH extends RecyclerHolder, D> extends RecyclerBaseAdapter<VH, D> {
    public abstract int getItemViewLayout(int viewType);

    @Override
    View newView(Context context, int viewType) {
        return View.inflate(context, getItemViewLayout(viewType), null);
    }
}
