package com.android.volley.kit;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by zhuzhe on 15/9/25.
 */
public class VolleyBitmapCache implements ImageLoader.ImageCache {

    private LruCache<String, Bitmap> cache;

    public VolleyBitmapCache() {
        cache = new LruCache<String, Bitmap>(8 * 1024 * 1024) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
    }

    @Override
    public Bitmap getBitmap(String url) {
        return cache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        cache.put(url, bitmap);
    }
}
