package com.snicesoft.androidkit.otherkit.http;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.snicesoft.basekit.HttpKit;
import com.snicesoft.basekit.gson.GsonUtils;
import com.snicesoft.basekit.http.ContentType;
import com.snicesoft.basekit.http.HttpCallBack;
import com.snicesoft.basekit.http.HttpError;
import com.snicesoft.basekit.http.HttpRequest;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : request.getParams().keySet()) {
            builder.add(key, request.getParams().get(key));
        }
        Request ok = new Request.Builder().url(request.getUrl()).post(builder.build()).build();
        send(request, ok, callBack);
    }

    @Override
    public void put(HttpRequest request, HttpCallBack callBack) {
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : request.getParams().keySet()) {
            builder.add(key, request.getParams().get(key));
        }
        Request ok = new Request.Builder().url(request.getUrl()).put(builder.build()).build();
        send(request, ok, callBack);
    }

    @Override
    public void delete(HttpRequest request, HttpCallBack callBack) {
        request.setFullUrl();
        Request ok = new Request.Builder().url(request.getUrl()).delete().build();
        send(request, ok, callBack);
    }

    @Override
    public void postFile(HttpRequest request, HttpCallBack callBack) {
        MediaType MEDIA_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (String key : request.getParams().keySet()) {
            builder.addFormDataPart(key, request.getParams().get(key));
        }
        for (String key : request.getFiles().keySet()) {
            File file = request.getFiles().get(key);
            builder.addFormDataPart(key, file.getName(), RequestBody.create(MEDIA_TYPE, file));
        }
        Request ok = new Request.Builder().url(request.getUrl()).post(builder.build()).build();
        send(request, ok, callBack);
    }

    @Override
    public void postJSON(HttpRequest request, HttpCallBack callBack) {
        RequestBody body = RequestBody.create(MediaType.parse(ContentType.JSON), request.getJson());
        Request ok = new Request.Builder().url(request.getUrl()).post(body).build();
        send(request, ok, callBack);
    }

    private void send(HttpRequest request, Request ok, final HttpCallBack callBack) {
        startTimeout(request, callBack);
        Call call = client.newCall(ok);
        requestMap.put(request, call);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                cancelTimer(callBack);
                mHandler.post(new Runnable() {
                    public void run() {
                        if (callBack != null) {
                            callBack.onError(new HttpError(e.getMessage()));
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String body = response.body().string();
                cancelTimer(callBack);
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
