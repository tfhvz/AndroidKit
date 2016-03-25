package com.snicesoft.viewbind.bind;

import android.view.View;
import android.widget.ProgressBar;

import com.snicesoft.viewbind.annotation.DataBind;

/**
 * Created by zhuzhe on 16/3/25.
 */
public class BindProgress extends IBind {
    public BindProgress(DataBind dataBind) {
        super(dataBind);
    }

    @Override
    public void bindView(View view, Object value) {
        ((ProgressBar) view).setProgress(Integer.parseInt(value.toString()));
    }
}
