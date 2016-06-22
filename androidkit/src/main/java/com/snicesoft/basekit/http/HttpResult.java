package com.snicesoft.basekit.http;

/**
 * Created by zhuzhe on 15/10/30.
 */
public abstract class HttpResult<T> {
    public abstract void onSuccess(T t);

    public void onError(String error) {

    }

    public void onLoading(long count, long current) {
    }
}
