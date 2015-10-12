package com.snicesoft.viewbind.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.snicesoft.viewbind.AVKit;
import com.snicesoft.viewbind.ViewFinder;
import com.snicesoft.viewbind.rule.IHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("rawtypes")
abstract class BaseAdapter<H extends IHolder, D> extends android.widget.BaseAdapter {
    private Context context;
    protected int resource;
    private List<D> dataList = new ArrayList<D>();

    public BaseAdapter(Context context) {
        this(context, 0);
    }

    public BaseAdapter(Context context, int layoutRes) {
        this.context = context;
        this.resource = layoutRes;
    }

    public final void add(D d) {
        this.dataList.add(d);
        notifyDataSetChanged();
    }

    public final void add(int position, D d) {
        this.dataList.add(position, d);
        notifyDataSetChanged();
    }

    public final void addAll(Collection<D> collection) {
        this.dataList.addAll(collection);
        notifyDataSetChanged();
    }

    public final void addAll(int position, Collection<D> collection) {
        this.dataList.addAll(position, collection);
        notifyDataSetChanged();
    }

    public final List<D> getDataList() {
        return this.dataList;
    }

    public final void clear() {
        this.dataList.clear();
        notifyDataSetChanged();
    }

    public final void remove(int location) {
        this.dataList.remove(location);
        notifyDataSetChanged();
    }

    public final void setDataList(Collection<D> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public final Context getContext() {
        return context;
    }

    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public D getItem(int position) {
        if (dataList == null || dataList.isEmpty())
            return null;
        if (position >= dataList.size()) {
            return null;
        }
        return dataList.get(position);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        D data = getItem(position);
        H holder = null;
        if (convertView == null) {
            holder = newHolder();
            convertView = newView(position);
            if (holder != null) {
                holder.setTag(data);
                holder.setParent(convertView);
                AVKit.initHolder(holder, new ViewFinder(convertView));
                convertView.setTag(holder);
            }
        } else {
            holder = (H) convertView.getTag();
        }
        if (data != null) {
            AVKit.dataBind(data, new ViewFinder(convertView));
        }
        if (holder != null) {
            holder.setPosition(position);
            holder.initViewParams();
        }
        return convertView;
    }

    abstract View newView(int position);

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * 创建Holder
     *
     * @return
     */
    public H newHolder() {
        return null;
    }
}
