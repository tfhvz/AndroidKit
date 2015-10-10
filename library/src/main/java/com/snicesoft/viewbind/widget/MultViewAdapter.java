package com.snicesoft.viewbind.widget;

import android.content.Context;
import android.view.View;

import com.snicesoft.viewbind.rule.IHolder;

@SuppressWarnings("rawtypes")
public abstract class MultViewAdapter<H extends IHolder, D> extends BaseAdapter<H, D> {
	public MultViewAdapter(Context context) {
		super(context);
	}

	public MultViewAdapter(Context context, int layoutRes) {
		super(context, layoutRes);
	}

	public abstract int getItemViewType(int position);

	public abstract int getViewTypeCount();

	@Override
	final View newView(int position) {
		return View.inflate(getContext(), getItemViewType(position), null);
	}
}