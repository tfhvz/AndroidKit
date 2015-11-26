package com.snicesoft.pluginkit;

/**
 * Created by zhuzhe on 15/11/24.
 */
public class Globals {
    /**
     * 私有目录中保存插件文件的文件夹名
     */
    public static final String PRIVATE_PLUGIN_OUTPUT_DIR_NAME = "plugins-file";

    /**
     * 私有目录中保存插件odex的文件夹名
     */
    public static final String PRIVATE_PLUGIN_ODEX_OUTPUT_DIR_NAME = "plugins-opt";
    /**
     * Activity来自插件的标志
     */
    public static final String PLUGIN_ID = "_pluginId";
    public static final String PLUGIN_ACTIVITY = "_targetAct";

    public static Class<?> getPluginTargetActivity() {
        return DynamicActivity.class;
    }
}
