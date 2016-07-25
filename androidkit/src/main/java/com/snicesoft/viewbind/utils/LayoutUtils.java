package com.snicesoft.viewbind.utils;

import android.content.Context;

import com.snicesoft.viewbind.annotation.Layout;

public class LayoutUtils {
    public static int getLayoutId(Context context, Class<?> clazz) {
        int layoutId = 0;
        Layout layout = clazz.getAnnotation(Layout.class);
        if (layout != null) {
            layoutId = layout.value();
            if (layoutId == 0) {
                layoutId = context.getResources().getIdentifier(layout.name(), "layout", context.getPackageName());
            }
        } else {
            if (layoutId == 0) {
                System.err.println("@Layout not find, or not override layout().");
            }
        }
        return layoutId;
    }
}
