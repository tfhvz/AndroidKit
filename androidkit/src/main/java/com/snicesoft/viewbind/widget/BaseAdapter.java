package com.snicesoft.viewbind.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.google.gson.internal.$Gson$Types;
import com.snicesoft.viewbind.AVKit;
import com.snicesoft.viewbind.ViewFinder;
import com.snicesoft.viewbind.rule.IHolder;

import java.lang.reflect.Modifier;
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
            try {
                holder = newHolder();
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    private AbsListView absListView;

    public void setAbsListView(AbsListView absListView) {
        this.absListView = absListView;
    }

    public final void notifyItem(int position) {
        if (absListView == null || position == -1)
            return;
        View mView = absListView.getChildAt(position - absListView.getFirstVisiblePosition());
        if (mView.getTag() == null)
            return;
        H holder = (H) mView.getTag();
        holder.initViewParams();
    }

    abstract View newView(int position);

    @Override
    public long getItemId(int position) {
        return 0;
    }

    H newHolder() throws Exception {
        Class hClass = $Gson$Types.getRawType(getType(0));
        if (hClass == IHolder.class)
            return null;
        if (hClass.getName().contains(getClass().getName() + "$")) {
            if (Modifier.isStatic(hClass.getModifiers()))
                return (H) hClass.newInstance();
            return (H) hClass.getConstructors()[0].newInstance(this);
        }
        return (H) hClass.newInstance();
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
