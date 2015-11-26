package com.snicesoft.pluginkit.env;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.util.Log;

import com.snicesoft.pluginkit.Globals;
import com.snicesoft.pluginkit.PluginManager;

/**
 * Created by zhuzhe on 15/11/24.
 */
public class FrameworkInstrumentation extends Instrumentation {
    private static final String tag = "FrameworkInstrumentation";

    @Override
    public Activity newActivity(ClassLoader cl, String className,
                                Intent intent) throws InstantiationException,
            IllegalAccessException, ClassNotFoundException {
        if (className.equals(Globals.getPluginTargetActivity())) {
            String pluginId = intent
                    .getStringExtra(Globals.PLUGIN_ID);
            String actClassName = intent
                    .getStringExtra(Globals.PLUGIN_ACTIVITY);
            PlugInfo plugin = PluginManager.getInstance().getPluginById(pluginId);
            if (plugin == null) {
                plugin = PluginManager.getInstance().getPluginByPackageName(pluginId);
            }
            Log.d(tag, "Instrumentation.newActivity():pluginId=" + pluginId + ", actClassName=" + actClassName + ", plugin = " + plugin);
            if (plugin != null) {
                return (Activity) plugin.getClassLoader().loadActivityClass(actClassName).newInstance();
            }
        }
        return super.newActivity(cl, className, intent);
    }
}
