package com.snicesoft.basekit.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by zhe on 16/3/19.
 */
public class GsonUtils {
    private static Gson gson = null;

    public static Gson getGson() {
        if (gson == null)
            gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson;
    }
}
