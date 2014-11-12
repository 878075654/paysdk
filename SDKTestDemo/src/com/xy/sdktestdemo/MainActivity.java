package com.xy.sdktestdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xy.xypay.bean.PayArgs;
import com.xy.xypay.inters.XYPayCallback;
import com.xy.xypay.utils.StringUtils;
import com.xy.xypay.utils.XYPaySDK;


public class MainActivity extends Activity{
	Button btnButton;
	
	Button testBtn;
	
	boolean flag  = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btnButton = (Button)this.findViewById(R.id.btn);
		testBtn = (Button)this.findViewById(R.id.test);
		
		//��ʼ��SDK
		XYPaySDK.initSDK(getApplicationContext(), "lol", true);
		//ע��֧���Ļص��ӿ�
		XYPaySDK.setOnPayCallback(new XYPayCallback() {
			@Override
			public void onPayFinished(int status) {
				switch (status) {
				case XYPaySDK.XYPay_RESULT_CODE_CANCEL://֧��ȡ��
					StringUtils.printLog(true, "getPayStatus"+status,"֧��ȡ��");
					break;
				case XYPaySDK.XYPay_RESULT_CODE_SUCCESS://֧���ɹ�
					StringUtils.printLog(true, "getPayStatus"+status,"֧���ɹ�");
					break;
				case XYPaySDK.XYPay_RESULT_CODE_FAILURE://֧��ʧ��
					StringUtils.printLog(true, "getPayStatus"+status,"֧��ʧ��");
					break;
				}
			}
		});
		
		btnButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				   //��ʼ����������
					PayArgs payArg = new PayArgs();
				 
					payArg.platform ="lol";
					payArg.sid = "1";
					payArg.pay_rmb = "6";
					payArg.imei = "99000316884451";
					payArg.resource_id = "1123107";
					
					payArg.token = "d174ebf344be3713a1c8b5fe722ae872";
					payArg.openuid = "a19fa46e795097c3bc458e88fc88aa9a";
					
					//���²����ǳ�ֵ���õ���1�ƶ�,2��ͨ,3����
					payArg.pay_type = "1";
					payArg.card_id = "";//car no
					payArg.card_pass = "";//card pwd
					
					//��ʼ֧������
					XYPaySDK.initPayArgs(payArg);
					//��ת��xy֧������
					XYPaySDK.jumpToXYPayCenter(getApplicationContext());
			}
		});
		
		testBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (flag){
					testBtn.setText("��ʽ����");
				}else{
					testBtn.setText("���Ի���");
				}
				flag = !flag;
				XYPaySDK.setDebug(getApplicationContext(), flag);
			}
		});
		
	}
	
}
