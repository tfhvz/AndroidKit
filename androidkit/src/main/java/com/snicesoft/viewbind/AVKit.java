package com.snicesoft.viewbind;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    }

    static class IdField {
        public int id;
        public Class<?> annoClass;
        public String fieldName;

        public IdField(int id, String fieldName, Class<?> annoClass) {
            this.id = id;
            this.fieldName = fieldName;
            this.annoClass = annoClass;
        }
    }

    private static ConcurrentHashMap<String, List<IdField>> classIdFields = new ConcurrentHashMap<String, List<IdField>>();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat();

    private static LoadImg loadImg = new LoadImg() {
        @Override
        public void loadImg(View v, int loadingResId, int failResId, String url) {
            BitmapConfig config = new BitmapConfig(loadingResId, failResId);
            BitmapKit.getInstance().display(v, url, config);
        }
    };

    public static void setLoadImg(LoadImg loadImg) {
        AVKit.loadImg = loadImg;
    }

    public static void bind(Object data, ViewFinder finder) {
        if (data == null || finder == null)
            return;
        try {
            Class<?> clazz = data.getClass();
            int i = 0;
            do {
                if (i > 0) {
                    clazz = clazz.getSuperclass();
                }
                bind(data, finder, clazz);
                i++;
            } while (isNotObject(clazz));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void bindTo(Object data, ViewFinder finder, int id) {
        if (data == null || finder == null || id == 0)
            return;
        try {
            Class<?> clazz = data.getClass();
            int i = 0;
            do {
                if (i > 0) {
                    clazz = clazz.getSuperclass();
                }
                IdField idField = getIdField(clazz, id, DataBind.class);
                if (idField != null) {
                    Field field = clazz.getDeclaredField(idField.fieldName);
                    field.setAccessible(true);
                    setViewValue(finder.findViewById(idField.id), new ViewValue(field.get(data), field.getAnnotation(DataBind.class)));
                }
                i++;
            } while (isNotObject(clazz));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getViewId(Id id, Context context) {
        int vid = 0;
        if (id != null) {
            vid = id.value();
            if (vid == 0) {
                vid = context.getResources().getIdentifier(id.name(), "id", context.getPackageName());
            }
        }
        return vid;
    }

    private static int getViewId(DataBind dataBind, Context context) {
        int vid = 0;
        if (dataBind != null) {
            vid = dataBind.value();
            if (vid == 0) {
                vid = context.getResources().getIdentifier(dataBind.name(), "id", context.getPackageName());
            }
        }
        return vid;
    }

    private static void bind(Object data, ViewFinder finder, Class<?> clazz) {
        String className = clazz.getName();
        if (classIdFields.containsKey(className)) {
            for (IdField idField : classIdFields.get(className)) {
                try {
                    Field field = clazz.getDeclaredField(idField.fieldName);
                    field.setAccessible(true);
                    if (idField.annoClass == DataBind.class) {
                        DataBind dataBind = field.getAnnotation(DataBind.class);
                        setViewValue(finder.findViewById(idField.id), new ViewValue(field.get(data), dataBind));
                    } else if (idField.annoClass == Id.class) {
                        View v = finder.findViewById(idField.id);
                        setField(data, field, v);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (!classIdFields.containsKey(className))
                classIdFields.put(className, new ArrayList<IdField>());
            Field[] dataFields = clazz.getDeclaredFields();
            if (dataFields != null && dataFields.length > 0) {
                for (Field field : dataFields) {
                    Annotation[] annotations = field.getAnnotations();
                    if (annotations == null || annotations.length == 0)
                        continue;
                    setData(annotations[0], data, field, finder, clazz);
                }
            }
        }
    }

    private static void setData(Annotation annotation, Object data, Field field, ViewFinder finder, Class<?> clazz) {
        if (annotation == null)
            return;
        try {
            field.setAccessible(true);
            IdField idField = null;
            if (annotation instanceof DataBind) {
                DataBind dataBind = (DataBind) annotation;
                int vid = getViewId(dataBind, finder.getContext());
                idField = new IdField(vid, field.getName(), DataBind.class);
                setViewValue(finder.findViewById(idField.id), new ViewValue(field.get(data), dataBind));
            } else if (annotation instanceof Id) {
                Id id = (Id) annotation;
                int vid = getViewId(id, finder.getContext());
                idField = new IdField(vid, field.getName(), Id.class);
                setField(data, field, finder.findViewById(vid));
            }
            classIdFields.get(clazz.getName()).add(idField);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setField(Object data, Field field, Object value) throws IllegalAccessException {
        field.set(data, value);
    }

    private static boolean isNotObject(Class<?> clazz) {
        return !(clazz.getSuperclass() == Object.class);
    }

    @SuppressWarnings("unchecked")
    private static <T extends View> void setViewValue(T view, ViewValue viewValue) {
        if (view == null || viewValue == null)
            return;
        Object value = viewValue.value;
        String p = viewValue.dataBind.prefix();
        String s = viewValue.dataBind.suffix();
        int loading = viewValue.dataBind.loadingResId();
        String loadName = viewValue.dataBind.loadingResName();
        int fail = viewValue.dataBind.failResId();
        String failName = viewValue.dataBind.failResName();
        String pattern = viewValue.dataBind.pattern();
        switch (viewValue.dataBind.dataType()) {
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
                    } else if (value instanceof PagerAdapter && view instanceof ViewPager) {
                        ((ViewPager) view).setAdapter((PagerAdapter) value);
                    } else if (value instanceof RecyclerView.Adapter && view instanceof RecyclerView) {
                        ((RecyclerView) view).setAdapter((RecyclerView.Adapter) value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case NULL:
                break;
            default:
                break;
        }
        viewValue = null;
    }

    private AVKit() {
    }

    private static IdField getIdField(Class clazz, int id, Class<?> annoClass) {
        if (classIdFields.containsKey(clazz.getName())) {
            for (IdField idField : classIdFields.get(clazz.getName())) {
                if (idField.id == id && idField.annoClass == annoClass) {
                    return idField;
                }
            }
        }
        return null;
    }

    public static void remove(String className) {
        classIdFields.remove(className);
    }

    public static void destory() {
        classIdFields = null;
    }
}