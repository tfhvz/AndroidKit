package com.snicesoft.viewbind.bind;

import android.view.View;
import android.widget.RatingBar;

import com.snicesoft.viewbind.annotation.DataBind;

/**
 * Created by zhuzhe on 16/3/25.
 */
public class BindRating extends IBind {
    public BindRating(DataBind dataBind) {
        super(dataBind);
    }

    @Override
    public void bindView(View view, Object value) {
        ((RatingBar) view).setRating(Float.parseFloat(value.toString()));
    }
}
