package com.snicesoft.framework;

import android.os.Bundle;

import com.snicesoft.viewbind.base.AvActivity;
import com.snicesoft.viewbind.rule.IHolder;

public class BaseActivity<H extends IHolder, D> extends AvActivity<H, D> {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityMgr.addActivity(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityMgr.removeActivity(this);
	}
}
