package com.snicesoft.viewbind.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.snicesoft.viewbind.AVKit;
import com.snicesoft.viewbind.ViewFinder;
import com.snicesoft.viewbind.annotation.Layout;
import com.snicesoft.viewbind.rule.RecyclerHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by zhuzhe on 15/9/24.
 */
public abstract class RecyclerBaseAdapter<VH extends RecyclerHolder, D> extends RecyclerView.Adapter<VH> {
    protected int resource;
    private List<D> dataList;

    public RecyclerBaseAdapter() {
        this.dataList = new ArrayList<D>();
        Layout layout = getClass().getAnnotation(Layout.class);
        if (layout != null && layout.value() != 0) {
            this.resource = layout.value();
        }
    }

    public RecyclerBaseAdapter(int layoutRes) {
        this.dataList = new ArrayList<D>();
        this.resource = layoutRes;
    }

    public final void add(D d) {
        this.dataList.add(d);
        this.notifyDataSetChanged();
    }

    public final void add(int position, D d) {
        this.dataList.add(position, d);
        this.notifyDataSetChanged();
    }

    public final void addAll(Collection<D> collection) {
        this.dataList.addAll(collection);
        this.notifyDataSetChanged();
    }

    public final void addAll(int position, Collection<D> collection) {
        this.dataList.addAll(position, collection);
        this.notifyDataSetChanged();
    }

    public final List<D> getDataList() {
        return this.dataList;
    }

    public final void clear() {
        this.dataList.clear();
        this.notifyDataSetChanged();
    }

    public final void remove(int location) {
        this.dataList.remove(location);
        this.notifyDataSetChanged();
    }

    public final void setDataList(Collection<D> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        this.notifyDataSetChanged();
    }

    @Override
    public final VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), resource, null);
        return newHolder(view);
    }

    public final D getItem(int position) {
        return this.dataList != null && !this.dataList.isEmpty()
                ? (position >= this.dataList.size() ? null : this.dataList.get(position)) : null;
    }

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        D data = getItem(position);
        if (data != null) {
            AVKit.dataBind(data, new ViewFinder(holder.itemView));
        }
        bindHolder(holder, data, position);
    }

    @Override
    public final void onBindViewHolder(VH holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public abstract void bindHolder(VH holder, D data, int position);

    public abstract VH newHolder(View view);
}
