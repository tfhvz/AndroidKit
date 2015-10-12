package com.snicesoft.viewbind.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.snicesoft.viewbind.AVKit;
import com.snicesoft.viewbind.ViewFinder;
import com.snicesoft.viewbind.pluginmgr.Proxy;
import com.snicesoft.viewbind.rule.IHolder;

/**
 * FragmentActivity基类
 * 
 * @author zhe
 *
 * @param <H>
 * @param <D>
 */
@SuppressWarnings("rawtypes")
@SuppressLint("NewApi")
public class AvAppCompatActivity<H extends IHolder, D> extends FragmentActivity implements IAv<H, D>, OnClickListener {
	private ViewFinder finder;
	protected D _data;
	protected H _holder;

	@Override
	public final D getData() {
		return _data;
	}

	@Override
	public final H getHolder() {
		return _holder;
	}

	@Override
	public final void dataBindAll() {
		AVKit.dataBind(_data, finder);
	}

	@Override
	public final void dataBindTo(String fieldName) {
		AVKit.dataBindTo(_data, finder, fieldName);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(LayoutUtils.getLayoutId(getThisClass()));
		_holder = newHolder();
		_data = newData();
		finder = new ViewFinder(this);
		AVKit.initHolder(_holder, finder);
		dataBindAll();
	}

	public Class<?> getThisClass() {
		Class<?> clazz = getClass();
		if (Proxy.PROXY_ACTIVITY.equals(clazz.getName())) {
			clazz = getClass().getSuperclass();
		}
		return clazz;
	}

	@Override
	public D newData() {
		return null;
	}

	@Override
	public H newHolder() {
		return null;
	}

	@Override
	public void onClick(View v) {
		if (v == null)
			return;
	}
}