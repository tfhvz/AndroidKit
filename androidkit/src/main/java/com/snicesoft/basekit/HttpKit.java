package com.snicesoft.basekit;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;

import com.snicesoft.basekit.http.HttpCallBack;
import com.snicesoft.basekit.http.HttpError;
import com.snicesoft.basekit.http.HttpRequest;

import java.util.Map;

public abstract class HttpKit implements IHttpKit {

    protected static HttpKit instance;
    protected Map<HttpCallBack, TimeoutTimer> timeoutTimerMap;
    protected boolean shouldCache = true;
    protected long timeout = 10 * 1000L;
    protected Handler mHandler = new Handler(Looper.getMainLooper());

    public synchronized static HttpKit getInstance() {
        return instance;
    }

    @Override
    public void setShouldCache(boolean shouldCache) {
        this.shouldCache = shouldCache;
    }

    @Override
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    protected void startTimeout(final HttpRequest request, final HttpCallBack callBack) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                TimeoutTimer timeoutTimer = new TimeoutTimer(timeout, 1000).bind(instance).add(request, callBack);
                timeoutTimerMap.put(callBack, timeoutTimer);
                timeoutTimer.start();
            }
        });

    }

    protected void cancelTimer(HttpCallBack callBack) {
        if (timeoutTimerMap.get(callBack) != null) {
            timeoutTimerMap.get(callBack).cancel();
            timeoutTimerMap.remove(callBack);
        }
    }

    public class TimeoutTimer extends CountDownTimer {
        HttpCallBack callBack;
        HttpRequest request;
        IHttpKit httpKit;

        public TimeoutTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public TimeoutTimer bind(IHttpKit httpKit) {
            this.httpKit = httpKit;
            return this;
        }

        public TimeoutTimer add(HttpRequest request, HttpCallBack callBack) {
            this.callBack = callBack;
            this.request = request;
            return this;
        }

        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
            this.callBack.onError(new HttpError("timeout"));
            this.httpKit.cancel(this.request);
        }
    }
}
