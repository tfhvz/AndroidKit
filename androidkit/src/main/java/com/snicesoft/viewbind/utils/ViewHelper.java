package com.snicesoft.viewbind.utils;

import android.widget.ImageView;
import android.widget.TextView;

import com.snicesoft.viewbind.ViewFinder;

/**
 * Created by zhe on 16/3/19.
 */
public class ViewHelper {
    private ViewFinder viewFinder;

    public ViewHelper(ViewFinder viewFinder) {
        this.viewFinder = viewFinder;
    }

    public ViewHelper setText(int vId, String value) {
        viewFinder.findViewById(vId, TextView.class).setText(value);
        return this;
    }

    public ViewHelper setText(int vId, int resId) {
        viewFinder.findViewById(vId, TextView.class).setText(resId);
        return this;
    }

    public ViewHelper setTextColor(int vId, int textColor) {
        viewFinder.findViewById(vId, TextView.class).setTextColor(textColor);
        return this;
    }

    public ViewHelper setImageResource(int vId, int resId) {
        viewFinder.findViewById(vId, ImageView.class).setImageResource(resId);
        return this;
    }

    public ViewHelper setVisible(int vId, int visible) {
        viewFinder.findViewById(vId).setVisibility(visible);
        return this;
    }
}
