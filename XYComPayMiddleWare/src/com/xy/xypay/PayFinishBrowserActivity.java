package com.xy.xypay;

import com.xy.xypay.utils.DialogUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

public class PayFinishBrowserActivity extends Activity {
	private WebView mWebView;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				DialogUtils.showToast(PayFinishBrowserActivity.this, "支付成功");
				break;
			case -1:
				DialogUtils.showToast(PayFinishBrowserActivity.this, "游戏服错误");

				break;
			case -2:

				DialogUtils.showToast(PayFinishBrowserActivity.this, "数据库连接错误");
				break;
			case -3:

				DialogUtils.showToast(PayFinishBrowserActivity.this, "玩家不存在");
				break;
			case -4:

				DialogUtils.showToast(PayFinishBrowserActivity.this, "游戏服不存在");
				break;
			case -5:
				DialogUtils.showToast(PayFinishBrowserActivity.this, "加密验证失败");

				break;
			case -6:
				DialogUtils.showToast(PayFinishBrowserActivity.this,
						"订单已存在（订单已成功）");

				break;
			case -7:
				DialogUtils.showToast(PayFinishBrowserActivity.this, "其他错误");

				break;
			case -8:
				DialogUtils.showToast(PayFinishBrowserActivity.this, "参数错误");

				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mWebView = new WebView(this);
		LinearLayout layout = new LinearLayout(this);
		layout.addView(mWebView);
		setContentView(layout);

		String url = this.getIntent().getStringExtra("loding_url");
		mWebView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});

		class JsObject {

			@JavascriptInterface
			public void clickOnAndroid(int ret) {
				mHandler.sendEmptyMessage(ret);
				Log.v("info", "执行了方法。。。。。。。");
			}

		}

		WebSettings webSettings = mWebView.getSettings();
		
		webSettings.setJavaScriptEnabled(true);
		mWebView.addJavascriptInterface(new JsObject(), "demo");
//		mWebView.loadUrl("http://wy.xy.com/hd/androidTest.html");
		 mWebView.loadUrl(url);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
