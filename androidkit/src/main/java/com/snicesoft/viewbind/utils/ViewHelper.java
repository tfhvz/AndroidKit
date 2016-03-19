package com.snicesoft.viewbind.utils;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
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

    public ViewHelper setImageDrawable(int vId, Drawable drawable) {
        viewFinder.findViewById(vId, ImageView.class).setImageDrawable(drawable);
        return this;
    }

    public ViewHelper setVisible(int vId, int visible) {
        viewFinder.findViewById(vId).setVisibility(visible);
        return this;
    }

    public ViewHelper setBackgroundColor(int vId, int color) {
        viewFinder.findViewById(vId).setBackgroundColor(color);
        return this;
    }

    public ViewHelper setBackgroundRes(int vId, int backgroundRes) {
        viewFinder.findViewById(vId).setBackgroundResource(backgroundRes);
        return this;
    }

    public ViewHelper linkify(int vId) {
        Linkify.addLinks(viewFinder.findViewById(vId, TextView.class), Linkify.ALL);
        return this;
    }

    public ViewHelper setTypeface(int vId, Typeface typeface) {
        TextView view = viewFinder.findViewById(vId, TextView.class);
        view.setTypeface(typeface);
        view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        return this;
    }

    public ViewHelper setTypeface(Typeface typeface, int... viewIds) {
        for (int vId : viewIds) {
            TextView view = viewFinder.findViewById(vId, TextView.class);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    public ViewHelper setProgress(int vId, int progress) {
        viewFinder.findViewById(vId, ProgressBar.class).setProgress(progress);
        return this;
    }

    public ViewHelper setProgress(int vId, int progress, int max) {
        ProgressBar view = viewFinder.findViewById(vId, ProgressBar.class);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    public ViewHelper setMax(int vId, int max) {
        viewFinder.findViewById(vId, ProgressBar.class).setMax(max);
        return this;
    }

    public ViewHelper setRating(int vId, float rating) {
        viewFinder.findViewById(vId, RatingBar.class).setRating(rating);
        return this;
    }

    public ViewHelper setRating(int vId, float rating, int max) {
        RatingBar view = viewFinder.findViewById(vId, RatingBar.class);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    public ViewHelper setOnClickListener(int vId, View.OnClickListener listener) {
        viewFinder.findViewById(vId).setOnClickListener(listener);
        return this;
    }

    public ViewHelper setOnTouchListener(int vId, View.OnTouchListener listener) {
        viewFinder.findViewById(vId).setOnTouchListener(listener);
        return this;
    }

    public ViewHelper setOnLongClickListener(int vId, View.OnLongClickListener listener) {
        viewFinder.findViewById(vId).setOnLongClickListener(listener);
        return this;
    }

    public ViewHelper setOnItemClickListener(int vId, AdapterView.OnItemClickListener listener) {
        viewFinder.findViewById(vId, AdapterView.class).setOnItemClickListener(listener);
        return this;
    }

    public ViewHelper setOnItemLongClickListener(int vId, AdapterView.OnItemLongClickListener listener) {
        viewFinder.findViewById(vId, AdapterView.class).setOnItemLongClickListener(listener);
        return this;
    }

    public ViewHelper setOnItemSelectedClickListener(int vId, AdapterView.OnItemSelectedListener listener) {
        viewFinder.findViewById(vId, AdapterView.class).setOnItemSelectedListener(listener);
        return this;
    }

    public ViewHelper setTag(int vId, Object tag) {
        viewFinder.findViewById(vId).setTag(tag);
        return this;
    }

    public ViewHelper setTag(int vId, int key, Object tag) {
        viewFinder.findViewById(vId).setTag(key, tag);
        return this;
    }

    public ViewHelper setChecked(int vId, boolean checked) {
        Checkable view = (Checkable) viewFinder.findViewById(vId);
        view.setChecked(checked);
        return this;
    }

    public ViewHelper setAdapter(int vId, Adapter adapter) {
        viewFinder.findViewById(vId, AdapterView.class).setAdapter(adapter);
        return this;
    }

    public ViewHelper setAdapter(int vId, RecyclerView.Adapter adapter) {
        viewFinder.findViewById(vId, RecyclerView.class).setAdapter(adapter);
        return this;
    }
}
