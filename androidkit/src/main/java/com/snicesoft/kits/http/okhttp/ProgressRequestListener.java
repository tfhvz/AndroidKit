package com.snicesoft.kits.http.okhttp;

/**
 * Created by zhuzhe on 16/7/21.
 */
public interface ProgressRequestListener {
    void onRequestProgress(long bytesWritten, long contentLength, boolean done);
}
