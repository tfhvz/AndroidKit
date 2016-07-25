package com.snicesoft.kits.http.okhttp;

/**
 * Created by zhuzhe on 16/7/21.
 */
public interface ProgressResponseListener {
    void onResponseProgress(long bytesRead, long contentLength, boolean done);
}
