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
import com.android.volley.toolbox.PostUploadRequest;
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
    private Map<HttpRequest, Request> requestMap;
    private RequestQueue volleyQueue;
    private Gson mGson;

    private VolleyHttpKit(Context context) {
        volleyQueue = Volley.newRequestQueue(context);
        requestMap = new HashMap<HttpRequest, Request>();
        mGson = new Gson();
    }

    public synchronized static HttpKit getInstance(Context context) {
        if (instance == null) {
            instance = new VolleyHttpKit(context);
        }
        return instance;
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
        final Listener<String> listener = new Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (callBack != null) {
                    if (callBack.mType == String.class) {
                        callBack.onSuccess(response);
                    } else {
                        Object o = mGson.fromJson(response, callBack.mType);
                        callBack.onSuccess(o);
                    }
                }
            }
        };
        final ErrorListener errorListener = new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (callBack != null) {
                    callBack.onError(new HttpError(error.getMessage()));
                }
            }
        };
        PostUploadRequest volleyRequest = new PostUploadRequest(request.getUrl(), listener, errorListener) {
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
        requestMap.put(request, volleyRequest);
        volleyQueue.add(volleyRequest);
    }

    @Override
    public void postJSON(HttpRequest request, final HttpCallBack callBack) {
        final Listener<String> listener = new Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (callBack != null) {
                    if (callBack.mType == String.class) {
                        callBack.onSuccess(response);
                    } else {
                        Object o = mGson.fromJson(response, callBack.mType);
                        callBack.onSuccess(o);
                    }
                }
            }
        };
        final ErrorListener errorListener = new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (callBack != null) {
                    callBack.onError(new HttpError(error.getMessage()));
                }
            }
        };
        JsonRequest<String> volleyRequest = new JsonRequest<String>(Request.Method.POST, request.getUrl(),
                request.getJson(), listener, errorListener) {

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
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };
        requestMap.put(request, volleyRequest);
        volleyQueue.add(volleyRequest);
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
        final Listener<String> listener = new Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (callBack != null) {
                    if (callBack.mType == String.class) {
                        callBack.onSuccess(response);
                    } else {
                        Object o = mGson.fromJson(response, callBack.mType);
                        callBack.onSuccess(o);
                    }
                }
            }
        };
        final ErrorListener errorListener = new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (callBack != null) {
                    callBack.onError(new HttpError(error.getMessage()));
                }
            }
        };
        StringRequest volleyRequest = new StringRequest(method, request.getUrl(), listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (method == Method.POST || method == Method.PUT) {
                    return request.getParams();
                }
                return super.getParams();
            }
        };
        requestMap.put(request, volleyRequest);
        volleyQueue.add(volleyRequest);
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
