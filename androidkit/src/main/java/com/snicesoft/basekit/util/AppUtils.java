package com.snicesoft.basekit.util;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

public class AppUtils {
	/**
	 * 获得版本号
	 */
	public static int getVersionCode(Context context) {
		int verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			verCode = -1;
		}
		return verCode;
	}

	public static String getVersion(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			verName = "";
		}
		return verName;
	}

	public static void installApk(Context context, String filename) {
		File file = new File(filename);
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		String type = "application/vnd.android.package-archive";
		intent.setDataAndType(Uri.fromFile(file), type);
		context.startActivity(intent);
	}
}
