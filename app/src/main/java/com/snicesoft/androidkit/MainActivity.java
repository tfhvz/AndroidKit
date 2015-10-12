package com.snicesoft.androidkit;

import android.os.Bundle;

import com.snicesoft.basekit.LogKit;
import com.snicesoft.framework.AKActivity;
import com.snicesoft.net.api.API;
import com.snicesoft.androidkit.android.R;

import com.snicesoft.viewbind.annotation.Layout;

@Layout(R.layout.activity_main)
public class MainActivity extends AKActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogKit.d(API.User.USER);
    }
}
