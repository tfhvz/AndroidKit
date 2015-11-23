package com.snicesoft.androidkit;

import android.os.Bundle;
import android.os.Environment;

import com.snicesoft.androidkit.android.R;
import com.snicesoft.basekit.LogKit;
import com.snicesoft.framework.AKActivity;
import com.snicesoft.net.api.API;
import com.snicesoft.pluginkit.PluginManager;
import com.snicesoft.pluginkit.environment.PlugInfo;
import com.snicesoft.viewbind.annotation.Layout;
import com.snicesoft.viewbind.rule.IHolder;

import java.io.File;
import java.util.Collection;

@Layout(R.layout.activity_main)
public class MainActivity extends AKActivity<MainActivity.Holder, Void> {
    private static final String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();

    public class Holder extends IHolder {

        @Override
        public void initViewParams() {
            LogKit.d("===init");
            try {
                Collection<PlugInfo> plugInfos = PluginManager.getSingleton().loadPlugin(new File(sdcard + "/Plugin/myplug.apk"));
                LogKit.d(plugInfos.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogKit.d(API.User.USER);
    }
}
