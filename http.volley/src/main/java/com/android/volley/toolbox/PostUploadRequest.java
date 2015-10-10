package com.android.volley.toolbox;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.LoadingListener;

import android.util.Log;

public abstract class PostUploadRequest extends Request<String> {
	private String BOUNDARY = "--------------520-13-14"; // 数据分隔线
	private String MULTIPART_FORM_DATA = "multipart/form-data";
	private Response.Listener<String> listener;

	public PostUploadRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
		super(Method.POST, url, errorListener);
		this.listener = listener;
		setShouldCache(false);
		// 设置请求的响应事件，因为文件上传需要较长的时间，所以在这里加大了，设为5秒
		setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
	}

	public PostUploadRequest(String url, Response.Listener<String> listener, ErrorListener errorListener,
			LoadingListener loadingListener) {
		super(Method.POST, url, errorListener, loadingListener);
		this.listener = listener;
		setShouldCache(false);
		// 设置请求的响应事件，因为文件上传需要较长的时间，所以在这里加大了，设为5秒
		setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
	}

	/**
	 * 回调正确的数据
	 * 
	 * @param response
	 *            The parsed response returned by
	 */
	@Override
	protected void deliverResponse(String response) {
		if (listener != null) {
			listener.onResponse(response);
		}
	}

	/**
	 * 这里开始解析数据
	 * 
	 * @param response
	 *            Response from the network
	 * @return
	 */
	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		try {
			String mString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			Log.v("zgy", "====mString===" + mString);
			return Response.success(mString, HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		}
	}

	public abstract Map<String, File> getFilepPrams();

	// 普通字符串数据
	private void writeStringParams() throws Exception {
		Set<String> keySet = getParams().keySet();
		for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
			String name = it.next();
			String value = getParams().get(name);

			StringBuffer sb = new StringBuffer();
			sb.append("--" + BOUNDARY + "\r\n");
			sb.append("Content-Disposition: form-data; name=\"" + name + "\"\r\n");
			sb.append("\r\n");
			sb.append(encode(value));
			sb.append("\r\n");
			bos.write(sb.toString().getBytes("utf-8"));
		}
	}

	// 文件数据
	private void writeFileParams() throws Exception {
		Set<String> keySet = getFilepPrams().keySet();
		for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
			String name = it.next();
			File value = getFilepPrams().get(name);

			StringBuffer sb = new StringBuffer();
			sb.append("--" + BOUNDARY + "\r\n");
			sb.append("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + encode(value.getName())
					+ "\"\r\n");
			sb.append("Content-Type: " + getContentType(value) + "\r\n");
			sb.append("\r\n");
			bos.write(sb.toString().getBytes("utf-8"));
			bos.write(getBytes(value));
			bos.write("\r\n".getBytes("utf-8"));
		}
	}

	// 获取文件的上传类型，图片格式为image/png,image/jpg等。非图片为application/octet-stream
	private String getContentType(File f) throws Exception {
		String type = MimeKit.getFileType(f);
		if (type == null)
			// return "application/x-www-form-urlencoded";
			return "application/octet-stream";
		return type;
	}

	// 把文件转换成字节数组
	private byte[] getBytes(File f) throws Exception {
		FileInputStream in = new FileInputStream(f);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int n;
		while ((n = in.read(b)) != -1) {
			out.write(b, 0, n);
		}
		in.close();
		return out.toByteArray();
	}

	// 添加结尾数据
	private void paramsEnd() throws Exception {
		bos.write(getBytes("--" + BOUNDARY + "--" + "\r\n"));
		bos.write(getBytes("\r\n"));
	}

	private byte[] getBytes(String s) {
		try {
			return s.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 对包含中文的字符串进行转码，此为UTF-8。服务器那边要进行一次解码
	private String encode(String value) throws Exception {
		return URLEncoder.encode(value, "UTF-8");
	}

	// 最大2M缓存
	ByteArrayOutputStream bos = new ByteArrayOutputStream();

	@Override
	public byte[] getBody() throws AuthFailureError {
		if (getFilepPrams().size() == 0) {
			return super.getBody();
		}
		try {
			writeFileParams();
			writeStringParams();
			paramsEnd();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bos.toByteArray();
	}

	@Override
	public String getBodyContentType() {
		return MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY;
	}

}
