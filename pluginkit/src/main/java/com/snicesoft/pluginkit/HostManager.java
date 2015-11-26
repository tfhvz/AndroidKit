package com.snicesoft.pluginkit;

import android.content.Context;
import android.content.res.Resources;

/**
 * 宿主管理类
 * 
 * @author zhe
 *
 */
public class HostManager {
	/**
	 * 获取宿主Resources
	 * 
	 * @return
	 */
	public static Resources getHostResources() {
		return getHostContext().getResources();
	}

	/**
	 * 获取宿主Context
	 * 
	 * @return
	 */
	public static Context getHostContext() {
		if (PluginManager.getInstance() != null)
			return PluginManager.getInstance().getContext();
		return null;
	}
}
