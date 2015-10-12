package com.android.volley.kit;

import android.content.Context;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by zhuzhe on 15/9/25.
 */
public class VolleyImageLoader {
    RequestQueue mQueue;
    ImageLoader imageLoader;

    private VolleyImageLoader(Context context) {
        mQueue = Volley.newRequestQueue(context);
        imageLoader = new ImageLoader(mQueue, new VolleyBitmapCache());

    }

    private static VolleyImageLoader loader;

    public static void init(Context context) {
        if (loader == null) {
            loader = new VolleyImageLoader(context);
        }
    }

    public static VolleyImageLoader getInstance() {
        return loader;
    }

    public void displayImageView(ImageView view, String url, int loading, int faild) {
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(view, loading, faild);
        imageLoader.get(url, listener);
        //指定图片允许的最大宽度和高度
        //imageLoader.get("http://developer.android.com/images/home/aw_dac.png",listener, 200, 200);
    }
}
