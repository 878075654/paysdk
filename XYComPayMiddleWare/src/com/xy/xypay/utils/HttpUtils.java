package com.xy.xypay.utils;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.http.client.ClientProtocolException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * TODO 网络连接工具类
 * 
 * @author pengsk
 * @data: 2014年11月10日 下午4:33:44
 * @version: V1.0
 */
public class HttpUtils {

	private HttpUtils() {
	}

	//

	/**
	 * TODO 判断网络是否可用，
	 * 
	 * @param context
	 * @return boolean
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService("connectivity");
		if (connectivity == null)
			return false;
		NetworkInfo info = connectivity.getActiveNetworkInfo();
		if (info == null || !info.isConnected())
			return false;
		return info.getState() == android.net.NetworkInfo.State.CONNECTED;
	}

	//
	/**
	 * TODO 使用get方式连接网络获取返回值，
	 * 
	 * @param path
	 *            连接的完整url
	 * @param isDebug
	 *            是否是debug状态
	 * @return String json数据 包含是否成功等 订单号，渠道跳转url 登录是否过期
	 */
	public static String doGetRequest(String path, boolean isDebug) {
		String result = "";
		try {
			if (!StringUtils.isEmpty(path)) {
				URL url = new URL(path);
				HttpURLConnection httpURLConnection = (HttpURLConnection) url
						.openConnection();
				httpURLConnection.setRequestMethod("GET");
				httpURLConnection.setConnectTimeout(10000);
				httpURLConnection.setReadTimeout(10000);
				httpURLConnection.setDoOutput(true);
				ByteArrayOutputStream by = new ByteArrayOutputStream();
				InputStream inputStream = null;
				int responseCode = httpURLConnection.getResponseCode();
				StringUtils.printLog(isDebug, "doGetRequest()访问网络状态码",
						"responseCode=" + responseCode);
				if (responseCode == 200) {
					inputStream = httpURLConnection.getInputStream();
					for (int b = 0; (b = inputStream.read()) != -1;)
						by.write(b);
					by.flush();
					byte bb[] = by.toByteArray();
					result = new String(bb, 0, bb.length);
					return result;
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
