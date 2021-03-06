package com.snicesoft.viewbind.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.internal.$Gson$Types;
import com.snicesoft.basekit.util.ListUtils;
import com.snicesoft.viewbind.AVKit;
import com.snicesoft.viewbind.rule.RecyclerHolder;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by zhuzhe on 15/9/24.
 */
public abstract class RecyclerBaseAdapter<VH extends RecyclerHolder, D> extends RecyclerView.Adapter<VH> {
    protected int resource;
    private List<D> dataList;

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
        this.notifyItemInserted(position);
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
        this.notifyItemRemoved(location);
    }

    public final void remove(D d) {
        this.dataList.remove(d);
        notifyDataSetChanged();
    }

    public boolean contains(D d) {
        return this.dataList.contains(d);
    }

    public final void setDataList(Collection<D> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        this.notifyDataSetChanged();
    }

    @Override
    public final VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), resource, null);
        try {
            return newHolder(view);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public final D getItem(int position) {
        return this.dataList != null && !this.dataList.isEmpty()
                ? (position >= this.dataList.size() ? null : this.dataList.get(position)) : null;
    }

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        D data = getItem(position);
        if (data != null) {
            AVKit.bind(data, holder.finder);
        }
        bindHolder(holder, data, position);
    }

    @Override
    public final void onBindViewHolder(VH holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(this.dataList);
    }

    public abstract void bindHolder(VH holder, D data, int position);

    VH newHolder(View view) throws Exception {
        Class vClass = $Gson$Types.getRawType(getType(0));
        if (vClass.getName().contains(getClass().getName() + "$")) {
            if (Modifier.isStatic(vClass.getModifiers()))
                return (VH) vClass.newInstance();
            return (VH) vClass.getConstructors()[0].newInstance(this, view);
        } else
            return (VH) vClass.getConstructors()[0].newInstance(view);

    }

    Type getType(int index) {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[index]);
    }
}
