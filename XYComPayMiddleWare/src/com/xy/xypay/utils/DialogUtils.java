/*
 * 
 */
package com.xy.xypay.utils;

import com.xy.xypay.inters.XYPayCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/** 
 * TODO toast 加载进度dialog 退出支付dialog 工具类   
 * @author  pengsk 
 * @data:  2014年11月10日 下午4:14:12 
 * @version:  V1.0 
 */
public class DialogUtils {
	private static Toast mToast;
	public static void showToast(Context context, String info) {
		if (mToast != null) {
			mToast.cancel();
		}
		mToast = Toast.makeText(context, info, Toast.LENGTH_SHORT);
		mToast.show();
	}
	/** 
     * 得到自定义的progressDialog,用来显示网络请求的加载动画
     * @param context 
     * @param msg 加载信息
     * @return 
     */  
    public static Dialog createLoadingDialog(Context context, String msg) {  
        LayoutInflater inflater = LayoutInflater.from(context);  
        View v = inflater.inflate(ResourceUtil.getLayoutId(context, "loading_dialog"), null);// 得到加载view  
        RelativeLayout layout = (RelativeLayout) v.findViewById(ResourceUtil.getId(context, "dialog_view"));// 加载布局  
        // main.xml中的ImageView  
        ImageView spaceshipImage = (ImageView) v.findViewById(ResourceUtil.getId(context,"img"));  
        TextView tipTextView = (TextView) v.findViewById(ResourceUtil.getId(context, "tipTextView"));// 提示文字  
        // 加载动画  
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(  
                context, ResourceUtil.getAnimId(context, "loading_animation"));  
        // 使用ImageView显示动画  
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);  
        if (!StringUtils.isEmpty(msg)){
        	tipTextView.setText(msg);// 设置加载信息  
        }
        Dialog loadingDialog = new Dialog(context, ResourceUtil.getStyleId(context, "loading_dialog"));// 创建自定义样式dialog  
        loadingDialog.setCancelable(true);// 不可以用“返回键”取消  
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(  
                LinearLayout.LayoutParams.MATCH_PARENT,  
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局  
        return loadingDialog;  
    } 
    
    /**  
     * TODO 弹出确认退出的对话框
     *  @param context 当前activity
     *  @param object  支付的回调
     *  @param status 支付状态码
     *  @return
     * @throw 
     * @return Dialog 
     */
    public static Dialog createConfirmExitDialog(final Activity context,final Object object,final int status){
		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
		// 弹出退出警告框
		View view = LayoutInflater.from(context).inflate(
							ResourceUtil.getLayoutId(context,"dialog_alert_exit"), null);
		alertBuilder.setView(view);
		final AlertDialog alertDialog =  alertBuilder.create();
		//退出按钮
		Button exitCancelBtn = (Button) view.findViewById(ResourceUtil.getId(context, "exit_btn_cancel"));
		exitCancelBtn.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							alertDialog.dismiss();
						}
					});
		Button exitCertainBtn = (Button) view.findViewById(ResourceUtil.getId(context,"exit_btn_certain"));
		exitCertainBtn.setOnClickListener(new View.OnClickListener(){
						@Override
						public void onClick(View v) {
							alertDialog.dismiss();
							//如果是回调接口调用回调接口的结束支付方法
							if (object instanceof XYPayCallback){
								((XYPayCallback)object).onPayFinished(status);
							}
							context.finish();
						}
		});
		return alertDialog;
	}
}
