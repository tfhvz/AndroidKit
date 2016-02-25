package com.snicesoft.viewbind.widget;

import android.content.Context;
import android.view.View;

import com.snicesoft.viewbind.LayoutUtils;
import com.snicesoft.viewbind.rule.IHolder;

/**
 * @author zhu zhe
 * @version V1.0
 * @since 2015年4月15日 上午9:52:57
 */
@SuppressWarnings("rawtypes")
public abstract class AVAdapter<H extends IHolder, D> extends BaseAdapter<H, D> {

    public AVAdapter(Context context) {
        super(context);
        this.resource = LayoutUtils.getLayoutId(context, getClass());
    }

    public AVAdapter(Context context, int layoutRes) {
        super(context, layoutRes);
    }

    @Override
    View newView(int position) {
        return View.inflate(getContext(), resource, null);
    }

}
