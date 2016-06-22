package com.snicesoft.androidkit;

import com.snicesoft.android.R;
import com.snicesoft.viewbind.annotation.DataBind;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

/**
 * Created by zhe on 16/6/22.
 */
@EFragment(R.layout.fragment_test)
public class TestFragment extends BaseFragment<TestFragment.Data, MainActivity> {
    public class Data {
        @DataBind(name = "textView")
        String test = "test";
    }

    @Override
    public boolean isProxy() {
        return true;
    }

    @AfterViews
    void load() {
        bindAll();
    }
}
