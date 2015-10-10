package com.snicesoft.basekit.util;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.widget.Toast;

public class UpdateUtil {
	@SuppressLint("NewApi")
	public static void showDialog(final Context context, String message,
			final String httpApkPath) {
		File file = new File(Environment.getExternalStorageDirectory()
				+ "/snicesoft/apk/");
		if (!file.exists()) {
			file.mkdirs();
		}
		Builder builder = null;
		if (android.os.Build.VERSION.SDK_INT < 11) {
			builder = new Builder(context);
		} else {
			builder = new Builder(context, 0x3);
		}
		builder.setTitle("软件更新");
		builder.setMessage("更新说明:\r\n" + message);
		builder.setNegativeButton("更新", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (CommonUtils.hasSdcard()) {
					DownloadUtils.download(context, httpApkPath, "apk",
							"snicesoft.apk");
				} else {
					Toast.makeText(context, "请插入SD卡，才能下载", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		builder.setPositiveButton("暂不更新",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		builder.create().show();
	}
}
