package com.snicesoft.basekit.util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;

@SuppressLint("NewApi")
public class DialogUtil {
	public static AlertDialog.Builder getAlertBuilder(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		if (android.os.Build.VERSION.SDK_INT > 10)
			builder = new AlertDialog.Builder(context, 0x3);
		return builder;
	}

	public static ProgressDialog getProgressDialog(Context context) {
		ProgressDialog progressDialog = new ProgressDialog(context);
		if (android.os.Build.VERSION.SDK_INT > 10)
			progressDialog = new ProgressDialog(context, 0x3);
		return progressDialog;
	}
}
