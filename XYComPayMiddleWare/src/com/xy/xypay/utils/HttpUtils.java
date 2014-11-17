package com.xy.xypay.utils;

import java.io.*;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;

/**
 * TODO 网络连接工具类
 *
 * @author pengsk
 * @data: 2014年11月10日 下午4:33:44
 * @version: V1.0
 */
public class HttpUtils {

    public static final String resourceidMessageUrl = "http://cn.passport.xyandroid.com/inapi/gamePayInfo/";

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
                .getSystemService(Context.CONNECTIVITY_SERVICE);

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
     * @param path    连接的完整url
     * @param isDebug 是否是debug状态
     * @return String json数据 包含是否成功等 订单号，渠道跳转url 登录是否过期
     */
    public static String doGetRequest(String path, boolean isDebug) {
        String result = "";
        try {
            if (!StringUtils.isEmpty(path)) {
                URL url = new URL(path);
                StringUtils.printLog(true, "doGetRequest()访问Url",
                        "response url= " + url);
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
                    for (int b = 0; (b = inputStream.read()) != -1; )
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

    /**
     * TODO post请求获取resouceid。兑换比例游戏币名称等。jsON数据
     *
     * @param path    基本url
     * @param isDebug 是否是调试模式
     * @param args    传入的参数
     * @return JSON返回的参数
     */
    public static String doPostRequest(String path, boolean isDebug, Map<String, String> args) {
        String result = "";
        try {
            if (!StringUtils.isEmpty(path)) {
                Set<Map.Entry<String, String>> entries = null;
                Iterator<Map.Entry<String, String>> iterator = null;
                if (StringUtils.isEmpty(path))
                    return null;
                if (args == null || args.size() == 0)
                    return null;
                entries = args.entrySet();
                iterator = entries.iterator();
                java.util.Map.Entry<String, String> entry;
                List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
                while (iterator.hasNext()) {
                    entry = iterator.next();
                    BasicNameValuePair pair1 = new BasicNameValuePair(entry.getKey(), entry.getValue());
                    pairs.add(pair1);
                }

                HttpEntity entity = null;
                // 创建客户端对象
                HttpClient client = new DefaultHttpClient();
                // 设置连接属性
                client.getParams().setParameter(
                        CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
                // 创建请求对象
                HttpUriRequest request = null;
                request = new HttpPost(path);

                if (pairs != null && !pairs.isEmpty()) {
                    UrlEncodedFormEntity reqEntity = new UrlEncodedFormEntity(
                            pairs);
                    ((HttpPost) request).setEntity(reqEntity);
                }// 执行请求获得响应对象
                HttpResponse response = client.execute(request);
                // 判断响应码获取 实体对象
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    entity = response.getEntity();
                }
                if (entity != null) {
                    // 本地页数加1
                    // 获得响应实体
                    InputStream is = entity.getContent();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(is, "UTF-8"));
                    StringBuilder builder = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        // 读取响应数据
                        builder.append(line);
                    }
                    result = builder.toString().trim();

                }

            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
////		if(isDebug){
//			result="{\"ret\":0,\"msg\":0,\"data\":{\"resource_id\":\"1123107\",\"rmb_ratio\":\"100\",\"game_currency_name\":\"元宝\"}}";
////		}
        return result;
    }
}
