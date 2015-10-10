package com.snicesoft.basekit;


import com.snicesoft.basekit.http.HttpCallBack;
import com.snicesoft.basekit.http.HttpRequest;

@SuppressWarnings("rawtypes")
interface IHttpKit {
    public void get(HttpRequest request, HttpCallBack callBack);

    public void post(HttpRequest request, HttpCallBack callBack);

    public void put(HttpRequest request, HttpCallBack callBack);

    public void delete(HttpRequest request, HttpCallBack callBack);

    public void postFile(HttpRequest request, HttpCallBack callBack);

    public void postJSON(HttpRequest request, HttpCallBack callBack);

    public void cancel(HttpRequest request);

    public void cancelAll();
}
