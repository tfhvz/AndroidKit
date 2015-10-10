package com.snicesoft.basekit.util;

import android.os.Handler;
import android.view.View;
import android.widget.ScrollView;

public class ViewTools {

	public static void scrollToBottom(final ScrollView scroll, final View inner) {
		Handler mHandler = new Handler();
		mHandler.post(new Runnable() {
			public void run() {
				if (scroll == null || inner == null) {
					return;
				}

				int offset = inner.getMeasuredHeight() - scroll.getHeight();
				if (offset < 0) {
					offset = 0;
				}
				scroll.smoothScrollTo(0, offset);
			}
		});
	}

	public static int getHeigth(View v) {
		if (v == null)
			return 0;
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		v.measure(w, h);
		return v.getMeasuredHeight();
	}

	public static int getWidth(View v) {
		if (v == null)
			return 0;
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		v.measure(w, h);
		return v.getMeasuredWidth();
	}
}
