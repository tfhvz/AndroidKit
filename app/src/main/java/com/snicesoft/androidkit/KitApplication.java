package com.snicesoft.androidkit;

import android.app.Application;

import com.android.volley.kit.VolleyHttpKit;
import com.android.volley.kit.VolleyImageLoader;
import com.lidroid.xutils.XUtilsBitmapKit;
import com.snicesoft.androidkit.otherkit.bitmap.UILBitmapKit;
import com.snicesoft.androidkit.otherkit.http.OkhttpKit;
import com.snicesoft.basekit.LogKit;
import com.snicesoft.basekit.net.api.APIConfig;
import com.snicesoft.basekit.net.api.Config;
import com.snicesoft.basekit.net.api.ConfigFactory;
import com.snicesoft.net.api.API;

/**
 * Created by zhuzhe on 15/10/10.
 */
public class KitApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LogKit.customTagPrefix = "Kit_";
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

        //======初始化API======
        APIConfig.initTest(Config.Scheme.HTTPS, "192.168.0.122", 0, "userinfo/");
        APIConfig.initProduct(Config.Scheme.HTTPS, "89.23.78.345", 0, "userinfo/");
        API.init(ConfigFactory.Mode.TEST);
    }
}
