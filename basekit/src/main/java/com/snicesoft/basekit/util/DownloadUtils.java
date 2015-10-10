package com.snicesoft.basekit.util;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

/**
 * @author zhu zhe
 * @since 2015年3月5日 下午10:40:21
 * @version V1.0
 */
@SuppressLint("NewApi")
public class DownloadUtils {
	private static String dirType = "snicesoft";

	/**
	 * 使用系统下载器下载文件
	 * 
	 * @param context
	 *            上下文
	 * @param httpFile
	 *            网络地址
	 * @param subType
	 *            子文件目录
	 * @param fileName
	 *            文件后缀名
	 */
	public static void download(Context context, String httpFile,
			String subType, String fileName) {
		DownloadManager downloadManager = (DownloadManager) context
				.getSystemService(Context.DOWNLOAD_SERVICE);
		Uri uri = Uri.parse(httpFile);
		Request request = new Request(uri);
		request.setAllowedNetworkTypes(Request.NETWORK_MOBILE
				| Request.NETWORK_WIFI);
		File file = new File(Environment.getExternalStorageDirectory() + "/"
				+ dirType + "/" + subType + "/");
		if (!file.exists()) {
			file.mkdirs();
		}
		String subPath = subType + "/" + fileName;
		request.setDestinationInExternalPublicDir(dirType, subPath);
		request.setVisibleInDownloadsUi(true);
		request.setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		downloadManager.enqueue(request);
		Toast.makeText(context, "正在下载", Toast.LENGTH_SHORT).show();
	}
}
