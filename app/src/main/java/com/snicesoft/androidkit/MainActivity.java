package com.snicesoft.androidkit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.snicesoft.androidkit.android.R;
import com.snicesoft.basekit.LogKit;
import com.snicesoft.framework.AKActivity;
import com.snicesoft.viewbind.annotation.Layout;
import com.snicesoft.viewbind.rule.IHolder;

@Layout(R.layout.activity_main)
public class MainActivity extends AKActivity<MainActivity.Holder, Void> {

    public class Holder extends IHolder {

        @Override
        public void initViewParams() {
            LogKit.d("===init");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btnPlugin:
                startActivity(new Intent(this, PluginActivity.class));
                break;
        }
    }
}
