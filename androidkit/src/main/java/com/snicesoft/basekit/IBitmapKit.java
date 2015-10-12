package com.snicesoft.basekit;

import android.view.View;

import com.snicesoft.basekit.bitmap.BitmapConfig;
import com.snicesoft.basekit.bitmap.BitmapLoadListener;

interface IBitmapKit {
	public <V extends View> void display(V v, String url);

	public <V extends View> void display(V v, String url, BitmapConfig config);

	public <V extends View> void display(V v, String url, final BitmapLoadListener<V> listener);

	public void clearMemoryCache();

	public void clearDiskCache();

	public void pause();

	public void resume();

	public void cancel();
}
