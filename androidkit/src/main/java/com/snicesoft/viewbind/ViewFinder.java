package com.snicesoft.viewbind;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhu zhe
 * @version V1.0 参考xUtils
 * @since 2015年4月15日 上午10:48:05
 */
public class ViewFinder {
    private View parent;
    private Context context;
    private Map<Integer, View> viewMap = new HashMap<Integer, View>();

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
        if (!viewMap.containsKey(vId))
            viewMap.put(vId, parent.findViewById(vId));
        return viewMap.get(vId);
    }
}
