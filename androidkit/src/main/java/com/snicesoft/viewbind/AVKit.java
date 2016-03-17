package com.snicesoft.viewbind;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import com.snicesoft.basekit.BitmapKit;
import com.snicesoft.basekit.bitmap.BitmapConfig;
import com.snicesoft.viewbind.annotation.DataBind;
import com.snicesoft.viewbind.annotation.Id;
import com.snicesoft.viewbind.rule.IHolder;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhu zhe
 * @version V1.0
 * @since 2015年4月15日 上午9:50:38
 */
@SuppressLint({"SimpleDateFormat", "UseSparseArrays"})
public class AVKit {
    public interface LoadImg {
        void loadImg(View v, int loadingResId, int failResId, String url);
    }

    static class ViewValue {
        public DataBind dataBind;
        public Object value;

        public ViewValue(Object value, DataBind dataBind) {
            super();
            this.value = value;
            this.dataBind = dataBind;
        }

        public DataBind getDataBind() {
            return dataBind;
        }

        public Object getValue() {
            return value;
        }
    }

    static class IdField {
        public int id;
        public String fieldName;

        public IdField(int id, String fieldName) {
            this.id = id;
            this.fieldName = fieldName;
        }

        public String getFieldName() {
            return fieldName;
        }

        public int getId() {
            return id;
        }
    }

    private static Map<String, List<IdField>> classIdFields = new HashMap<String, List<IdField>>();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat();

    private static LoadImg loadImg = new LoadImg() {
        @Override
        public void loadImg(View v, int loadingResId, int failResId, String url) {
            BitmapConfig config = new BitmapConfig(loadingResId, failResId);
            BitmapKit.getInstance().display(v, url, config);
        }
    };

    /**
     * 数据绑定到view
     *
     * @param <D>
     * @param <D>
     * @param data
     * @param finder
     */
    public static <D> void dataBind(D data, ViewFinder finder) {
        if (data == null || finder == null)
            return;
        try {
            Class<?> clazz = data.getClass();
            int i = 0;
            do {
                if (i > 0) {
                    clazz = clazz.getSuperclass();
                }
                dataBind(data, finder, clazz);
                i++;
            } while (isNotObject(clazz));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public static <D> void dataBindTo(D data, ViewFinder finder, String fieldName) {
        if (data == null || finder == null || TextUtils.isEmpty(fieldName))
            return;
        try {
            Class<?> clazz = data.getClass();
            int i = 0;
            do {
                if (i > 0) {
                    clazz = clazz.getSuperclass();
                }
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                bindValue(data, finder, field);
                i++;
            } while (isNotObject(clazz));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <D> void dataBindTo(D data, ViewFinder finder, int id) {
        if (data == null || finder == null || id == 0)
            return;
        try {
            Class<?> clazz = data.getClass();
            int i = 0;
            do {
                if (i > 0) {
                    clazz = clazz.getSuperclass();
                }
                Field field = getField(clazz, id);
                field.setAccessible(true);
                bindValue(data, finder, field);
                i++;
            } while (isNotObject(clazz));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static <D> void dataBind(D data, ViewFinder finder, Class<?> clazz) {
        Field[] dataFields = clazz.getDeclaredFields();
        if (dataFields != null && dataFields.length > 0) {
            for (Field field : dataFields) {
                if (field.getAnnotation(DataBind.class) == null)
                    continue;
                try {
                    field.setAccessible(true);
                    checkClassName(clazz.getName());
                    int vid = getVid(finder, field);
                    classIdFields.get(clazz.getName()).add(new IdField(vid, field.getName()));
                    bindValue(data, finder, field);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void checkClassName(String name) {
        if (!classIdFields.containsKey(name))
            classIdFields.put(name, new ArrayList<IdField>());
        else
            classIdFields.get(name).clear();
    }

    private static void bindValue(Object data, ViewFinder finder, Field field) throws IllegalAccessException {
        Object value = field.get(data);
        if (value == null)
            return;
        DataBind dataBind = field.getAnnotation(DataBind.class);
        if (dataBind != null) {
            int vid = getVid(dataBind, finder);
            setValue(finder.findViewById(vid), new ViewValue(value, dataBind));
        }
    }

    private static int getVid(ViewFinder finder, Field field) {
        DataBind dataBind = field.getAnnotation(DataBind.class);
        if (dataBind != null) {
            return getVid(dataBind, finder);
        } else {
            return 0;
        }
    }

    private static int getVid(DataBind dataBind, ViewFinder finder) {
        int vid = 0;
        if (dataBind != null) {
            vid = dataBind.id();
            if (vid == 0) {
                Context context = finder.getParent().getContext();
                vid = context.getResources().getIdentifier(dataBind.name(), "id", context.getPackageName());
            }
        }
        return vid;
    }

    @SuppressWarnings("rawtypes")
    public static <H extends IHolder> void initHolder(H holder, ViewFinder finder) {
        if (holder == null || finder == null)
            return;
        try {
            holder.setParent(finder.getParent());
            Class<?> clazz = holder.getClass();
            int i = 0;
            do {
                if (i > 0) {
                    clazz = clazz.getSuperclass();
                }
                initHolder(holder, finder, clazz);
                i++;
            } while (isNotIHolder(clazz));
            if (holder != null)
                holder.initViewParams();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("rawtypes")
    public static void initHolder(Object holder, ViewFinder finder) {
        if (holder == null || finder == null)
            return;
        try {
            Class<?> clazz = holder.getClass();
            int i = 0;
            do {
                if (i > 0) {
                    clazz = clazz.getSuperclass();
                }
                initHolder(holder, finder, clazz);
                i++;
            } while (isNotObject(clazz));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initHolder(Object holder, ViewFinder finder, Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        if (fields != null && fields.length > 0)
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    Id resource = field.getAnnotation(Id.class);
                    if (resource == null)
                        continue;
                    int resId = resource.value();
                    if (resId == 0) {
                        Context context = finder.getParent().getContext();
                        resId = context.getResources().getIdentifier(resource.name(), "id", context.getPackageName());
                    }
                    View v = finder.findViewById(resId);
                    if (v != null) {
                        if (field.get(holder) == null)
                            field.set(holder, v);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    }

    private static boolean isNotObject(Class<?> clazz) {
        return !(clazz.getSuperclass() == Object.class);
    }

    private static boolean isNotIHolder(Class<?> clazz) {
        return !(clazz.getSuperclass() == IHolder.class);
    }

    public static void setLoadImg(LoadImg loadImg) {
        AVKit.loadImg = loadImg;
    }

    @SuppressWarnings("unchecked")
    private static <T extends View> void setValue(T view, ViewValue viewValue) {
        if (view == null || viewValue == null)
            return;
        Object value = viewValue.getValue();
        String p = viewValue.getDataBind().prefix();
        String s = viewValue.getDataBind().suffix();
        int loading = viewValue.getDataBind().loadingResId();
        String loadName = viewValue.getDataBind().loadingResName();
        int fail = viewValue.getDataBind().failResId();
        String failName = viewValue.getDataBind().failResName();
        String pattern = viewValue.getDataBind().pattern();
        switch (viewValue.getDataBind().dataType()) {
            case STRING:
                TextView tv = (TextView) view;
                if (TextUtils.isEmpty(pattern)) {
                    tv.setText(p + value + s);
                } else {
                    dateFormat.applyPattern(pattern);
                    if (value instanceof Long || value instanceof Date) {
                        tv.setText(p + dateFormat.format(value) + s);
                    } else if (value instanceof String) {
                        tv.setText(p + value + s);
                    }
                }
                break;
            case HTML:
                TextView html = (TextView) view;
                html.setText(Html.fromHtml(p + value + s));
                break;
            case IMG:
                if (value instanceof Integer) {
                    int resId = Integer.parseInt(value.toString());
                    if (view instanceof ImageView) {
                        ((ImageView) view).setImageResource(resId);
                    } else {
                        view.setBackgroundResource(resId);
                    }
                } else if (value instanceof String) {
                    if (loadImg != null) {
                        Context context = view.getContext();
                        if (loading == 0) {
                            loading = context.getResources().getIdentifier(loadName, "drawable", context.getPackageName()) | context.getResources().getIdentifier(loadName, "mipmap", context.getPackageName());
                        }
                        if (fail == 0) {
                            fail = context.getResources().getIdentifier(failName, "drawable", context.getPackageName()) | context.getResources().getIdentifier(failName, "mipmap", context.getPackageName());
                        }
                        loadImg.loadImg(view, loading, fail, p + value.toString() + s);
                    }
                }
                break;
            case CHECK:
                if (value instanceof Boolean) {
                    Checkable checkable = (Checkable) view;
                    checkable.setChecked(Boolean.getBoolean(value + ""));
                }
                break;
            case ADAPTER:
                try {
                    if (value instanceof Adapter && view instanceof AdapterView) {
                        ((AdapterView<Adapter>) view).setAdapter((Adapter) value);
                    }
                    if (value instanceof PagerAdapter && view instanceof ViewPager) {
                        ((ViewPager) view).setAdapter((PagerAdapter) value);
                    }
                } catch (Exception e) {
                }
                break;
            case NULL:
                break;
            default:
                break;
        }
    }

    private AVKit() {
    }

    private static Field getField(Class clazz, int id) {
        Field field = null;
        if (classIdFields.containsKey(clazz.getName())) {
            for (IdField idField : classIdFields.get(clazz.getName())) {
                if (idField.getId() == id) {
                    try {
                        field = clazz.getDeclaredField(idField.getFieldName());
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
        return field;
    }

    public static void remove(String className) {
        classIdFields.remove(className);
    }

    public static void destory() {
        classIdFields = null;
    }
}
