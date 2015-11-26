/*
 * Copyright (C) 2015 HouKx <hkx.aidream@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.snicesoft.pluginkit;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Looper;
import android.util.Log;

import com.snicesoft.pluginkit.env.FrameworkInstrumentation;
import com.snicesoft.pluginkit.env.PlugInfo;
import com.snicesoft.pluginkit.env.PluginClassLoader;
import com.snicesoft.pluginkit.env.PluginContextWrapper;
import com.snicesoft.pluginkit.utils.FileUtil;
import com.snicesoft.pluginkit.utils.PluginManifestUtil;
import com.snicesoft.pluginkit.utils.ReflectionUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 插件管理器
 *
 * @author HouKangxi
 */
public class PluginManager implements FileFilter {
    private static final String tag = PluginManager.class.getSimpleName();

    private static PluginManager instance;
    /**
     * 插件包名 -- 插件信息 的映射
     */
    private final Map<String, PlugInfo> pluginIdToInfoMap = new ConcurrentHashMap<String, PlugInfo>();
    /**
     * 插件包名 -- 插件信息 的映射
     */
    private final Map<String, PlugInfo> pluginPkgToInfoMap = new ConcurrentHashMap<String, PlugInfo>();
    private Context context;
    /**
     * 插件dex opt输出路径
     */
    private String dexOutputPath;
    /**
     * 私有目录中存储插件的路径
     */
    private File dexInternalStoragePath;
    /**
     * Activity生命周期监听器
     */
    private PluginActivityLifeCycleCallback pluginActivityLifeCycleCallback;
    private volatile SoftReference<Activity> actFrom;

    private PluginManager(Context context) {
        if (!isMainThread()) {
            throw new IllegalThreadStateException("PluginManager must init in UI Thread!");
        }
        if (context instanceof Activity) {
            instance.actFrom = new SoftReference<Activity>((Activity) context);
            this.context = ((Activity) context).getApplication();
        } else if (context instanceof Service) {
            this.context = ((Service) context).getApplication();
        } else if (context instanceof Application) {
            this.context = (Application) context;
        } else {
            this.context = context.getApplicationContext();
        }
        File optimizedDexPath = context.getDir(Globals.PRIVATE_PLUGIN_OUTPUT_DIR_NAME, Context.MODE_PRIVATE);
        if (!optimizedDexPath.exists()) {
            if (!optimizedDexPath.mkdirs()) {
                Log.w(tag, "Cannot access optimizedDexPath!");
            }
        }
        dexOutputPath = optimizedDexPath.getAbsolutePath();
        dexInternalStoragePath = context.getDir(Globals.PRIVATE_PLUGIN_ODEX_OUTPUT_DIR_NAME, Context.MODE_PRIVATE);
        if (!dexInternalStoragePath.mkdirs()) {
            Log.w(tag, "Cannot access dexInternalStoragePath!");
        }
        try {
            Context contextImpl = ((ContextWrapper) context).getBaseContext();
            Object activityThread = ReflectionUtils.getFieldValue(contextImpl, "mMainThread");
            Field instrumentationF = activityThread.getClass().getDeclaredField("mInstrumentation");
            instrumentationF.setAccessible(true);
            FrameworkInstrumentation instrumentation = new FrameworkInstrumentation();
            instrumentationF.set(activityThread, instrumentation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return 当前是否为主线程
     */
    public boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    public static PluginManager getInstance(Context context) {
        checkInit();
        return instance;
    }

    public static PluginManager getInstance() {
        return instance;
    }

    public boolean startMainActivity(Context context, String pkgOrId) {
        Log.d(tag, "startMainActivity by:" + pkgOrId);
        PlugInfo plug = preparePlugForStartActivity(context, pkgOrId);
        if (plug.getMainActivity() == null) {
            Log.e(tag, "startMainActivity: plug.getMainActivity() == null!");
            return false;
        }
        if (plug.getMainActivity().activityInfo == null) {
            Log.e(tag,
                    "startMainActivity: plug.getMainActivity().activityInfo == null!");
            return false;
        }
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(pkgOrId,
                plug.getMainActivity().activityInfo.name));
        startActivity(context, intent);
        return true;
    }

    public void startActivity(Context context, Intent intent) {
        performStartActivity(context, intent);
        context.startActivity(intent);
    }

    public void startActivityForResult(Activity activity, Intent intent,
                                       int requestCode) {
        performStartActivity(context, intent);
        activity.startActivityForResult(intent, requestCode);
    }

    private PlugInfo preparePlugForStartActivity(Context context,
                                                 String plugIdOrPkg) {
        PlugInfo plug = null;
        plug = getPluginByPackageName(plugIdOrPkg);
        if (plug == null) {
            plug = getPluginById(plugIdOrPkg);
        }
        if (plug == null) {
            throw new IllegalArgumentException("plug not found by:"
                    + plugIdOrPkg);
        }
        return plug;
    }

    private void performStartActivity(Context context, Intent intent) {
        checkInit();
        String plugIdOrPkg;
        String actName;
        ComponentName origComp = intent.getComponent();
        if (origComp != null) {
            plugIdOrPkg = origComp.getPackageName();
            actName = origComp.getClassName();
        } else {
            throw new IllegalArgumentException("plug intent must set the ComponentName!");
        }
        PlugInfo plug = preparePlugForStartActivity(context, plugIdOrPkg);
        Log.i(tag, "performStartActivity: " + actName);
        intent.putExtra(Globals.PLUGIN_ID, plug.getId());
        intent.putExtra(Globals.PLUGIN_ACTIVITY, actName);
        ComponentName comp = new ComponentName(context, Globals.getPluginTargetActivity());
        intent.setComponent(comp);
    }

    private static void checkInit() {
        if (instance == null) {
            throw new IllegalStateException("PluginManager has not init!");
        }
    }

    public PlugInfo getPluginById(String pluginId) {
        if (pluginId == null) {
            return null;
        }
        return pluginIdToInfoMap.get(pluginId);
    }

    public PlugInfo getPluginByPackageName(String packageName) {
        return pluginPkgToInfoMap.get(packageName);
    }

    public Collection<PlugInfo> getPlugins() {
        return pluginIdToInfoMap.values();
    }

    public void uninstallPluginById(String pluginId) {
        uninstallPlugin(pluginId, true);
    }

    public void uninstallPluginByPkg(String pkg) {
        uninstallPlugin(pkg, false);
    }

    private void uninstallPlugin(String k, boolean isId) {
        checkInit();
        File dex = new File(dexOutputPath + "/" + k.replace(".apk", ".dex"));
        if (dex.exists())
            dex.delete();
        PlugInfo pl = isId ? removePlugById(k) : removePlugByPkg(k);
        if (pl == null) {
            return;
        }
        if (context instanceof Application) {
            try {
                ReflectionUtils.invokeMethod(context, pl.getApplication(), "unregisterComponentCallbacks");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private PlugInfo removePlugById(String pluginId) {
        PlugInfo pl = null;
        synchronized (this) {
            pl = pluginIdToInfoMap.remove(pluginId);
            if (pl == null) {
                return null;
            }
            pluginPkgToInfoMap.remove(pl.getPackageName());
        }
        return pl;
    }

    private PlugInfo removePlugByPkg(String pkg) {
        PlugInfo pl = null;
        synchronized (this) {
            pl = pluginPkgToInfoMap.remove(pkg);
            if (pl == null) {
                return null;
            }
            pluginIdToInfoMap.remove(pl.getId());
        }
        return pl;
    }

    /**
     * 加载指定插件或指定目录下的所有插件
     * <p/>
     * 都使用文件名作为Id
     *
     * @param pluginSrcDirFile - apk或apk目录
     * @return 插件集合
     * @throws Exception
     */
    public Collection<PlugInfo> loadPlugin(final File pluginSrcDirFile)
            throws Exception {
        checkInit();
        if (pluginSrcDirFile == null || !pluginSrcDirFile.exists()) {
            Log.e(tag, "invalidate plugin file or Directory :"
                    + pluginSrcDirFile);
            return null;
        }
        if (pluginSrcDirFile.isFile()) {
            // 如果是文件则尝试加载单个插件，暂不检查文件类型，除apk外，以后可能支持加载其他类型文件,如jar
            PlugInfo one = loadPluginWithId(pluginSrcDirFile, null, null);
            return Collections.singletonList(one);
        }
        // clear all first
        synchronized (this) {
            pluginPkgToInfoMap.clear();
            pluginIdToInfoMap.clear();
        }
        File[] pluginApks = pluginSrcDirFile.listFiles(this);
        if (pluginApks == null || pluginApks.length < 1) {
            throw new FileNotFoundException("could not find plugins in:"
                    + pluginSrcDirFile);
        }
        for (File pluginApk : pluginApks) {
            PlugInfo plugInfo = buildPlugInfo(pluginApk, null, null);
            if (plugInfo != null) {
                savePluginToMap(plugInfo);
            }
        }
        return pluginIdToInfoMap.values();
    }

    private synchronized void savePluginToMap(PlugInfo plugInfo) {
        pluginPkgToInfoMap.put(plugInfo.getPackageName(), plugInfo);
        pluginIdToInfoMap.put(plugInfo.getId(), plugInfo);
    }

    // /**
    // * 单独加载一个apk <br/>
    // * 使用文件名作为插件id <br/>
    // * 目标文件也是与源文件同名
    // *
    // * @param pluginApk
    // * @return
    // * @throws Exception
    // */
    // public PlugInfo loadPlugin(File pluginApk) throws Exception {
    // return loadPluginWithId(pluginApk, null, null);
    // }

    /**
     * 单独加载一个apk
     *
     * @param pluginApk
     * @param pluginId  - 如果参数为null,则使用文件名作为插件id
     * @return
     * @throws Exception
     */
    public PlugInfo loadPluginWithId(File pluginApk, String pluginId)
            throws Exception {
        return loadPluginWithId(pluginApk, pluginId, null);
    }

    public PlugInfo loadPluginWithId(File pluginApk, String pluginId,
                                     String targetFileName) throws Exception {
        checkInit();
        PlugInfo plugInfo = buildPlugInfo(pluginApk, pluginId, targetFileName);
        if (plugInfo != null) {
            savePluginToMap(plugInfo);
        }
        return plugInfo;
    }

    private PlugInfo buildPlugInfo(File pluginApk, String pluginId,
                                   String targetFileName) throws Exception {
        PlugInfo info = new PlugInfo();
        info.setId(pluginId == null ? pluginApk.getName() : pluginId);

        File privateFile = new File(dexInternalStoragePath,
                targetFileName == null ? pluginApk.getName() : targetFileName);

        info.setFilePath(privateFile.getAbsolutePath());

        if (!pluginApk.getAbsolutePath().equals(privateFile.getAbsolutePath())) {
            copyApkToPrivatePath(pluginApk, privateFile);
        }
        String dexPath = privateFile.getAbsolutePath();
        PluginManifestUtil.setManifestInfo(context, dexPath, info);

        PluginClassLoader loader = new PluginClassLoader(dexPath,
                dexOutputPath, context.getClassLoader(), info);
        info.setClassLoader(loader);

        try {
            AssetManager am = (AssetManager) AssetManager.class.newInstance();
            am.getClass().getMethod("addAssetPath", String.class)
                    .invoke(am, dexPath);
            info.setAssetManager(am);
            Resources ctxres = context.getResources();
            Resources res = new Resources(am, ctxres.getDisplayMetrics(),
                    ctxres.getConfiguration());
            info.setResources(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (actFrom != null && actFrom.get() != null) {
            initPluginApplication(info, actFrom.get(), true);
        }
        Log.i(tag, "buildPlugInfo: " + info);
        return info;
    }

    public void initPluginApplication(final PlugInfo info, Activity actFrom)
            throws Exception {
        initPluginApplication(info, actFrom, false);
    }

    private void initPluginApplication(final PlugInfo plugin, Activity actFrom,
                                       boolean onLoad) throws Exception {
        if (!onLoad && plugin.getApplication() != null) {
            return;
        }
        final String className = plugin.getPackageInfo().applicationInfo.name;
        if (className == null) {
            if (onLoad) {
                return;
            }
            Application application = new Application();
            setApplicationBase(plugin, application);
            return;
        }
        Log.d(tag, plugin.getId() + ", ApplicationClassName = " + className);

        Runnable setApplicationTask = new Runnable() {
            public void run() {
                ClassLoader loader = plugin.getClassLoader();
                try {
                    Class<?> applicationClass = loader.loadClass(className);
                    Application application = (Application) applicationClass.newInstance();
                    setApplicationBase(plugin, application);
                    // invoke plugin application's onCreate()
                    application.onCreate();
                } catch (Throwable e) {
                    Log.e(tag, Log.getStackTraceString(e));
                }
            }
        };
        // create Application instance for plugin
        if (actFrom == null) {
            if (onLoad)
                return;
            setApplicationTask.run();
        } else {
            actFrom.runOnUiThread(setApplicationTask);
        }
    }

    private void setApplicationBase(PlugInfo plugin, Application application)
            throws Exception {

        synchronized (plugin) {
            if (plugin.getApplication() != null) {
                // set plugin's Application only once
                return;
            }
            plugin.setApplication(application);
            //
            PluginContextWrapper ctxWrapper = new PluginContextWrapper(context,
                    plugin);
            plugin.appWrapper = ctxWrapper;
            // attach
            Method attachMethod = Application.class
                    .getDeclaredMethod("attach", Context.class);
            attachMethod.setAccessible(true);
            attachMethod.invoke(application, ctxWrapper);
            //
            if (context instanceof Application) {
                ReflectionUtils.invokeMethod(context, application, "registerComponentCallbacks");
            }
            // register Receivers
            List<ResolveInfo> receivers = plugin.getReceivers();
            if (receivers != null && !receivers.isEmpty()) {
                for (ResolveInfo receiver : receivers) {
                    String receiverClassName = receiver.activityInfo.name;
                    try {
                        Class<?> rclass = plugin.getClassLoader().loadClass(
                                receiverClassName);
                        BroadcastReceiver receiverObj = (BroadcastReceiver) rclass
                                .newInstance();
                        application.registerReceiver(receiverObj,
                                receiver.filter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // TODO when uninstall plugin, should:
                    // application.unregisterReceiver(receiver);
                }
            }
        }
    }

    private void copyApkToPrivatePath(File pluginApk, File f) {
        // if (f.exists() && pluginApk.length() == f.length()) {
        // // 这里只是简单的判断如果两个文件长度相同则不拷贝，严格的做应该比较签名如 md5\sha-1
        // return;
        // }
        FileUtil.copyFile(pluginApk, f);
    }

    public File getDexInternalStoragePath() {
        return dexInternalStoragePath;
    }

    public Context getContext() {
        return context;
    }

    public PluginActivityLifeCycleCallback getPluginActivityLifeCycleCallback() {
        return pluginActivityLifeCycleCallback;
    }

    public void setPluginActivityLifeCycleCallback(
            PluginActivityLifeCycleCallback pluginActivityLifeCycleCallback) {
        this.pluginActivityLifeCycleCallback = pluginActivityLifeCycleCallback;
    }

    @Override
    public boolean accept(File pathname) {
        if (pathname.isDirectory()) {
            return false;
        }
        String fname = pathname.getName();
        return fname.endsWith(".apk");
    }

}
