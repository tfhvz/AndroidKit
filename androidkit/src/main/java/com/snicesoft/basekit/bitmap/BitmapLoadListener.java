package com.snicesoft.basekit.bitmap;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

public interface BitmapLoadListener<V extends View> {
	public void onLoadCompleted(V container, Bitmap bitmap);
	public void onLoadFailed(V container, Drawable drawable);
}
