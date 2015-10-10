/*
 * Copyright (c) 2013. wyouflf (wyouflf@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lidroid.xutils.util;

import java.io.File;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

/**
 * Created by wyouflf on 13-8-30.
 */
public class OtherUtils {
	private OtherUtils() {
	}

	/**
	 * @param context
	 * @param dirName
	 *            Only the folder name, not full path.
	 * @return app_cache_path/dirName
	 */
	public static String getDiskCacheDir(Context context, String dirName) {
		String cachePath = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			File externalCacheDir = context.getExternalCacheDir();
			if (externalCacheDir != null) {
				cachePath = externalCacheDir.getPath();
			}
		}
		if (cachePath == null) {
			File cacheDir = context.getCacheDir();
			if (cacheDir != null && cacheDir.exists()) {
				cachePath = cacheDir.getPath();
			}
		}

		return cachePath + File.separator + dirName;
	}

	@SuppressWarnings("deprecation")
	public static long getAvailableSpace(File dir) {
		try {
			final StatFs stats = new StatFs(dir.getPath());
			return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
		} catch (Throwable e) {
			return -1;
		}

	}

	private static SSLSocketFactory sslSocketFactory;

	@SuppressWarnings("deprecation")
	public static void trustAllHttpsURLConnection() {
		// Create a trust manager that does not validate certificate chains
		if (sslSocketFactory == null) {
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				@Override
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };
			try {
				SSLContext sslContext = SSLContext.getInstance("TLS");
				sslContext.init(null, trustAllCerts, null);
				sslSocketFactory = sslContext.getSocketFactory();
			} catch (Throwable e) {
			}
		}

		if (sslSocketFactory != null) {
			HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
			HttpsURLConnection
					.setDefaultHostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		}
	}
}
