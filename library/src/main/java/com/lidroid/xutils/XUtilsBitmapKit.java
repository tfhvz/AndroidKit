package com.lidroid.xutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import com.snicesoft.basekit.BitmapKit;
import com.snicesoft.basekit.bitmap.BitmapConfig;
import com.snicesoft.basekit.bitmap.BitmapLoadListener;

/**
 * xUtils 图片加载
 *
 * @author zhuzhe
 */
@SuppressWarnings("deprecation")
public class XUtilsBitmapKit extends BitmapKit {
    public static BitmapUtils bitmapUtils;

    private XUtilsBitmapKit(Context ctx) {
        bitmapUtils = new BitmapUtils(ctx);
    }

    public BitmapUtils getBitmapUtils() {
        return bitmapUtils;
    }

    public synchronized static BitmapKit getInstance(Context ctx) {
        if (instance == null) {
            instance = new XUtilsBitmapKit(ctx);
        }
        return instance;
    }

    @Override
    public <V extends View> void display(V v, String url) {
        bitmapUtils.display(v, url);
    }

    @Override
    public <V extends View> void display(V v, String url, BitmapConfig config) {
        BitmapDisplayConfig dc = new BitmapDisplayConfig();
        if (config.getFailRes() != 0)
            dc.setLoadFailedDrawable(v.getContext().getResources().getDrawable(config.getFailRes()));
        if (config.getLoadingRes() != 0)
            dc.setLoadingDrawable(v.getContext().getResources().getDrawable(config.getLoadingRes()));
        bitmapUtils.display(v, url, dc);
    }

    @Override
    public <V extends View> void display(V v, String url, final BitmapLoadListener<V> listener) {
        bitmapUtils.display(v, url, new DefaultBitmapLoadCallBack<V>() {

            @Override
            public void onLoadCompleted(V container, String arg1, Bitmap bitmap, BitmapDisplayConfig arg3,
                                        BitmapLoadFrom arg4) {
                if (listener != null) {
                    listener.onLoadCompleted(container, bitmap);
                }
            }

            @Override
            public void onLoadFailed(V container, String arg1, Drawable drawable) {
                if (listener != null) {
                    listener.onLoadFailed(container, drawable);
                }
            }

        });
    }

    @Override
    public void clearMemoryCache() {
        bitmapUtils.clearMemoryCache();
    }

    @Override
    public void clearDiskCache() {
        bitmapUtils.clearDiskCache();
    }

    @Override
    public void pause() {
        bitmapUtils.pause();
    }

    @Override
    public void resume() {
        bitmapUtils.resume();
    }

    @Override
    public void cancel() {
        bitmapUtils.cancel();
    }

}
