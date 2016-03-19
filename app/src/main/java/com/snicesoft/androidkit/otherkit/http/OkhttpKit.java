package com.snicesoft.androidkit.otherkit.http;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.snicesoft.basekit.HttpKit;
import com.snicesoft.basekit.http.ContentType;
import com.snicesoft.basekit.http.HttpCallBack;
import com.snicesoft.basekit.http.HttpError;
import com.snicesoft.basekit.http.HttpRequest;
import com.snicesoft.basekit.gson.GsonUtils;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * OKhttp 网络请求
 *
 * @author zhuzhe
 */
public class OkhttpKit extends HttpKit {
    private Map<HttpRequest, Call> requestMap;
    private OkHttpClient client;
    private Handler mHandler;
    private Gson mGson;

    private OkhttpKit() {
        client = new OkHttpClient();
        client.setConnectTimeout(30, TimeUnit.SECONDS);
        mHandler = new Handler(Looper.getMainLooper());
        mGson = GsonUtils.getGson();
        requestMap = new HashMap<HttpRequest, Call>();
    }

    public synchronized static HttpKit getInstance() {
        if (instance == null) {
            instance = new OkhttpKit();
        }
        return instance;
    }

    @Override
    public void setShouldCache(boolean shouldCache) {

    }

    @Override
    public void get(HttpRequest request, HttpCallBack callBack) {
        request.setFullUrl();
        Request ok = new Request.Builder().url(request.getUrl()).build();
        send(request, ok, callBack);
    }

    @Override
    public void post(HttpRequest request, HttpCallBack callBack) {
        FormEncodingBuilder formBuilder = new FormEncodingBuilder();
        for (String key : request.getParams().keySet()) {
            formBuilder.add(key, request.getParams().get(key));
        }
        Request ok = new Request.Builder().url(request.getUrl()).post(formBuilder.build()).build();
        send(request, ok, callBack);
    }

    @Override
    public void put(HttpRequest request, HttpCallBack callBack) {

    }

    @Override
    public void delete(HttpRequest request, HttpCallBack callBack) {

    }

    @Override
    public void postFile(HttpRequest request, HttpCallBack callBack) {
        MediaType MEDIA_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
        Request ok = new Request.Builder().url(request.getUrl())
                .post(RequestBody.create(MEDIA_TYPE, request.getFiles().get(0))).build();
        send(request, ok, callBack);
    }

    @Override
    public void postJSON(HttpRequest request, HttpCallBack callBack) {
        RequestBody body = RequestBody.create(MediaType.parse(ContentType.JSON), request.getJson());
        Request ok = new Request.Builder().url(request.getUrl()).post(body).build();
        send(request, ok, callBack);
    }

    private void send(HttpRequest request, Request ok, final HttpCallBack callBack) {
        Call call = client.newCall(ok);
        requestMap.put(request, call);
        call.enqueue(new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {
                final String body = arg0.body().string();
                mHandler.post(new Runnable() {
                    public void run() {
                        if (callBack != null) {
                            if (callBack.mType == String.class) {
                                callBack.onSuccess(body);
                            } else {
                                Object o = mGson.fromJson(body, callBack.mType);
                                callBack.onSuccess(o);
                            }
                        }
                    }
                });
            }

            @Override
            public void onFailure(Request arg0, final IOException arg1) {
                mHandler.post(new Runnable() {
                    public void run() {
                        if (callBack != null) {
                            callBack.onError(new HttpError(arg1.getMessage()));
                        }
                    }
                });
            }
        });
    }

    @Override
    public void cancel(HttpRequest request) {
        if (requestMap.containsKey(request)) {
            if (!requestMap.get(request).isCanceled())
                requestMap.get(request).cancel();
        }
    }

    @Override
    public void cancelAll() {
        for (HttpRequest request : requestMap.keySet()) {
            cancel(request);
        }
    }
}
