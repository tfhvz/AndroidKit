
package com.android.volley.toolbox;

import android.os.SystemClock;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;

import org.apache.http.multipart.FilePart;
import org.apache.http.multipart.StringPart;
import org.apache.http.multipart.UploadMultipartEntity;
import org.apache.http.multipart.UploadMultipartEntity.ProgressListener;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * A request for making a Multi Part request
 */
public abstract class MultiPartRequest extends StringRequest {
    public interface LoadListener {
        void onLoading(long count, long current);
    }

    LoadListener loadListener;

    public void setLoadListener(LoadListener loadListener) {
        this.loadListener = loadListener;
    }

    private static final String PROTOCOL_CHARSET = "utf-8";

    private UploadMultipartEntity mMultipartEntity;
    /**
     * Default connection timeout for Multipart requests
     */
    public static final int TIMEOUT_MS = 60000;

    public MultiPartRequest(String url, Response.Listener<String> listener,
                            Response.ErrorListener errorListener) {
        super(Method.POST, url, listener, errorListener);
        mMultipartEntity = new UploadMultipartEntity();
        mMultipartEntity.setListener(new ProgressListener() {
            public long getRate() {
                return 100L;
            }

            long time = SystemClock.uptimeMillis();
            long count = -1;

            @Override
            public void transferred(long num) {
                if (count == -1) {
                    count = mMultipartEntity.getContentLength();
                }
                if (loadListener != null) {
                    loadListener.onLoading(count, num);
                }
            }
        });
        setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public String getBodyContentType() {
        try {
            if (getParams() != null) {
                Set<String> keySet = getParams().keySet();
                for (Iterator<String> it = keySet.iterator(); it.hasNext(); ) {
                    String key = it.next();
                    String value = getParams().get(key);
                    addPart(key, value);
                }
            }
            if (getFilepPrams() != null) {
                Set<String> keySet = getFilepPrams().keySet();
                for (Iterator<String> it = keySet.iterator(); it.hasNext(); ) {
                    String key = it.next();
                    File value = getFilepPrams().get(key);
                    addPart(key, value);
                }
            }
        } catch (AuthFailureError e) {
            e.printStackTrace();
        }
        return mMultipartEntity.getContentType().getValue();
    }

    private void addPart(String key, String value) {
        StringPart part = new StringPart(key, value, PROTOCOL_CHARSET);
        mMultipartEntity.addPart(part);
    }

    private void addPart(String key, File file) {
        FilePart part = new FilePart(key, file, null, null);
        mMultipartEntity.addPart(part);
    }

    public UploadMultipartEntity getMultipartEntity() {
        return mMultipartEntity;
    }

    public abstract Map<String, File> getFilepPrams() throws AuthFailureError;
}
