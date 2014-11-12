package com.xy.xypay;

import com.xy.xypay.bean.PayArgs;
import com.xy.xypay.inters.XYPayCallback;
import com.xy.xypay.utils.StringUtils;
import com.xy.xypay.utils.XYPaySDK;

import android.app.Activity;
import android.os.Bundle;

public class PayAcceptedActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		PayArgs payArgs= (PayArgs) this.getIntent().getSerializableExtra("xypay");
		//初始化SDK
				XYPaySDK.initSDK(getApplicationContext(), "lol", true);
				//注册支付的回调接口
				XYPaySDK.setOnPayCallback(new XYPayCallback() {
					@Override
					public void onPayFinished(int status) {
						switch (status) {
						case XYPaySDK.XYPay_RESULT_CODE_CANCEL://支付取消
							StringUtils.printLog(true, "getPayStatus"+status,"支付取消");
							finish();
							break;
						case XYPaySDK.XYPay_RESULT_CODE_SUCCESS://支付成功
							StringUtils.printLog(true, "getPayStatus"+status,"支付成功");
							finish();
							break;
						case XYPaySDK.XYPay_RESULT_CODE_FAILURE://支付失败
							StringUtils.printLog(true, "getPayStatus"+status,"支付失败");
							finish();
							break;
						}
					}
				});
				//初始化测试数据
				PayArgs payArg = new PayArgs();
			 
				payArg.platform ="lol";
				payArg.sid = "1";
				payArg.pay_rmb = "6";
				payArg.imei = "99000316884451";
				payArg.resource_id = "1123107";
				
				payArg.token = "d174ebf344be3713a1c8b5fe722ae872";
				payArg.openuid = "a19fa46e795097c3bc458e88fc88aa9a";
				
				//以下参数是充值卡用到的1移动,2联通,3电信
				payArg.pay_type = "1";
				payArg.card_id = "";//car no
				payArg.card_pass = "";//card pwd
				
				//初始支付对象
				XYPaySDK.initPayArgs(payArg);
				//跳转到xy支付中心
				XYPaySDK.jumpToXYPayCenter(getApplicationContext());
				
		
	}

}
