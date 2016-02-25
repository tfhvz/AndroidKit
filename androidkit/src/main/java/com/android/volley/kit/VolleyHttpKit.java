package com.android.volley.kit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.MultiPartRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.snicesoft.basekit.HttpKit;
import com.snicesoft.basekit.http.HttpCallBack;
import com.snicesoft.basekit.http.HttpError;
import com.snicesoft.basekit.http.HttpRequest;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * volley 网络加载
 *
 * @author zhuzhe
 */
@SuppressLint({"rawtypes", "unchecked"})
public class VolleyHttpKit extends HttpKit {

    private RequestQueue volleyQueue;
    private Gson mGson;

    private VolleyHttpKit(Context context) {
        volleyQueue = Volley.newRequestQueue(context);
        requestMap = new HashMap<HttpRequest, Request>();
        timeoutTimerMap = new HashMap<HttpCallBack, TimeoutTimer>();
        mGson = new Gson();
    }

    public synchronized static HttpKit getInstance(Context context) {
        if (instance == null) {
            instance = new VolleyHttpKit(context);
        }
        return instance;
    }

    private Listener<String> getListener(final HttpCallBack callBack) {
        return new Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (callBack != null) {
                    if (callBack.mType == String.class) {
                        cancelTimer(callBack);
                        callBack.onSuccess(response);
                    } else {
                        try {
                            Object o = mGson.fromJson(response, callBack.mType);
                            cancelTimer(callBack);
                            callBack.onSuccess(o);
                        } catch (Exception e) {
                            callBack.onError(new HttpError("GSON 匹配错误."));
                        }

                    }
                }
            }
        };
    }

    private ErrorListener getErrorListener(final HttpCallBack callBack) {
        return new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (callBack != null) {
                    if (error == null) {
                        callBack.onError(null);
                    } else {
                        callBack.onError(new HttpError(error.getMessage()));
                    }
                }
            }
        };
    }

    @Override
    public void get(HttpRequest request, HttpCallBack callBack) {
        send(Request.Method.GET, request, callBack);
    }

    @Override
    public void post(HttpRequest request, HttpCallBack callBack) {
        send(Request.Method.POST, request, callBack);
    }

    @Override
    public void put(HttpRequest request, HttpCallBack callBack) {
        send(Request.Method.PUT, request, callBack);
    }

    @Override
    public void delete(HttpRequest request, final HttpCallBack callBack) {
        send(Request.Method.DELETE, request, callBack);
    }

    @Override
    public void postFile(final HttpRequest request, final HttpCallBack callBack) {

        final MultiPartRequest.LoadListener loadListener = new MultiPartRequest.LoadListener() {
            @Override
            public void onLoading(long count, long current) {
                if (callBack != null) {
                    callBack.onLoading(count, current);
                }
            }
        };
        MultiPartRequest volleyRequest = new MultiPartRequest(request.getUrl(), getListener(callBack), getErrorListener(callBack)) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return request.getParams();
            }

            @Override
            public Map<String, File> getFilepPrams() {
                return request.getFiles();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return request.getHeaders();
            }
        };
        volleyRequest.setShouldCache(shouldCache);
        volleyRequest.setLoadListener(loadListener);
        requestMap.put(request, volleyRequest);
        volleyQueue.add(volleyRequest);
        startTimeout(request, callBack);
    }

    @Override
    public void postJSON(final HttpRequest request, final HttpCallBack callBack) {
        JsonRequest<String> volleyRequest = new JsonRequest<String>(Request.Method.POST, request.getUrl(),
                request.getJson(), getListener(callBack), getErrorListener(callBack)) {

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String mString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(mString, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = request.getHeaders();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };
        volleyRequest.setShouldCache(shouldCache);
        requestMap.put(request, volleyRequest);
        volleyQueue.add(volleyRequest);
        startTimeout(request, callBack);
    }

    private void send(final int method, final HttpRequest request, final HttpCallBack callBack) {
        if (request == null || TextUtils.isEmpty(request.getUrl())) {
            if (callBack != null) {
                callBack.onError(new HttpError("URL is NULL."));
            }
            return;
        }
        if (method == Request.Method.GET || method == Request.Method.DELETE) {
            request.setFullUrl();
        }
        StringRequest volleyRequest = new StringRequest(method, request.getUrl(), getListener(callBack), getErrorListener(callBack)) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (method == Method.POST || method == Method.PUT) {
                    return request.getParams();
                }
                return super.getParams();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (request.getHeaders().isEmpty()) {
                    return super.getHeaders();
                } else {
                    return request.getHeaders();
                }
            }
        };
        volleyRequest.setShouldCache(shouldCache);
        requestMap.put(request, volleyRequest);
        volleyQueue.add(volleyRequest);
        startTimeout(request, callBack);
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
        if (requestMap != null) {
            requestMap.clear();
        }
        if (volleyQueue != null) {
            volleyQueue.stop();
        }
    }
}