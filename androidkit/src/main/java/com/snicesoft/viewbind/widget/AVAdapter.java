package com.snicesoft.viewbind.widget;

import android.content.Context;
import android.view.View;

import com.snicesoft.viewbind.annotation.Layout;
import com.snicesoft.viewbind.rule.IHolder;

/**
 * @author zhu zhe
 * @since 2015年4月15日 上午9:52:57
 * @version V1.0
 */
@SuppressWarnings("rawtypes")
public abstract class AVAdapter<H extends IHolder, D> extends BaseAdapter<H, D> {

	public AVAdapter(Context context) {
		super(context);
		Layout layout = getClass().getAnnotation(Layout.class);
		if (layout != null && layout.value() != 0) {
			this.resource = layout.value();
		}
	}

	public AVAdapter(Context context, int layoutRes) {
		super(context, layoutRes);
	}

	@Override
	View newView(int position) {
		return View.inflate(getContext(), resource, null);
	}

}
