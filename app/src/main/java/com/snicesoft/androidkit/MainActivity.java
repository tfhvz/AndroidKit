package com.snicesoft.androidkit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.snicesoft.basekit.LogKit;
import com.snicesoft.net.api.API;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.snicesoft.android.R.layout.activity_main);
        LogKit.d(API.User.USER);
    }
}
