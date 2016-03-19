package com.snicesoft.basekit.gson;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * Created by zhe on 16/3/19.
 */
public class SkipExclusionStrategy implements ExclusionStrategy {
    private final Class<?>[] typeToSkip;

    public SkipExclusionStrategy(Class<?>... typeToSkip) {
        this.typeToSkip = typeToSkip;
    }

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotation(GsonSkip.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        if (typeToSkip == null)
            return false;
        for (Class<?> skip : typeToSkip) {
            if (skip == clazz)
                return true;
        }
        return false;
    }
}
