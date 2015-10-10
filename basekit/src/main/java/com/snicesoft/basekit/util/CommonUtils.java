package com.snicesoft.basekit.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

public class CommonUtils {
	public static void openActivity(Context context, Class<?> clazz,
			Bundle... bundles) {
		Intent intent = new Intent(context, clazz);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (bundles != null && bundles.length > 0) {
			intent.putExtras(bundles[0]);
		}
		context.startActivity(intent);
	}

	public static void showToast(Context context, int resId, int... duration) {
		if (duration.length > 0)
			showToast(context, context.getString(resId), duration[0]);
		else
			showToast(context, context.getString(resId));
	}

	public static void showToast(Context context, String tip, int... duration) {
		int du = Toast.LENGTH_SHORT;
		if (duration.length > 0)
			du = duration[0];
		Toast.makeText(context, tip, du).show();
	}

	/**
	 * 检查是否存在SDCard
	 * 
	 * @return
	 */
	public static boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}
}
