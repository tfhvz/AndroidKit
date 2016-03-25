package com.snicesoft.viewbind.bind;

import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.snicesoft.viewbind.annotation.DataBind;

/**
 * Created by zhuzhe on 16/3/25.
 */
public class BindHtml extends IBind {
    public BindHtml(DataBind dataBind) {
        super(dataBind);
    }

    @Override
    public void bindView(View view, Object value) {
        String p = getDataBind().prefix();
        String s = getDataBind().suffix();
        TextView html = (TextView) view;
        html.setText(Html.fromHtml(p + value + s));
    }
}
