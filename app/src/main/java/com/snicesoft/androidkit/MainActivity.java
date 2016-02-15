package com.snicesoft.androidkit;

import android.view.View;

import com.snicesoft.androidkit.android.R;
import com.snicesoft.framework.AKActivity;
import com.snicesoft.net.controller.TestController;
import com.snicesoft.viewbind.annotation.AutoController;
import com.snicesoft.viewbind.annotation.Layout;
import com.snicesoft.viewbind.rule.IHolder;

@Layout(R.layout.activity_main)
public class MainActivity extends AKActivity<MainActivity.Holder, Void> {

    public class Holder extends IHolder {
        @Override
        public void initViewParams() {

        }
    }
    @AutoController
    TestController testController;

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btnPlugin:
//                HttpRequest request = new HttpRequest("http://192.168.10.161:8080/web-weiding/resource");
//                request.addFile("file", new File("/storage/sdcard0/DCIM/Camera/IMG_20151110_164815.jpg"));
//                HttpKit.getInstance().postFile(request, new HttpCallBack<Result<Resource>>() {
//
//                    @Override
//                    public void onSuccess(Result<Resource> resourceResult) {
//                        CommonUtils.showToast(getBaseContext(), "成功");
//                    }
//
//                    @Override
//                    public void onLoading(long count, long current) {
//                        LogKit.d("==" + current + "/" + count);
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
