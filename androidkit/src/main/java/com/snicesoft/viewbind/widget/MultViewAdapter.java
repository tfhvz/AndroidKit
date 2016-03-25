package com.snicesoft.viewbind.widget;

import android.content.Context;
import android.view.View;

@SuppressWarnings("rawtypes")
public abstract class MultViewAdapter<D> extends BaseAdapter<D> {
    public MultViewAdapter(Context context) {
        super(context);
    }

    public abstract int getItemViewType(int position);

    @Override
    View newView(int position) {
        return View.inflate(getContext(), getItemViewType(position), null);
    }
}