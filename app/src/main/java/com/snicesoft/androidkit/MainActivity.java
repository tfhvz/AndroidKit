package com.snicesoft.androidkit;

import android.view.View;
import android.widget.Button;

import com.snicesoft.androidkit.android.R;
import com.snicesoft.basekit.LogKit;
import com.snicesoft.framework.AKActivity;
import com.snicesoft.net.controller.TestController;
import com.snicesoft.viewbind.annotation.AutoController;
import com.snicesoft.viewbind.annotation.DataBind;
import com.snicesoft.viewbind.annotation.Id;
import com.snicesoft.viewbind.annotation.Layout;
import com.snicesoft.viewbind.rule.IHolder;

@Layout(name = "activity_main")
public class MainActivity extends AKActivity<MainActivity.Holder, MainActivity.Data> {

    public class Holder extends IHolder {
        @Id(name = "btnPlugin")
        Button btnPlugin;
        @Override
        public void initViewParams() {
            LogKit.d("test:"+btnPlugin.toString());
        }
    }

    public class Data{
        @DataBind(name = "btnPlugin")
        String name = "zenme是大写";
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
