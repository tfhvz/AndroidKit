package com.snicesoft.androidkit;

import android.app.Application;

import com.android.volley.kit.VolleyHttpKit;
import com.android.volley.kit.VolleyImageLoader;
import com.lidroid.xutils.XUtilsBitmapKit;
import com.snicesoft.androidkit.otherkit.bitmap.UILBitmapKit;
import com.snicesoft.androidkit.otherkit.http.OkhttpKit;

/**
 * Created by zhuzhe on 15/10/10.
 */
public class KitApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //======初始化Bitmap组件======

        //bitmap组件之xutils_bitmap
        XUtilsBitmapKit.getInstance(getApplicationContext());
        //bitmap组件之VolleyImageLoader
        //VolleyImageLoader.init(getApplicationContext());
        //bitmap组件之universal-image-loader
        //UILBitmapKit.getInstance(getApplicationContext());

        //======初始化Http组件======

        //1:http组件之volley
        VolleyHttpKit.getInstance(getApplicationContext());
        //2:http组件之Okhttp
        //OkhttpKit.getInstance();
    }
}
