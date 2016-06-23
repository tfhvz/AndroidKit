package com.snicesoft.kits.bitmap;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.snicesoft.basekit.BitmapKit;
import com.snicesoft.basekit.bitmap.BitmapConfig;
import com.snicesoft.basekit.bitmap.BitmapLoadListener;

/**
 * Created by zhe on 16/6/22.
 */
public class GlideBitmapKit extends BitmapKit {
    public synchronized static BitmapKit getInstance(Context ctx) {
        if (instance == null) {
            instance = new GlideBitmapKit(ctx);
        }
        return instance;
    }

    private Context context;

    private GlideBitmapKit(Context context) {
        this.context = context;
    }

    @Override
    public <V extends View> void display(V v, String url) {
        if (v instanceof ImageView)
            Glide.with(v.getContext())
                    .load(url)
                    .centerCrop()
                    .crossFade()
                    .into((ImageView) v);
    }

    @Override
    public <V extends View> void display(V v, String url, BitmapConfig config) {
        if (v instanceof ImageView)
            Glide.with(v.getContext())
                    .load(url)
                    .placeholder(config.getLoadingRes())
                    .error(config.getFailRes())
                    .centerCrop()
                    .crossFade()
                    .into((ImageView) v);
    }

    @Override
    public <V extends View> void display(V v, String url, BitmapLoadListener<V> listener) {
        if (v instanceof ImageView)
            Glide.with(v.getContext())
                    .load(url)
                    .centerCrop()
                    .crossFade()
                    .into((ImageView) v);
    }

    @Override
    public void clearMemoryCache() {
        Glide.get(context).clearMemory();
    }

    @Override
    public void clearDiskCache() {
        Glide.get(context).clearDiskCache();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void cancel() {

    }
}
