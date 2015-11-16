package com.snicesoft.viewbind.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.internal.$Gson$Types;
import com.snicesoft.viewbind.AVKit;
import com.snicesoft.viewbind.ViewFinder;
import com.snicesoft.viewbind.rule.IHolder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
            holder.setTag(data);
            holder.setPosition(position);
            if (holder.getPosition() != -1) {
                holder.initViewParams();
            }
        }
        return convertView;
    }

    abstract View newView(int position);

    @Override
    public long getItemId(int position) {
        return 0;
    }

    H newHolder() {
        Class hClass = $Gson$Types.getRawType(getType(0));
        try {
            if (hClass == IHolder.class)
                return null;
            return (H) hClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
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
