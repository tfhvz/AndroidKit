package com.snicesoft.viewbind.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.snicesoft.viewbind.AVKit;
import com.snicesoft.viewbind.ViewFinder;
import com.snicesoft.viewbind.rule.IHolder;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class AvFragment<H extends IHolder, D, FA extends FragmentActivity> extends Fragment
		implements IAv<H, D>, View.OnClickListener {
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

	public final FA fa() {
		return (FA) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(LayoutUtils.getLayoutId(getClass()), null);
		_holder = newHolder();
		_data = newData();
		finder = new ViewFinder(root);
		AVKit.initHolder(_holder, finder);
		dataBindAll();
		return root;
	}

	@Override
	public D newData() {
		return null;
	}

	@Override
	public H newHolder() {
		return null;
	}

    public void onRefresh(){

    }
	@Override
	public void onClick(View view) {
		
	}
}