package com.android.volley.toolbox;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;

@SuppressLint("DefaultLocale")
public class MimeKit {
	private static Map<String, String> sFileTypeMap = new HashMap<String, String>();

	static {
		sFileTypeMap.put("JPG", "image/jpeg");
		sFileTypeMap.put("JPEG", "image/jpeg");
		sFileTypeMap.put("GIF", "image/gif");
		sFileTypeMap.put("PNG", "image/png");
		sFileTypeMap.put("BMP", "image/x-ms-bmp");
		sFileTypeMap.put("WBMP", "image/vnd.wap.wbmp");
	}

	public static String getFileType(File f) {
		int lastDot = f.getPath().lastIndexOf(".");
		if (lastDot < 0)
			return null;
		return sFileTypeMap.get(f.getPath().substring(lastDot + 1).toUpperCase());
	}
}
