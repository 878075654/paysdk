package com.xy.xypay.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.http.protocol.HTTP;

import android.app.Dialog;
import android.util.Log;

import com.xy.xypay.XYPayCenterActivity;
import com.xy.xypay.alipay.Keys;
import com.xy.xypay.bean.PayArgs;

/** 
 * TODO 支付工具类 
 * @author  pengsk 
 * @data:  2014年11月10日 下午2:38:15 
 * @version:  V1.0 
 */

public class XYPayUtiles {
	private static boolean isDebug;

	public XYPayUtiles() {
		isDebug = XYPayCenterActivity.getIsDebug();
	}

	/**
	 * TODO<支付宝支付相关辅助方法 得到订单>
	 * 
	 * @param price
	 *            价格
	 * @param args
	 *            支付宝访问时的提交参数
	 * @return
	 * @throw
	 * @return String
	 */
	public static String getNewOrderInfo(String price, PayArgs payArgs) {
		
		String args=genAlipayArgs(payArgs);
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(getOutTradeNo());
		sb.append("\"&subject=\"");
		sb.append("购买恺英网络科技的游戏道具");
		sb.append("\"&body=\"");
		sb.append("恺英网络科技的游戏充值商品");
		sb.append("\"&total_fee=\"");
		sb.append(price == null ? "" : price);
		sb.append("\"&notify_url=\"");
		// 网址需要做URL编码
		String temp = "http://dev3.pay.xy.com/index.php?action=alipaymobile/appnotify"
				+ args;
		Log.e("支付宝回调通知地址", temp);
		try {
			sb.append(URLEncoder.encode(
					"http://dev3.pay.xy.com/index.php?action=alipaymobile/appnotify"
							+ args, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"UTF-8");
		sb.append("\"&return_url=\"");
		try {
			sb.append(URLEncoder.encode("http://m.alipay.com", HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);
		sb.append("\"");

		return new String(sb);
	}

	
	
	/**  
	 * TODO< 生成流水号> 
	 *  @return 流水号
	 * @throw 
	 * @return String 
	 */
	public static String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		java.util.Random r = new java.util.Random();
		key += r.nextInt();
		key = key.substring(0, 15);
		StringUtils.printLog(isDebug, "生成的流水号", "outTradeNo: " + key);
		return key;
	}

	/**
	 * 把PayArgs对象转换为map对象然后生成网络get请求的参数
	 * 
	 * @param payArgs 游戏提交的参数类
	 * @return
	 */
	// 把PayArgs对象转换为map对象然后生成网络get请求的参数
	public static Map<String, String> changePayArgs2Map(PayArgs payArgs) {
		Map<String, String> map = new TreeMap<String, String>();
		map.put("resource_id", payArgs.resource_id);
		map.put("taocan_id", payArgs.taocan_id);
		map.put("pay_rmb", payArgs.pay_rmb);
		map.put("jump_url", payArgs.jump_url);
		map.put("callback_url", payArgs.callback_url);
		map.put("sid", payArgs.sid);
		map.put("openuid", payArgs.openuid == null ? "" : payArgs.openuid);
		map.put("token", payArgs.token == null ? "" : payArgs.token);
		map.put("platform", payArgs.platform == null ? "" : payArgs.platform);
		map.put("pay_type", payArgs.pay_type);
		map.put("card_id", payArgs.card_id);
		map.put("card_pass", payArgs.card_pass);
		map.put("imei", payArgs.imei);
		map.put("bindid", payArgs.bindid);
		// 添加的额外参数
		map.put("appExtra1", payArgs.appExtra1);
		map.put("appExtra2", payArgs.appExtra2);
		StringUtils.printLog(isDebug, "XYPayCenterActivity付款对象转换为map",
				map.toString());
		return map;
	}
	
	/**
	 * 
	 * TODO 获得完整的http链接付款地址
	 *  @param payArgs 游戏提交的参数类
	 *  @param path 支付地址
	 *  @param payType 支付方式
	 *  @return
	 * @throw 
	 * @return String 获得完整的http链接付款地址
	 */
	
	public static String genHttpPath(PayArgs payArgs, String path,
			String payType) {
		Map<String, String> args=changePayArgs2Map(payArgs);
		StringBuffer sb = new StringBuffer();
		sb.append(path).append(payType);

		Set<Map.Entry<String, String>> entries = null;
		Iterator<Map.Entry<String, String>> iterator = null;
		if (StringUtils.isEmpty(path) || StringUtils.isEmpty(payType))
			return null;
		if (args == null || args.size() == 0)
			return null;
		entries = args.entrySet();
		iterator = entries.iterator();
		sb.append("&");
		java.util.Map.Entry<String, String> entry;
		while (iterator.hasNext()) {
			entry = iterator.next();
			sb.append(entry.getKey()).append("=").append(entry.getValue())
					.append("&");
		}
		if (sb.lastIndexOf("&") == sb.length() - 1)
			sb.deleteCharAt(sb.length() - 1);

		StringUtils.printLog(isDebug, "加密后的签名",
				StringUtils.md5(genSignature(args)));
		// 添加签名的认证字符串
		sb.append("&").append("sign=")
				.append(StringUtils.md5(genSignature(args)));

		StringUtils.printLog(isDebug, "完整的http链接付款地址", sb.toString().trim());
		return sb.toString().trim();
	}

	// 生成支付宝访问时的提交参数
	
	/**  
	 * TODO  生成支付宝访问时的提交参数
	 *  @param args s
	 *  @return
	 * @throw 
	 * @return String  生成支付宝访问时的提交参数
	 */
	public static String genAlipayArgs(PayArgs payArgs) {
		
		Map<String, String> args=changePayArgs2Map( payArgs);
		
		StringBuffer sb = new StringBuffer();
		Set<Map.Entry<String, String>> entries = null;
		Iterator<Map.Entry<String, String>> iterator = null;
		if (args == null || args.size() == 0)
			return null;
		entries = args.entrySet();
		iterator = entries.iterator();
		java.util.Map.Entry<String, String> entry;
		sb.append("&").append("taocan_id=" + "").append("&");
		while (iterator.hasNext()) {
			entry = iterator.next();
			if (entry.getKey().equals("sid")) {
				sb.append(entry.getKey()).append("=").append(entry.getValue())
						.append("&");
			} else if (entry.getKey().equals("openuid")) {
				sb.append(entry.getKey()).append("=").append(entry.getValue())
						.append("&");
			} else if (entry.getKey().equals("resource_id")) {
				sb.append(entry.getKey()).append("=").append(entry.getValue())
						.append("&");
			} else {
				continue;
			}
		}
		if (sb.lastIndexOf("&") == sb.length() - 1)
			sb.deleteCharAt(sb.length() - 1);
		StringUtils.printLog(isDebug, "支付宝访问时的提交参数", sb.toString().trim());
		return sb.toString().trim();
	}

	// 生成参数签名
	
	/**  
	 * TODO  生成参数签名
	 *  @param args PayArgs变成map形式
	 *  @return
	 * @throw 
	 * @return String 加密前的支付参数
	 */
	public static String genSignature(Map<String, String> args) {
		Set<Map.Entry<String, String>> entries = null;
		Iterator<Map.Entry<String, String>> iterator = null;
		List<String> list = null;
		StringBuffer sb = new StringBuffer("582df15de91b3f12d8e710073e43f4f8");
		if (args == null || args.size() == 0)
			return null;
		entries = args.entrySet();
		iterator = entries.iterator();
		Map.Entry<String, String> entry = null;
		list = new ArrayList<String>();
		while (iterator.hasNext()) {
			entry = iterator.next();
			if (!((String) entry.getKey()).equals("resource_id")) {
				if (entry.getKey().equals("callback_url")) {
					continue;
				} else {
					list.add(entry.getValue());
				}
			}
		}
		Collections.sort(list);
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i));
		}
		String temp = sb.toString();
		StringUtils.printLog(isDebug, "genSignature()方法中", "加密前的支付参数" + temp);
		return temp;
	}
	
}
