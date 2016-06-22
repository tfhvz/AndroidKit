package com.snicesoft.basekit.http;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
	private String url;
	private String json;
	private Map<String, File> files;
	private Map<String, String> params;
	public Map<String, String> headers;

	public HttpRequest() {
		params = new HashMap<String, String>();
		files = new HashMap<String, File>();
		headers = new HashMap<String, String>();
	}

	public HttpRequest(String url) {
		this();
		setUrl(url);
	}

	public void addHeader(String key, String value) {
		headers.put(key, value);
	}

	public void put(String key, String value) {
		params.put(key, value);
	}

	public void addFile(String key, File file) {
		files.put(key, file);
	}

	public void addFile(String key, String filePath) {
		files.put(key, new File(filePath));
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setJson(String json) {
		this.json = json;
	}

	private String encode(String s) {
		try {
			return URLEncoder.encode(s, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public void setFullUrl() {
		if (!url.contains("?")) {
			url = url + "?";
		} else {
			if (!url.endsWith("?")) {
				url = url + "&";
			}
		}
		StringBuffer buffer = new StringBuffer();
		int i = 0;
		for (String key : params.keySet()) {
			String value = params.get(key);
			if (i == params.keySet().size() - 1) {
				buffer.append(encode(key) + "=" + encode(value));
			} else {
				buffer.append(encode(key) + "=" + encode(value) + "&");
			}
			i++;
		}
		url += buffer.toString();
	}

	public String getJson() {
		return json;
	}

	public String getUrl() {
		return url;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public Map<String, File> getFiles() {
		return files;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}
}
