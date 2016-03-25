package com.snicesoft.viewbind.bind;

import android.view.View;
import android.widget.Checkable;

import com.snicesoft.viewbind.annotation.DataBind;

/**
 * Created by zhuzhe on 16/3/25.
 */
public class BindCheck extends IBind {
    public BindCheck(DataBind dataBind) {
        super(dataBind);
    }

    @Override
    public void bindView(View view, Object value) {
        if (value instanceof Boolean) {
            Checkable checkable = (Checkable) view;
            checkable.setChecked(Boolean.getBoolean(value + ""));
        }
    }
}
