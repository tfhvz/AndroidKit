package com.snicesoft.viewbind.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.snicesoft.basekit.util.ListUtils;
import com.snicesoft.viewbind.AVKit;
import com.snicesoft.viewbind.ViewFinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("rawtypes")
abstract class BaseAdapter<D> extends android.widget.BaseAdapter {
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
        return ListUtils.getSize(this.dataList);
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
        ViewFinder holder = null;
        if (convertView == null) {
            holder = new ViewFinder(convertView);
            convertView = newView(position);
            convertView.setTag(holder);
        } else {
            holder = (ViewFinder) convertView.getTag();
        }
        if (data != null) {
            AVKit.bind(data, holder);
            dataBind(position, data, holder);
        }
        return convertView;
    }

    protected abstract void dataBind(int position, D data, ViewFinder holder);

    private AbsListView absListView;

    public void setAbsListView(AbsListView absListView) {
        this.absListView = absListView;
    }

    public final void notifyItemChanged(int position) {
        if (absListView == null || position == -1)
            return;
        try {
            int index = position - absListView.getFirstVisiblePosition();
            View mView = absListView.getChildAt(index);//获取指定itemIndex在屏幕中的view
            if (mView == null || mView.getTag() == null)
                return;
            D data = getItem(index);
            if (data != null) {
                AVKit.bind(data, (ViewFinder) mView.getTag());
                dataBind(index, data, (ViewFinder) mView.getTag());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    abstract View newView(int position);

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
