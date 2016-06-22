package com.snicesoft.androidkit;

import android.view.View;

import com.snicesoft.android.R;
import com.snicesoft.viewbind.annotation.DataBind;
import com.snicesoft.viewbind.bind.DataType;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity<MainActivity.Data> {
    public class Data {
        @DataBind(name = "btnPlugin")
        String name = "zenme是大写";
        @DataBind(dataType = DataType.IMG, name = "imageView",loadingResName = "ic_launcher", failResName = "ic_launcher")
        String img = "http://h.hiphotos.baidu.com/zhidao/pic/item/eac4b74543a9822628850ccc8c82b9014b90eb91.jpg";
        @DataBind(name = "ratingBar", dataType = DataType.RATING)
        float rating = 3;
        @DataBind(name = "seekBar", dataType = DataType.PROGRESS)
        int progress = 30;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btnPlugin:
                _hd.name = "dsfdsf";
                bindTo(R.id.btnPlugin);
                openFragment(R.id.content, new TestFragment_());
                break;
        }
    }
}
