package com.snicesoft.basekit;


import com.snicesoft.basekit.http.HttpCallBack;
import com.snicesoft.basekit.http.HttpRequest;

@SuppressWarnings("rawtypes")
interface IHttpKit {
    public void setShouldCache(boolean shouldCache);

    public void setTimeout(long timeout);

    public void get(HttpRequest request, HttpCallBack callBack);

    public void post(HttpRequest request, HttpCallBack callBack);

    public void put(HttpRequest request, HttpCallBack callBack);

    public void delete(HttpRequest request, HttpCallBack callBack);

    public void postFile(HttpRequest request, HttpCallBack callBack);

    public void postJSON(HttpRequest request, HttpCallBack callBack);

    public <T> T get(HttpRequest request, Class<T> clazz);

    public <T> T post(HttpRequest request, Class<T> clazz);

    public <T> T put(HttpRequest request, Class<T> clazz);

    public <T> T delete(HttpRequest request, Class<T> clazz);

    public <T> T postFile(HttpRequest request, Class<T> clazz);

    public <T> T postJSON(HttpRequest request, Class<T> clazz);

    public void cancel(HttpRequest request);

    public void cancelAll();
}
