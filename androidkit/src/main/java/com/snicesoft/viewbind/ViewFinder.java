package com.snicesoft.viewbind;

import android.app.Activity;
import android.view.View;

/**
 * @author zhu zhe
 * @version V1.0 参考xUtils
 * @since 2015年4月15日 上午10:48:05
 */
public class ViewFinder {
    private View parent;

    public ViewFinder(View view) {
        this.parent = view;
    }

    public ViewFinder(Activity activity) {
        this.parent = activity.getWindow().getDecorView();
    }

    public View getParent() {
        return parent;
    }

    public View findViewById(int vId) {
        if (vId == 0)
            return null;
        return parent.findViewById(vId);
    }
}
