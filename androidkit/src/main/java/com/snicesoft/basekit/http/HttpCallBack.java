package com.snicesoft.basekit.http;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.google.gson.internal.$Gson$Types;

public abstract class HttpCallBack<T> {
	public Type mType;

	public HttpCallBack() {
		mType = getSuperclassTypeParameter(getClass());
	}

	static Type getSuperclassTypeParameter(Class<?> subclass) {
		Type superclass = subclass.getGenericSuperclass();
		if (superclass instanceof Class) {
			throw new RuntimeException("Missing type parameter.");
		}
		ParameterizedType parameterized = (ParameterizedType) superclass;
		return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
	}

	public abstract void onSuccess(T t);

	public void onLoading(long count, long current) {
	}

	public abstract void onError(HttpError error);
}
