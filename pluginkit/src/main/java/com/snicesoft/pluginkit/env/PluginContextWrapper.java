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
package com.snicesoft.pluginkit.env;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import com.snicesoft.pluginkit.PluginPackageManager;

/**
 * Plugin Context 包装类
 * 
 * @author HouKangxi
 *
 */
@SuppressLint("NewApi")
public class PluginContextWrapper extends ContextWrapper {
	private PlugInfo plugin;
	private static final String tag = "PluginContextWrapper";
	private File fileDir;
	private PluginPackageManager pkgManager;

	public PluginContextWrapper(Context base, PlugInfo plugin) {
		super(base);
		this.plugin = plugin;
		getApplicationInfo().sourceDir = plugin.getFilePath();
		getApplicationInfo().dataDir = ActivityOverider.getPluginBaseDir(
				plugin.getId()).getAbsolutePath();
		fileDir = new File(ActivityOverider.getPluginBaseDir(plugin.getId())
				.getAbsolutePath() + "/files/");
		pkgManager = new PluginPackageManager(base.getPackageManager());
	}

	@Override
	public File getFilesDir() {
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		return fileDir;
	}

	@Override
	public PackageManager getPackageManager() {
		Log.d(tag, "PackageManager()");
		return pkgManager;
	}

	@Override
	public Context getApplicationContext() {
		Log.v(tag, "getApplicationContext()");
		return plugin.getApplication();
	}

	@Override
	public String getPackageName() {
		Log.d(tag, "getPackageName()");
		return plugin.getPackageName();
	}

	@Override
	public Resources getResources() {
		Log.d(tag, "getResources()");
		return plugin.getResources();
	}

	@Override
	public AssetManager getAssets() {
		Log.d(tag, "getAssets()");
		return plugin.getAssetManager();
	}
}