package com.snicesoft.viewbind.bind;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.snicesoft.viewbind.AVKit;
import com.snicesoft.viewbind.annotation.DataBind;

/**
 * Created by zhuzhe on 16/3/25.
 */
public class BindImg extends IBind {
    public BindImg(DataBind dataBind) {
        super(dataBind);
    }

    public AVKit.LoadImg loadImg;

    public void setLoadImg(AVKit.LoadImg loadImg) {
        this.loadImg = loadImg;
    }

    @Override
    public void bindView(View view, Object value) {
        String p = getDataBind().prefix();
        String s = getDataBind().suffix();
        int loading = getDataBind().loadingResId();
        String loadName = getDataBind().loadingResName();
        int fail = getDataBind().failResId();
        String failName = getDataBind().failResName();
        if (value instanceof Integer) {
            int resId = Integer.parseInt(value.toString());
            if (view instanceof ImageView) {
                ((ImageView) view).setImageResource(resId);
            } else {
                view.setBackgroundResource(resId);
            }
        } else if (value instanceof Drawable) {
            if (view instanceof ImageView) {
                ((ImageView) view).setImageDrawable((Drawable) value);
            } else {
                view.setBackgroundDrawable((Drawable) value);
            }
        } else if (value instanceof String) {
            if (loadImg != null) {
                Context context = view.getContext();
                if (loading == 0) {
                    loading = context.getResources().getIdentifier(loadName, "drawable", context.getPackageName()) | context.getResources().getIdentifier(loadName, "mipmap", context.getPackageName());
                }
                if (fail == 0) {
                    fail = context.getResources().getIdentifier(failName, "drawable", context.getPackageName()) | context.getResources().getIdentifier(failName, "mipmap", context.getPackageName());
                }
                loadImg.loadImg(view, loading, fail, p + value.toString() + s);
            }
        }
    }
}
