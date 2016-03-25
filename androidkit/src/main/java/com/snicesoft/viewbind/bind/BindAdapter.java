package com.snicesoft.viewbind.bind;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

import com.snicesoft.viewbind.annotation.DataBind;

/**
 * Created by zhuzhe on 16/3/25.
 */
public class BindAdapter extends IBind {
    public BindAdapter(DataBind dataBind) {
        super(dataBind);
    }

    @Override
    public void bindView(View view, Object value) {
        try {
            if (value instanceof Adapter && view instanceof AdapterView) {
                ((AdapterView<Adapter>) view).setAdapter((Adapter) value);
            } else if (value instanceof PagerAdapter && view instanceof ViewPager) {
                ((ViewPager) view).setAdapter((PagerAdapter) value);
            } else if (value instanceof RecyclerView.Adapter && view instanceof RecyclerView) {
                ((RecyclerView) view).setAdapter((RecyclerView.Adapter) value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
