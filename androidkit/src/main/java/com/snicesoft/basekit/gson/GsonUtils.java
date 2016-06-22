package com.snicesoft.basekit.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by zhe on 16/3/19.
 */
public class GsonUtils {
    private static Gson gson = null;

    public static void setGson(Gson gson) {
        GsonUtils.gson = gson;
    }

    public static Gson getGson() {
        if (gson == null)
            gson = new GsonBuilder()
                    .setExclusionStrategies(new SkipExclusionStrategy())
                    .serializeNulls()
                    .create();
        return gson;
    }
}
