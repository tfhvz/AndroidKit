package com.snicesoft.viewbind.bind;

import android.view.View;

import com.snicesoft.viewbind.annotation.DataBind;

/**
 * Created by zhuzhe on 16/3/25.
 */
public abstract class IBind {
    private DataBind dataBind;

    public DataBind getDataBind() {
        return dataBind;
    }

    public IBind(DataBind dataBind) {
        this.dataBind = dataBind;
    }

    public abstract void bindView(View view, Object value);
}
