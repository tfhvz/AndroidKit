package com.snicesoft.androidkit;

import android.app.Application;

import com.snicesoft.basekit.LogKit;
import com.snicesoft.kits.bitmap.GlideBitmapKit;
import com.snicesoft.kits.http.OkhttpKit;

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
        GlideBitmapKit.getInstance(getApplicationContext());
        //bitmap组件之VolleyImageLoader
        //VolleyImageLoader.init(getApplicationContext());
        //bitmap组件之universal-image-loader
        //UILBitmapKit.getInstance(getApplicationContext());

        //======初始化Http组件======

        //1:http组件之volley
        OkhttpKit.getInstance();
        OkhttpKit.getInstance().setTimeout(5000);
        //2:http组件之Okhttp
        //OkhttpKit.getInstance();

        //======初始化API======
//        API.getInstance().initTest(Config.Scheme.HTTPS, "192.168.0.122", 0, "userinfo/");
//        API.getInstance().initProduct(Config.Scheme.HTTPS, "89.23.78.345", 0, "userinfo/");
//        API.getInstance().init(ConfigFactory.Mode.TEST);
    }
}
