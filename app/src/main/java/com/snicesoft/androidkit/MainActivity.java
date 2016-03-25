package com.snicesoft.androidkit;

import android.view.View;
import android.widget.Button;

import com.snicesoft.androidkit.android.R;
import com.snicesoft.basekit.LogKit;
import com.snicesoft.framework.AKActivity;
import com.snicesoft.net.controller.TestController;
import com.snicesoft.viewbind.annotation.Context;
import com.snicesoft.viewbind.annotation.DataBind;
import com.snicesoft.viewbind.annotation.Id;
import com.snicesoft.viewbind.annotation.Layout;
import com.snicesoft.viewbind.bind.DataType;

@Layout(name = "activity_main")
public class MainActivity extends AKActivity<MainActivity.Data> {

    @Override
    public void onLoaded() {
        LogKit.d("==onLoaded");
    }

    @Override
    public void loadNetData() {
        LogKit.d("==NetData");
    }

    public class Data {
        @Id(name = "btnPlugin")
        Button btnPlugin;
        @DataBind(name = "btnPlugin")
        String name = "zenme是大写";
        @DataBind(dataType = DataType.IMG, name = "imageView", failResName = "ic_launcher")
        String img = "http://h.hiphotos.baidu.com/zhidao/pic/item/eac4b74543a9822628850ccc8c82b9014b90eb91.jpg";
        @DataBind(name = "ratingBar", dataType = DataType.RATING)
        float rating = 3;
        @DataBind(name = "seekBar", dataType = DataType.PROGRESS)
        int progress = 30;
    }

    @Context
    TestController testController;

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btnPlugin:
                _hd.name = "dsfdsf";
                bindTo(R.id.btnPlugin);
                LogKit.d("==hd:" + _hd.btnPlugin);
//                HttpRequest request = new HttpRequest("http://192.168.10.161:8080/web-weiding/charityRecord/love");
////                request.addFile("file", new File("/storage/sdcard0/DCIM/Camera/IMG_20151110_164815.jpg"));
//                HttpKit.getInstance().get(request, new HttpCallBack<Result<String>>() {
//
//                    @Override
//                    public void onSuccess(Result<String> resourceResult) {
//                        CommonUtils.showToast(getBaseContext(), "成功");
//                    }
//
//                    @Override
//                    public void onLoading(long count, long current) {
////                        LogKit.d("==" + current + "/" + count);
//                    }
//
//                    @Override
//                    public void onError(HttpError error) {
//                        CommonUtils.showToast(getBaseContext(), "失败");
//                    }
//                });
                break;
        }
    }
}
