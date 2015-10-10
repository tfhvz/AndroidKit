package com.snicesoft.viewbind.base;

import com.snicesoft.viewbind.annotation.Layout;

class LayoutUtils {
	public static int getLayoutId(Class<?> clazz) {
		int layoutId = 0;
		Layout layout = clazz.getAnnotation(Layout.class);
		if (layout != null && layout.value() != 0) {
			layoutId = layout.value();
		} else {
			layoutId = 0;
			System.err.println("@Layout not find.");
		}
		return layoutId;
	}
}
