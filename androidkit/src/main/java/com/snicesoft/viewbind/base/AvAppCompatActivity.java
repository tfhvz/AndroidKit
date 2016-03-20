package com.snicesoft.viewbind.base;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.internal.$Gson$Types;
import com.snicesoft.framework.AVFragment;
import com.snicesoft.viewbind.AVKit;
import com.snicesoft.viewbind.ViewFinder;
import com.snicesoft.viewbind.utils.LayoutUtils;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * FragmentActivity基类
 *
 * @param <HD>
 * @author zhe
 */
@SuppressWarnings("rawtypes")
@SuppressLint("NewApi")
public abstract class AvAppCompatActivity<HD> extends AppCompatActivity implements IAv, OnClickListener {
    private ViewFinder finder;
    protected HD _hd;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public ViewFinder getFinder() {
        return finder;
    }

    @Override
    public final void bindAll() {
        bind(_hd);
    }

    @Override
    public final void bindTo(int id) {
        AVKit.bindTo(_hd, finder, id);
    }

    @Override
    public final void bind(Object holder) {
        AVKit.bind(holder, finder);
    }

    @Override
    public int layout() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LayoutUtils.getLayoutId(this, getThisClass()));
        finder = new ViewFinder(this);
        try {
            _hd = newHD();
            bind(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        bindAll();
        onLoaded();
        loadNetData();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    protected final Fragment findFragmentById(int id) {
        return getSupportFragmentManager().findFragmentById(id);
    }

    protected final <F extends AVFragment> F findFragmentById(int id, Class<F> clazz) {
        return (F) getSupportFragmentManager().findFragmentById(id);
    }

    protected final Class<?> getThisClass() {
        Class<?> clazz = getClass();
        if (Proxy.PROXY_ACTIVITY.equals(clazz.getName())) {
            clazz = getClass().getSuperclass();
        }
        return clazz;
    }

    HD newHD() throws Exception {
        Type type = getType(0);
        if (type == null)
            return null;
        Class hClass = $Gson$Types.getRawType(type);
        if (hClass == Void.class)
            return null;
        if (hClass.getName().contains(getThisClass().getName() + "$")) {
            if (Modifier.isStatic(hClass.getModifiers()))
                return (HD) hClass.newInstance();
            return (HD) hClass.getConstructors()[0].newInstance(this);
        }
        return (HD) hClass.newInstance();
    }

    Type getType(int index) {
        Type superclass = getThisClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            return null;
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[index]);
    }

    @Override
    public void onClick(View v) {
        if (v == null)
            return;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        _hd = null;
        finder = null;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "AvAppCompat Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.snicesoft.viewbind.base/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "AvAppCompat Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.snicesoft.viewbind.base/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}