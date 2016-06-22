package com.snicesoft.viewbind;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import com.snicesoft.basekit.BitmapKit;
import com.snicesoft.basekit.bitmap.BitmapConfig;
import com.snicesoft.basekit.util.ListUtils;
import com.snicesoft.viewbind.annotation.DataBind;
import com.snicesoft.viewbind.bind.BindImg;
import com.snicesoft.viewbind.bind.IBind;
import com.snicesoft.viewbind.bind.IBindBuilder;
import com.snicesoft.viewbind.rule.RecyclerHolder;
import com.snicesoft.viewbind.utils.AutoUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
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
                    DataBind dataBind = field.getAnnotation(DataBind.class);
                    setViewValue(finder.findViewById(idField.id), field.get(data), IBindBuilder.create(dataBind));
                }
                i++;
            } while (isNotObject(clazz));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            if (ListUtils.isEmpty(classIdFields.get(className)))
                return;
            for (IdField idField : classIdFields.get(className)) {
                if (idField == null)
                    continue;
                try {
                    Field field = clazz.getDeclaredField(idField.fieldName);
                    field.setAccessible(true);
                    if (idField.annoClass == DataBind.class) {
                        DataBind dataBind = field.getAnnotation(DataBind.class);
                        setViewValue(finder.findViewById(idField.id), field.get(data), IBindBuilder.create(dataBind));
                    } else if (idField.annoClass == com.snicesoft.viewbind.annotation.Context.class) {
                        AutoUtils.loadContext(finder.getContext(), field, data);
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
                    for (Annotation an : annotations) {
                        setData(an, data, field, finder, clazz);
                    }
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
                setViewValue(finder.findViewById(idField.id), field.get(data), IBindBuilder.create(dataBind));
            } else if (annotation instanceof com.snicesoft.viewbind.annotation.Context) {
                idField = new IdField(0, field.getName(), com.snicesoft.viewbind.annotation.Context.class);
                AutoUtils.loadContext(finder.getContext(), field, data);
            } else {
                return;
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
        return !(clazz.getSuperclass() == Object.class || clazz.getSuperclass() == RecyclerHolder.class);
    }

    private static <T extends View> void setViewValue(T view, Object value, IBind bind) {
        if (view == null || value == null || bind == null)
            return;
        if (bind instanceof BindImg)
            ((BindImg) bind).setLoadImg(loadImg);
        bind.bindView(view, value);
        bind = null;
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