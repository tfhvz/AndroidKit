package com.snicesoft.viewbind.bind;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.snicesoft.viewbind.annotation.DataBind;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhuzhe on 16/3/25.
 */
public class BindText extends IBind {
    private SimpleDateFormat dateFormat = new SimpleDateFormat();

    public BindText(DataBind dataBind) {
        super(dataBind);
    }

    @Override
    public void bindView(View view, Object value) {
        String p = getDataBind().prefix();
        String s = getDataBind().suffix();
        String pattern = getDataBind().pattern();
        TextView tv = (TextView) view;
        if (TextUtils.isEmpty(pattern)) {
            if (value instanceof Integer) {
                tv.setText(p + view.getContext().getString(Integer.parseInt(value.toString())) + s);
            } else {
                tv.setText(p + value + s);
            }
        } else {
            dateFormat.applyPattern(pattern);
            if (value instanceof Long) {
                tv.setText(p + dateFormat.format(new Date(Long.parseLong(value.toString()))) + s);
            } else if (value instanceof Date) {
                tv.setText(p + dateFormat.format((Date) value) + s);
            } else {
                if (value instanceof String) {
                    try {
                        Long time = Long.parseLong(value.toString());
                        tv.setText(p + dateFormat.format(time) + s);
                    } catch (Exception e) {
                        tv.setText("ERROR-@DataBind");
                    }
                } else {
                    tv.setText("ERROR-@DataBind");
                }
            }
        }
    }
}
