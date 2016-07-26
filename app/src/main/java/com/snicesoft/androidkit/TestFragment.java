package com.snicesoft.androidkit;

import com.snicesoft.androidkit.android.R;
import com.snicesoft.framework.AVFragment;

/**
 * Created by zhuzhe on 16/7/26.
 */
//@Layout(name = "fragment_test")
public class TestFragment extends AVFragment<Void, MainActivity> {
    @Override
    public int layout() {
        return R.layout.fragment_test;
    }
}
