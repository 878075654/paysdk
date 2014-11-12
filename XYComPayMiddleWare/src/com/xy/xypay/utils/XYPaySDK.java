package com.xy.xypay.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.xy.xypay.XYPayCenterActivity;
import com.xy.xypay.bean.PayArgs;
import com.xy.xypay.inters.XYPayCallback;

/**
 * SDK应用环境设置
 * @author ruanwei
 *
 */
public class XYPaySDK {

	private static PayArgs payArgs;

	/**
	 * 支付成功的状态码值*/
	public static final int XYPay_RESULT_CODE_SUCCESS = 5000 ;
	/**
	 * 支付失败的状态码值
	 */
	public static final int XYPay_RESULT_CODE_FAILURE = 5001;
	
	/**
	 * 支付取消的状态码值
	 */
	public static final int XYPay_RESULT_CODE_CANCEL = 5002;
	
	
	private XYPaySDK(){};
	/** 初始化sdk应用环境,默认为测试环境
	 * @param context	应用程序上下文
	 * @param isDebug	是否使用debug模式
	 * @param platform	接入的游戏平台的参数
	 */
	public static void initSDK(Context context,String platform,boolean isDebug){
		SharedPreferences sharedPreferences =context.getSharedPreferences("settings", Context.MODE_PRIVATE); 
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean("isDebug", isDebug);
		editor.putString("platform", platform);
		editor.commit();
	}
	
	/**
	 * 设置应用环境
	 * @param context 应用程序上下文
	 * @param isDebug true为测试环境
	 */
	public static void setDebug(Context context,boolean isDebug){
		SharedPreferences sharedPreferences =context.getSharedPreferences("settings", Context.MODE_PRIVATE); 
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean("isDebug", isDebug);
		editor.commit();
	}
	/**
	 * 初始化游戏购买参数
	 * @param payParamsBean	初始化游戏购买参数
	 */
	public static void initPayArgs(PayArgs payArg) {
		if (payArg != null)
			 payArgs = payArg;
	}

	/**
	 * 得到初始化后的游戏购买参数
	 * @return	返回初始化话后的游戏购买参数Bean
	 */
	public static PayArgs getPayAgs() {
		return payArgs;
	}

	/**
	 * 调用完initPayArgs()方法后，调用此方法跳转到支付中心
	 * @param context	 应用程序上下文
//	 */
	public static void jumpToXYPayCenter(Context context){
		Intent intent = new Intent(context, XYPayCenterActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	/**
	 * 注册支付完成后的回调接口
	 * @param payResultCallback 支付完成的回调接口
	 */
	public static void setOnPayCallback(XYPayCallback payCallback){
		XYPayCenterActivity.mPayCallback = payCallback;
	}
 
 
}
