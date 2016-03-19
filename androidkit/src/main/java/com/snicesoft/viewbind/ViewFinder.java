package com.snicesoft.viewbind;

import android.app.Activity;
import android.content.Context;
import android.util.SparseArray;
import android.view.View;

import com.snicesoft.viewbind.utils.ViewHelper;

/**
 * @author zhu zhe
 * @version V1.0 参考xUtils
 * @since 2015年4月15日 上午10:48:05
 */
public class ViewFinder {
    private View parent;
    private Context context;
    private ViewHelper viewHelper = new ViewHelper(this);
    private SparseArray<View> views = new SparseArray<View>();

    public ViewFinder(View view) {
        this.parent = view;
        this.context = view.getContext();
    }

    public ViewFinder(Activity activity) {
        this.parent = activity.getWindow().getDecorView();
        this.context = activity;
    }

    public View getParent() {
        return parent;
    }

    public Context getContext() {
        return context;
    }

    public View findViewById(int vId) {
        if (vId == 0)
            return null;
        if (views.get(vId) == null)
            views.put(vId, parent.findViewById(vId));
        return views.get(vId);
    }

    public <V extends View> V findViewById(int vId, Class<V> clazz) {
        return (V) findViewById(vId);
    }

    public ViewHelper getViewHelper() {
        return viewHelper;
    }
}
