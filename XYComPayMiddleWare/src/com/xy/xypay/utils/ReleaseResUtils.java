package com.xy.xypay.utils;

import android.app.Dialog;
import android.os.Handler;
import android.view.View;


/**
 * @time	2014年8月4日19:13:11
 * @author ruanwei
 * @detail
 * 		释放资源工具类 
 *
 */
public class ReleaseResUtils {
//
	
	/**  
	 * TODO 释放按钮点击监听。
	 *  @param view
	 * @throw 
	 * @return void 
	 */
	public static void releaseListener(View view){
		if(view != null){
			view.setOnClickListener(null);
			view = null;
		}
	}
	
	public static void dismiss(Dialog dialog){
		if (dialog != null){
			dialog.dismiss();
			dialog = null;
		}
	}
	//释放handler
	
	/**  
	 * TODO
	 *  @param handler
	 * @throw 
	 * @return void 
	 */
	public static void releaseHandler(Handler handler){
		if (handler != null){
			handler.removeCallbacksAndMessages(null);
			handler = null;
		}
	}
	
}
