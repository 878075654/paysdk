package com.xy.xypay;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import com.xy.xycompaymiddleware.R;
import com.xy.xypay.bean.PayArgs;
import com.xy.xypay.inters.XYPayCallback;
import com.xy.xypay.utils.StringUtils;
import com.xy.xypay.utils.XYPaySDK;

/**
 * 
 * TODO
 * @author  pengsk 
 * @data:  2014年11月11日 下午3:21:09 
 * @version:  V1.0
 * 
 */
public class MainActivity extends Activity{
	
	Button btnButton;
	Button testBtn;
	TextView textsuccess;
	boolean flag  = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btnButton = (Button)this.findViewById(R.id.btn);
		testBtn = (Button)this.findViewById(R.id.test);
		textsuccess=(TextView)this.findViewById(R.id.textsuccess);
		//初始化SDK
		XYPaySDK.initSDK(getApplicationContext(), true);
		//注册支付的回调接口
		XYPaySDK.setOnPayCallback(new XYPayCallback() {
			@Override
			public void onPayFinished(int status) {
				switch (status) {
				case XYPaySDK.XYPay_RESULT_CODE_CANCEL://支付取消
					StringUtils.printLog(true, "getPayStatus"+status,"支付取消");
					textsuccess.setText("getPayStatus		"+status+"			支付取消");
					break;
				case XYPaySDK.XYPay_RESULT_CODE_SUCCESS://支付成功
					StringUtils.printLog(true, "getPayStatus"+status,"支付成功");
					textsuccess.setText("getPayStatus		"+status+"			支付成功");
					break;
				case XYPaySDK.XYPay_RESULT_CODE_FAILURE://支付失败
					StringUtils.printLog(true, "getPayStatus"+status,"支付失败");
					textsuccess.setText("getPayStatus		"+status+"			支付失败");
					break;
				}
			}
		});
		
		btnButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				   //初始化测试数据
					PayArgs payArg = new PayArgs();
					payArg.gid="1";//应用app
					payArg.sid="1";//区服；
				payArg.app_callback_url="";//发货地址
				payArg.openuid="a19fa46e795097c3bc458e88fc88aa9a";//平台UID
				payArg.appExtra1="";
				payArg.appExtra2="";
				//下面是测试临时用的
//				payArg.resource_id = "1123107";//应用的appid

//					payArg.sid = "1";//区服ID
//					payArg.pay_rmb = "6";//充值金额
//					payArg.imei = "99000316884451";//设备号
//					payArg.resource_id = "1123107";//应用的appid
//
//					payArg.openuid = "a19fa46e795097c3bc458e88fc88aa9a";//平台UID（可以带竖线+角色id，例如:openuid|role_id）
//
//					//以下参数是充值卡用到的1移动,2联通,3电信
//					payArg.pay_type = "1";
//					payArg.card_id = "";//car no
//					payArg.card_pass = "";//card pwd
					
					//初始支付对象
					XYPaySDK.initPayArgs(payArg);
					//跳转到xy支付中心
					XYPaySDK.jumpToXYPayCenter(getApplicationContext());
			}
		});
		
		testBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (flag){
					testBtn.setText("正式环境");
				}else{
					testBtn.setText("测试环境");
				}
				flag = !flag;
				XYPaySDK.setDebug(getApplicationContext(), flag);
			}
		});
		
	}
	
}
