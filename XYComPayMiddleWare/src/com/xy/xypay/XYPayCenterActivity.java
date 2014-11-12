package com.xy.xypay;

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
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.android.app.sdk.AliPay;
import com.mokredit.payment.MktPayment;
import com.mokredit.payment.MktPluginSetting;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;
import com.xy.xypay.adapter.CreditCardAdapter;
import com.xy.xypay.adapter.DepositBankAdapter;
import com.xy.xypay.adapter.PayCountAdapter;
import com.xy.xypay.alipay.Keys;
import com.xy.xypay.alipay.Rsa;
import com.xy.xypay.bean.CreditCardBean;
import com.xy.xypay.bean.DepositBankBean;
import com.xy.xypay.bean.PayArgs;
import com.xy.xypay.inters.XYPayCallback;
import com.xy.xypay.utils.DialogUtils;
import com.xy.xypay.utils.HttpUtils;
import com.xy.xypay.utils.ReleaseResUtils;
import com.xy.xypay.utils.ResourceUtil;
import com.xy.xypay.utils.StringUtils;
import com.xy.xypay.utils.XYPaySDK;
import com.xy.xypay.utils.XYPayUtiles;

/**
 * TODO 支付类
 * 
 * @author pengsk
 * @data: 2014年11月10日 下午3:01:10
 * @version: V1.0
 */
public class XYPayCenterActivity extends Activity {
	// 支付回调的接口
	public static XYPayCallback mPayCallback;
	// 支付的状态码
	private static int status = 5002;
	// 当前控件的一些id号码
	private int backId;
	private int creditcardbtnId, creditcard_rlayoutId, alipayrbtnId,
			moninerbtnId, depositcardrbtnId, rechargeablecardrbtnId,
			junwangcardrbtnId;

	// 付款按钮的id
	private int creditcard_submitpaybtnId, alipay_submitpaybtnId,
			mo9_submitpaybtnId, deposit_submitpaybtnId,
			rechargecard_submitpaybtnId, junwang_submitpaybtnId;

	private int chinamobileIvId, chinaunicomIvId, chinateleIvId;
	private int rechargecard_textview_paycountId;
	// 顶部的标题栏
	private Button back;// 回退按钮
	// 动画组件
	private Dialog loadingDialog;

	private Button creditcardbtn;// 信用卡支付
	private Button alipayrbtn;// 支付宝支付
	private Button mo9rbtn;// mo9支付
	private Button depositcardrbtn;// 储蓄卡支付
	private Button rechargeablecardrbtn;// 充值卡支付
	private Button junwangcardrbtn;// 骏网卡支付

	// 标题以及支付按钮
	private View.OnClickListener onClickListener;

	// 付款按钮监听
	private View.OnClickListener payBtnOnClickListener;
	// 支付金额相关
	// 价格
	private EditText input_paynum_edittext;
	// 转换比例
	private TextView exchangetextlabel;
	private TextView textyuan;

	/******************* 信用卡支付按钮对应的支付界面 ************************/
	private View creditcardViewInclude;
	private RelativeLayout creditcard_rlayout;
	private Button creditcard_submitpaybtn;

	private GridView creditcard_gridview;
	private CreditCardAdapter creidCardAdapter;
	private List<CreditCardBean> creditCardBeanList;

	/**************** 支付宝支付按钮对应的支付界面 **********************/
	private View alipayViewInclude;
	// 付款按钮
	private Button alipay_submitpaybtn;
	/******** 先玩后付支付按钮对应的支付布局 *******/
	private View mo9ViewInclude;
	// 付款按钮
	private Button mo9_submitpaybtn;
	/*************** 储蓄卡支付按钮对应的支付布局 *******************/
	private View depositViewInclude;
	// 可供选择的储蓄银行的GridView视图
	private GridView deposit_gridview;
	// gridview中上一个选中的状态
	private DepositBankAdapter depositBankAdapter;
	private List<DepositBankBean> depositBankBeanlist;
	// 付款按钮
	private Button deposit_submitpaybtn;
	/********************* 充值卡支付按钮对应的布局 ***************************/
	private View rechargecardViewInclude;
	// 选择的充值面额

	private ImageView chinamobileIv, chinaunicomIv, chinateleIv;
	// 选择充值卡类型的监听器对象
	private View.OnClickListener payTypeClickListener;
	private String card_type = "1";// 充值卡类型默认移动 1移动 2联通 3电信
	private TextView rechargecard_textview_paycount;
	private String payCount = "50";// 默认充值50元
	private String chargeCount = "50";
	// 选择面额时弹出的对话框
	private Dialog dialog = null;
	private List<String> payCountList;
	private ListView dialog_listview;
	private AlertDialog.Builder builder;
	private PayCountAdapter payCountAdapter = null;
	// 充值卡号
	private EditText rechargecard_input_cardno;
	// 充值密码
	private EditText rechargecard_input_cardpwd;
	// 支付按钮
	private Button rechargecard_submitpaybtn;

	/******************** 骏网卡支付按钮对应的布局 ********************************/
	private View junwangViewInclude;
	// 骏网卡号
	private EditText junwang_input_cardno;
	// 骏网卡密码
	private EditText junwang_input_cardpwd;
	// 支付按钮
	private Button junwang_submitpaybtn;

	/********************** SDK环境设置相关 ***********************/
	private SharedPreferences sharedPreferences;
	// 是否是测试环境
	private static boolean isDebug;
	// 设置读取SDK的环境配置

	// 按回退键或回退按钮时蹦出确认对话框
	private Dialog alertDialog;
	/****************************** 支付相关 ********************************/
	private String domain = "";
	// 支付方式,可以是银联，支付宝，信用卡等
	private String payType = "upmpmobile";// 默认使用信用卡
	private String path = "";
	// 支付参数
	private String MODE_RELEASE = "00"; // 正式
	private String MODE_TEST = "01"; // 测试
	private String unionpay_mode = "";
	// 信用卡、银联支付返回code
	private static final int UNION_PAY_RESULT_CODE = 1000;
	// mo9支付返回code
	private static final int MO9_PAY_RESULT_CODE = 2000;
	// 充值卡支付
	private static final int RECHARGEABLE_CARD_RESULT_CODE = 3000;
	// 支付宝支付
	private static final int ALIPAY_RESULT_CODE = 4000;
	// 骏网卡支付
	private static final int JUNWANG_CARD_RESULT_CODE = 5000;
	private PayArgs payArgs;
	// mog成功支付
	private String url_mo9_success = "";

	// 初始化SDK设置
	private void initSDK() {
		sharedPreferences = getApplicationContext().getSharedPreferences(
				"settings", Context.MODE_PRIVATE);
		isDebug = sharedPreferences.getBoolean("isDebug", true);
		if (isDebug) {// 测试环境
			unionpay_mode = MODE_TEST;
			domain = "http://dev3.pay.xy.com/index.php?action=";
		} else {// 正式环境
			unionpay_mode = MODE_RELEASE;
			domain = "http://pay2.xy.com/?action=";
		}
		path = domain;
		StringUtils.printLog(isDebug, "支付环境地址", path);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (loadingDialog != null && loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
			if (!StringUtils.isEmpty((String) msg.obj)) {
				JSONObject jsonObject = null;
				switch (msg.what) {
				case UNION_PAY_RESULT_CODE:// 银联支付
					// 使用银联SDK进行请求
					StringUtils.printLog(isDebug, "银联请求返回的信息",
							msg.obj.toString());
					try {
						jsonObject = new JSONObject((String) msg.obj);
						String result = jsonObject.getString("msg");
						JSONObject jsonObject2 = new JSONObject(result);

						int statusCode = -1;
						statusCode = jsonObject.getInt("ret");
						if (statusCode == 0) {// 成功
							String tn = jsonObject2.getString("tn");
							StringUtils.printLog(isDebug, "获得的流水号", tn);
							if (!StringUtils.isEmpty(tn)) {
								// 调用银联的支付SDK
								UPPayAssistEx.startPayByJAR(
										XYPayCenterActivity.this,
										PayActivity.class, null, null, tn,
										unionpay_mode);
							}
							// startLodingSuccess(jsonObject.getString("url"));
						} else {// 失败
							DialogUtils.showToast(XYPayCenterActivity.this,
									"请求失败");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				case MO9_PAY_RESULT_CODE:// mo9的支付
					try {
						jsonObject = new JSONObject((String) msg.obj);
						int statusCode = jsonObject.getInt("ret");
						switch (statusCode) {
						case 0:// 表示成功
							String url = jsonObject.getString("url");

							MktPluginSetting mktPluginSetting = new MktPluginSetting(
									url);
							Intent intent = new Intent(
									XYPayCenterActivity.this, MktPayment.class);
							intent.putExtra("mokredit_android",
									mktPluginSetting);
							startActivityForResult(intent, 100);
							//pengsk 2014.11.11 这是获得mo9支付后返回的url
							url_mo9_success = jsonObject.getString("url");
							break;
						default:
							DialogUtils.showToast(XYPayCenterActivity.this,
									jsonObject.getString("msg"));
							break;
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					StringUtils.printLog(isDebug, "mo9请求返回的信息",
							msg.obj.toString());
					break;
				case RECHARGEABLE_CARD_RESULT_CODE:// 充值卡支付
					StringUtils.printLog(isDebug, "充值卡请求返回的信息",
							msg.obj.toString());
					try {
						jsonObject = new JSONObject((String) msg.obj);
						String result = jsonObject.getString("msg");
						int statusCode = -1;
						statusCode = jsonObject.getInt("ret");
						if (statusCode == 0) {// 请求成功
							status = XYPaySDK.XYPay_RESULT_CODE_SUCCESS;
							// startLodingSuccess(jsonObject.getString("url"));
						} else {
							status = XYPaySDK.XYPay_RESULT_CODE_FAILURE;
						}
						DialogUtils.showToast(XYPayCenterActivity.this,
								StringUtils.decode(result));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				case ALIPAY_RESULT_CODE:// 阿里支付
					StringUtils.printLog(isDebug, "支付宝请求返回的信息",
							msg.obj.toString());
					String alipayresult = (String) msg.obj;
					int i = alipayresult.indexOf("{");
					int j = alipayresult.indexOf("}");
					String temp = alipayresult.substring(i + 1, j);
					int resutlCode = Integer.parseInt(temp);

					if (resutlCode == 9000) {// 支付成功
						status = XYPaySDK.XYPay_RESULT_CODE_SUCCESS;
					} else if (resutlCode == 6001) {// 支付取消
						status = XYPaySDK.XYPay_RESULT_CODE_CANCEL;
					} else {// 支付出错
						status = XYPaySDK.XYPay_RESULT_CODE_FAILURE;
					}
					break;
				case JUNWANG_CARD_RESULT_CODE:// 骏网支付
					StringUtils.printLog(isDebug, "骏网卡请求返回的信息",
							msg.obj.toString());
					try {
						jsonObject = new JSONObject((String) msg.obj);
						JSONObject result = jsonObject.getJSONObject("msg");
						int statusCode = -1;
						statusCode = jsonObject.getInt("ret");
						if (statusCode == 0) {// 请求成功
							status = XYPaySDK.XYPay_RESULT_CODE_SUCCESS;
							// startLodingSuccess(jsonObject.getString("url"));
						} else {
							status = XYPaySDK.XYPay_RESULT_CODE_FAILURE;
						}
						String resultInfo = result.getString("ret_msg");
						DialogUtils.showToast(XYPayCenterActivity.this,
								StringUtils.decode(resultInfo));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				default:
					break;
				}
			} else {
				DialogUtils
						.showToast(XYPayCenterActivity.this, "网络连接错误,请求数据失败");
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(ResourceUtil.getLayoutId(getApplicationContext(),
				"com_xy_xypay_pay_activity"));
		// 设置SDK环境
		initSDK();
		// 实例化视图
		initViews();
		// 给金额输入框设置监听
		addTextWatchers();
		initPayTypeListener();
		// 初始化监听器
		initListeners();
		// 初始化付款监听器
		initPayListeners();
		// 设置监听器
		setListeners();
		// 设置付款监听对象
		setPayListeners();
	}

	private void addTextWatchers() {
		// 信用卡
		input_paynum_edittext.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if ("".equals(s.toString().trim()) || s == null) {
					exchangetextlabel.setText("50元=500蓝钻");
				} else {
					int price = Integer.parseInt(s.toString());
					exchangetextlabel.setText(price + "元=" + (price * 10)
							+ "蓝钻");
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	// 初始化视图控件
	private void initViews() {
		backId = ResourceUtil.getId(getApplicationContext(), "back");
		// 左侧付款渠道导航按钮
		creditcardbtnId = ResourceUtil.getId(getApplicationContext(),
				"creditcardbtn");
		creditcard_rlayoutId = ResourceUtil.getId(getApplicationContext(),
				"creditcard_rlayout");
		alipayrbtnId = ResourceUtil
				.getId(getApplicationContext(), "alipayrbtn");
		moninerbtnId = ResourceUtil
				.getId(getApplicationContext(), "moninerbtn");
		depositcardrbtnId = ResourceUtil.getId(getApplicationContext(),
				"depositcardrbtn");
		rechargeablecardrbtnId = ResourceUtil.getId(getApplicationContext(),
				"rechargeablecardrbtn");
		junwangcardrbtnId = ResourceUtil.getId(getApplicationContext(),
				"junwangcardrbtn");
		// 付款按钮
		creditcard_submitpaybtnId = ResourceUtil.getId(getApplicationContext(),
				"creditcard_submitpaybtn");
		alipay_submitpaybtnId = ResourceUtil.getId(getApplicationContext(),
				"alipay_submitpaybtn");
		mo9_submitpaybtnId = ResourceUtil.getId(getApplicationContext(),
				"mo9_submitpaybtn");
		deposit_submitpaybtnId = ResourceUtil.getId(getApplicationContext(),
				"deposit_submitpaybtn");
		rechargecard_submitpaybtnId = ResourceUtil.getId(
				getApplicationContext(), "rechargecard_submitpaybtn");
		junwang_submitpaybtnId = ResourceUtil.getId(getApplicationContext(),
				"junwang_submitpaybtn");

		rechargecard_textview_paycountId = ResourceUtil.getId(
				getApplicationContext(), "rechargecard_textview_paycount");

		chinamobileIvId = ResourceUtil.getId(getApplicationContext(),
				"chinamobile_iv");
		chinaunicomIvId = ResourceUtil.getId(getApplicationContext(),
				"chinaunicom_iv");
		chinateleIvId = ResourceUtil.getId(getApplicationContext(),
				"chinatele_iv");

		// 标题栏的回退按钮和菜单按钮
		back = (Button) this.findViewById(ResourceUtil.getId(
				getApplicationContext(), "back"));
		// 右侧支付按钮实例化
		creditcardViewInclude = (RelativeLayout) this.findViewById(ResourceUtil
				.getId(getApplicationContext(), "creditcard_root_rlayout"));
		creditcard_rlayout = (RelativeLayout) this.findViewById(ResourceUtil
				.getId(getApplicationContext(), "creditcard_rlayout"));
		creditcardbtn = (Button) this.findViewById(ResourceUtil.getId(
				getApplicationContext(), "creditcardbtn"));
		alipayrbtn = (Button) this.findViewById(ResourceUtil.getId(
				getApplicationContext(), "alipayrbtn"));
		mo9rbtn = (Button) this.findViewById(ResourceUtil.getId(
				getApplicationContext(), "moninerbtn"));
		depositcardrbtn = (Button) this.findViewById(ResourceUtil.getId(
				getApplicationContext(), "depositcardrbtn"));
		rechargeablecardrbtn = (Button) this.findViewById(ResourceUtil.getId(
				getApplicationContext(), "rechargeablecardrbtn"));
		junwangcardrbtn = (Button) this.findViewById(ResourceUtil.getId(
				getApplicationContext(), "junwangcardrbtn"));
		// 支付金额相关
		input_paynum_edittext = (EditText) this.findViewById(ResourceUtil
				.getId(getApplicationContext(), "input_paynum_edittext"));
		exchangetextlabel = (TextView) this.findViewById(ResourceUtil.getId(
				getApplicationContext(), "exchangetextlabel"));
		textyuan = (TextView) this.findViewById(ResourceUtil.getId(
				getApplicationContext(), "textyuan"));

		/****************** 信用卡支付按钮对应的支付布局 *************************/
		creditcardViewInclude = (RelativeLayout) this.findViewById(ResourceUtil
				.getId(getApplicationContext(), "creditcard_rlayout_include"));

		creditcard_submitpaybtn = (Button) this.findViewById(ResourceUtil
				.getId(getApplicationContext(), "creditcard_submitpaybtn"));

		creditcard_gridview = (GridView) this.findViewById(ResourceUtil.getId(
				getApplicationContext(), "creditcard_gridview"));
		// 初始化数据填充grid_view
		if (creditCardBeanList == null) {
			creditCardBeanList = new ArrayList<CreditCardBean>();
			initCreditCardDatas();
			creidCardAdapter = new CreditCardAdapter(getApplicationContext(),
					creditCardBeanList);
			creditcard_gridview.setAdapter(creidCardAdapter);
		}

		/********************** 支付宝支付按钮对应的支付布局 ****************************************/
		alipayViewInclude = this.findViewById(ResourceUtil.getId(
				getApplicationContext(), "alipay_rlayout_include"));
		alipay_submitpaybtn = (Button) this.findViewById(ResourceUtil.getId(
				getApplicationContext(), "alipay_submitpaybtn"));

		/********************** 先玩后付支付按钮对应的支付布局 ****************************************/
		mo9ViewInclude = this.findViewById(ResourceUtil.getId(
				getApplicationContext(), "mo9_rlayout_include"));
		mo9_submitpaybtn = (Button) this.findViewById(ResourceUtil.getId(
				getApplicationContext(), "mo9_submitpaybtn"));

		/*************** 储蓄卡支付按钮对应的支付布局 *******************/
		depositViewInclude = this.findViewById(ResourceUtil.getId(
				getApplicationContext(), "deposit_rlayout_include"));
		deposit_gridview = (GridView) this.findViewById(ResourceUtil.getId(
				getApplicationContext(), "deposit_gridview"));
		deposit_submitpaybtn = (Button) this.findViewById(ResourceUtil.getId(
				getApplicationContext(), "deposit_submitpaybtn"));

		/********************* 充值卡支付按钮对应的布局 ***************************/
		rechargecardViewInclude = this.findViewById(ResourceUtil.getId(
				getApplicationContext(), "rechargecard_rlayout_include"));

		chinamobileIv = (ImageView) this.findViewById(ResourceUtil.getId(
				getApplicationContext(), "chinamobile_iv"));
		chinaunicomIv = (ImageView) this.findViewById(ResourceUtil.getId(
				getApplicationContext(), "chinaunicom_iv"));
		chinateleIv = (ImageView) this.findViewById(ResourceUtil.getId(
				getApplicationContext(), "chinatele_iv"));

		rechargecard_textview_paycount = (TextView) this
				.findViewById(ResourceUtil.getId(getApplicationContext(),
						"rechargecard_textview_paycount"));
		rechargecard_input_cardno = (EditText) this.findViewById(ResourceUtil
				.getId(getApplicationContext(), "rechargecard_input_cardno"));
		rechargecard_input_cardpwd = (EditText) this.findViewById(ResourceUtil
				.getId(getApplicationContext(), "rechargecard_input_cardpwd"));
		rechargecard_submitpaybtn = (Button) this.findViewById(ResourceUtil
				.getId(getApplicationContext(), "rechargecard_submitpaybtn"));

		/******************** 骏网卡支付按钮对应的布局 ********************************/
		junwangViewInclude = this.findViewById(ResourceUtil.getId(
				getApplicationContext(), "junwang_rlayout_include"));
		junwang_input_cardno = (EditText) this.findViewById(ResourceUtil.getId(
				getApplicationContext(), "junwang_input_cardno"));
		junwang_input_cardpwd = (EditText) this.findViewById(ResourceUtil
				.getId(getApplicationContext(), "junwang_input_cardpwd"));
		junwang_submitpaybtn = (Button) this.findViewById(ResourceUtil.getId(
				getApplicationContext(), "junwang_submitpaybtn"));
	}

	private void initListeners() {
		// 支付方式导航栏监听对象
		onClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v.getId() == backId) {// 回退按钮
					alertDialog = DialogUtils.createConfirmExitDialog(
							XYPayCenterActivity.this, mPayCallback, status);
					alertDialog.show();
				} else if ((v.getId() == creditcard_rlayoutId)
						|| (v.getId() == creditcardbtnId)) {// 信用卡支付
					input_paynum_edittext.setVisibility(View.VISIBLE);
					textyuan.setVisibility(View.VISIBLE);
					if (input_paynum_edittext.getText().toString().trim() != null
							&& !"".equals(input_paynum_edittext.getText()
									.toString().trim())) {
						int price = Integer.parseInt(input_paynum_edittext
								.getText().toString().trim());
						exchangetextlabel.setText(price + "元=" + (price * 10)
								+ "蓝钻");
					} else {
						exchangetextlabel.setText("50元=500蓝钻");
					}
					// 设置支付方式
					payType = "upmpmobile";
					creditcardViewInclude.setVisibility(View.VISIBLE);
					creditcard_rlayout.setBackgroundResource(ResourceUtil
							.getDrawableId(getApplicationContext(),
									"com_xy_xypay_side_on_bg"));
					// alipay不可见
					alipayViewInclude.setVisibility(View.GONE);
					alipayrbtn.setBackgroundResource(0);
					// mo9不可见
					mo9ViewInclude.setVisibility(View.GONE);
					mo9rbtn.setBackgroundResource(0);
					// 储蓄不可见
					depositViewInclude.setVisibility(View.GONE);
					depositcardrbtn.setBackgroundResource(0);

					rechargecardViewInclude.setVisibility(View.GONE);
					rechargeablecardrbtn.setBackgroundResource(0);

					junwangViewInclude.setVisibility(View.GONE);
					junwangcardrbtn.setBackgroundResource(0);

				} else if (v.getId() == alipayrbtnId) {// 支付宝支付
					input_paynum_edittext.setVisibility(View.VISIBLE);
					textyuan.setVisibility(View.VISIBLE);
					if (input_paynum_edittext.getText().toString().trim() != null
							&& !"".equals(input_paynum_edittext.getText()
									.toString().trim())) {
						int price = Integer.parseInt(input_paynum_edittext
								.getText().toString().trim());
						exchangetextlabel.setText(price + "元=" + (price * 10)
								+ "蓝钻");
					} else {
						exchangetextlabel.setText("50元=500蓝钻");
					}
					// 设置支付方式
					payType = "alipaymobile/appnotify";
					alipayViewInclude.setVisibility(View.VISIBLE);
					alipayrbtn.setBackgroundResource(ResourceUtil
							.getDrawableId(getApplicationContext(),
									"com_xy_xypay_side_on_bg"));
					creditcardViewInclude.setVisibility(View.GONE);
					creditcard_rlayout.setBackgroundResource(ResourceUtil
							.getDrawableId(getApplicationContext(),
									"com_xy_xypay_side_on_default"));
					mo9ViewInclude.setVisibility(View.GONE);
					mo9rbtn.setBackgroundResource(0);

					depositViewInclude.setVisibility(View.GONE);
					depositcardrbtn.setBackgroundResource(0);

					rechargecardViewInclude.setVisibility(View.GONE);
					rechargeablecardrbtn.setBackgroundResource(0);

					junwangViewInclude.setVisibility(View.GONE);
					junwangcardrbtn.setBackgroundResource(0);
				} else if (v.getId() == moninerbtnId) {// mo9支付
					input_paynum_edittext.setVisibility(View.VISIBLE);
					textyuan.setVisibility(View.VISIBLE);
					if (input_paynum_edittext.getText().toString().trim() != null
							&& !"".equals(input_paynum_edittext.getText()
									.toString().trim())) {
						int price = Integer.parseInt(input_paynum_edittext
								.getText().toString().trim());
						exchangetextlabel.setText(price + "元=" + (price * 10)
								+ "蓝钻");
					} else {
						exchangetextlabel.setText("50元=500蓝钻");
					}
					// 设置支付方式
					payType = "mo9mobile";
					mo9ViewInclude.setVisibility(View.VISIBLE);
					mo9rbtn.setBackgroundResource(ResourceUtil.getDrawableId(
							getApplicationContext(), "com_xy_xypay_side_on_bg"));

					creditcardViewInclude.setVisibility(View.GONE);
					creditcard_rlayout.setBackgroundResource(ResourceUtil
							.getDrawableId(getApplicationContext(),
									"com_xy_xypay_side_on_default"));

					alipayViewInclude.setVisibility(View.GONE);
					alipayrbtn.setBackgroundResource(0);

					depositViewInclude.setVisibility(View.GONE);
					depositcardrbtn.setBackgroundResource(0);

					rechargecardViewInclude.setVisibility(View.GONE);
					rechargeablecardrbtn.setBackgroundResource(0);

					junwangViewInclude.setVisibility(View.GONE);
					junwangcardrbtn.setBackgroundResource(0);

				} else if (v.getId() == depositcardrbtnId) {// 储蓄卡支付

					input_paynum_edittext.setVisibility(View.VISIBLE);
					textyuan.setVisibility(View.VISIBLE);

					if (input_paynum_edittext.getText().toString().trim() != null
							&& !"".equals(input_paynum_edittext.getText()
									.toString().trim())) {
						int price = Integer.parseInt(input_paynum_edittext
								.getText().toString().trim());
						exchangetextlabel.setText(price + "元=" + (price * 10)
								+ "蓝钻");
					} else {
						exchangetextlabel.setText("50元=500蓝钻");
					}
					// 设置支付方式
					payType = "upmpmobile";
					depositViewInclude.setVisibility(View.VISIBLE);
					depositcardrbtn.setBackgroundResource(ResourceUtil
							.getDrawableId(getApplicationContext(),
									"com_xy_xypay_side_on_bg"));

					mo9ViewInclude.setVisibility(View.GONE);
					mo9rbtn.setBackgroundResource(0);

					creditcardViewInclude.setVisibility(View.GONE);
					creditcard_rlayout.setBackgroundResource(ResourceUtil
							.getDrawableId(getApplicationContext(),
									"com_xy_xypay_side_on_default"));

					alipayViewInclude.setVisibility(View.GONE);
					alipayrbtn.setBackgroundResource(0);

					rechargecardViewInclude.setVisibility(View.GONE);
					rechargeablecardrbtn.setBackgroundResource(0);

					junwangViewInclude.setVisibility(View.GONE);
					junwangcardrbtn.setBackgroundResource(0);

					if (depositBankBeanlist == null) {
						depositBankBeanlist = new ArrayList<DepositBankBean>();
						initDepositBankDatas();
						depositBankAdapter = new DepositBankAdapter(
								getApplicationContext(), depositBankBeanlist);
						deposit_gridview.setAdapter(depositBankAdapter);
					}

				} else if (v.getId() == rechargeablecardrbtnId) {// 充值卡
					input_paynum_edittext.setVisibility(View.GONE);
					textyuan.setVisibility(View.GONE);

					exchangetextlabel.setText(chargeCount + "元="
							+ (Integer.parseInt(chargeCount) * 10) + "蓝钻");
					payType = "oneninemobilepay";
					rechargecardViewInclude.setVisibility(View.VISIBLE);
					rechargeablecardrbtn.setBackgroundResource(ResourceUtil
							.getDrawableId(getApplicationContext(),
									"com_xy_xypay_side_on_bg"));

					mo9ViewInclude.setVisibility(View.GONE);
					mo9rbtn.setBackgroundResource(0);

					creditcardViewInclude.setVisibility(View.GONE);
					creditcard_rlayout.setBackgroundResource(ResourceUtil
							.getDrawableId(getApplicationContext(),
									"com_xy_xypay_side_on_default"));

					alipayViewInclude.setVisibility(View.GONE);
					alipayrbtn.setBackgroundResource(0);

					depositViewInclude.setVisibility(View.GONE);
					depositcardrbtn.setBackgroundResource(0);

					junwangViewInclude.setVisibility(View.GONE);
					junwangcardrbtn.setBackgroundResource(0);

				} else if (v.getId() == junwangcardrbtnId) {// 骏网卡支付

					input_paynum_edittext.setVisibility(View.VISIBLE);
					textyuan.setVisibility(View.VISIBLE);
					if (input_paynum_edittext.getText().toString().trim() != null
							&& !"".equals(input_paynum_edittext.getText()
									.toString().trim())) {
						int price = Integer.parseInt(input_paynum_edittext
								.getText().toString().trim());
						exchangetextlabel.setText(price + "元=" + (price * 10)
								+ "蓝钻");
					} else {
						exchangetextlabel.setText("50元=500蓝钻");
					}
					payType = "heepaymobile";
					junwangViewInclude.setVisibility(View.VISIBLE);
					junwangcardrbtn.setBackgroundResource(ResourceUtil
							.getDrawableId(getApplicationContext(),
									"com_xy_xypay_side_on_bg"));

					mo9ViewInclude.setVisibility(View.GONE);
					mo9rbtn.setBackgroundResource(0);

					creditcardViewInclude.setVisibility(View.GONE);
					creditcard_rlayout.setBackgroundResource(ResourceUtil
							.getDrawableId(getApplicationContext(),
									"com_xy_xypay_side_on_default"));

					alipayViewInclude.setVisibility(View.GONE);
					alipayrbtn.setBackgroundResource(0);

					depositViewInclude.setVisibility(View.GONE);
					depositcardrbtn.setBackgroundResource(0);

					rechargecardViewInclude.setVisibility(View.GONE);
					rechargeablecardrbtn.setBackgroundResource(0);
				} else if (v.getId() == rechargecard_textview_paycountId) {// 充值卡支付时弹出创库选择面值
					View view = null;
					if (payCountList == null) {
						payCountList = new ArrayList<String>();
						payCountList.add("10元");
						payCountList.add("20元");
						payCountList.add("30元");
						payCountList.add("50元");
						payCountList.add("100元");
						payCountList.add("300元");

						payCountAdapter = new PayCountAdapter(
								getApplicationContext(), payCountList);
						builder = new AlertDialog.Builder(
								XYPayCenterActivity.this);
						view = LayoutInflater
								.from(getApplicationContext())
								.inflate(
										ResourceUtil
												.getLayoutId(
														getApplicationContext(),
														"dialog_paycount_listview_item"),
										null);
						dialog_listview = (ListView) view
								.findViewById(ResourceUtil.getId(
										getApplicationContext(),
										"dialog_listview"));
						dialog_listview.setAdapter(payCountAdapter);
						dialog_listview
								.setOnItemClickListener(new OnItemClickListener() {
									@Override
									public void onItemClick(
											AdapterView<?> parent, View view,
											int position, long id) {
										chargeCount = payCountList
												.get(position);
										chargeCount = chargeCount.substring(0,
												chargeCount.length() - 1);

										Integer price = Integer
												.parseInt(chargeCount);
										exchangetextlabel.setText(price + "元="
												+ (price * 10) + "蓝钻");
										Log.e("chargeCount", chargeCount);
										rechargecard_textview_paycount
												.setText("面    值:"
														+ payCountList
																.get(position));
										dialog.dismiss();
									}
								});
						builder.setView(view);
						dialog = builder.create();
					}
					dialog.show();
				}
			}
		};

	}

	// 付款监听实例化
	private void initPayListeners() {
		payBtnOnClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (HttpUtils.isNetworkAvailable(getApplicationContext())) {// 网络可用
					if (v.getId() == creditcard_submitpaybtnId) {// 信用卡付款
						payCount = input_paynum_edittext.getText().toString()
								.trim();
						if (StringUtils.isEmpty(payCount)) {
							DialogUtils.showToast(XYPayCenterActivity.this,
									"充值金额不能为空");
						} else {
							XYPayNotContainAlipay(null, null, UNION_PAY_RESULT_CODE);
						}
					} else if (v.getId() == alipay_submitpaybtnId) {// 支付宝
						payCount = input_paynum_edittext.getText().toString()
								.trim();
						if (StringUtils.isEmpty(payCount)) {
							DialogUtils.showToast(XYPayCenterActivity.this,
									"充值金额不能为空");
						} else {
							XYPayOnlyAlipay();
						}

					} else if (v.getId() == mo9_submitpaybtnId) {// mo9支付
						payCount = input_paynum_edittext.getText().toString()
								.trim();
						if (StringUtils.isEmpty(payCount)) {
							DialogUtils.showToast(XYPayCenterActivity.this,
									"充值金额不能为空");
						} else {
							XYPayNotContainAlipay(null, null, MO9_PAY_RESULT_CODE);
						}

					} else if (v.getId() == deposit_submitpaybtnId) {// 储蓄卡支付
						// 发起网络请求得到tn(流水号),然后调用银联的sdk
						payCount = input_paynum_edittext.getText().toString()
								.trim();
						if (StringUtils.isEmpty(payCount)) {
							DialogUtils.showToast(XYPayCenterActivity.this,
									"充值金额不能为空");
						} else {
							XYPayNotContainAlipay(null, null, UNION_PAY_RESULT_CODE);
						}
					} else if (v.getId() == rechargecard_submitpaybtnId) {// 充值卡
						final String cardid = rechargecard_input_cardno
								.getText().toString().trim();
						final String cardpwd = rechargecard_input_cardpwd
								.getText().toString().trim();
						if (StringUtils.isEmpty(cardid)) {
							DialogUtils.showToast(XYPayCenterActivity.this,
									"充值卡号不能为空");
							return;
						} else {
							if (StringUtils.isEmpty(cardpwd)) {
								DialogUtils.showToast(XYPayCenterActivity.this,
										"充值卡密码不能为空");
								return;
							} else {
								XYPayNotContainAlipay(cardid, cardpwd, RECHARGEABLE_CARD_RESULT_CODE);
							}
						}
					} else if (v.getId() == junwang_submitpaybtnId) {// 骏网卡支付
						final String cardid = junwang_input_cardno.getText()
								.toString().trim();
						final String cardpwd = junwang_input_cardpwd.getText()
								.toString().trim();
						payCount = input_paynum_edittext.getText().toString()
								.trim();
						if (StringUtils.isEmpty(payCount)) {
							DialogUtils.showToast(XYPayCenterActivity.this,
									"充值金额不能为空");
							return;
						} else {
							if (StringUtils.isEmpty(cardid)) {
								DialogUtils.showToast(XYPayCenterActivity.this,
										"骏网卡号不能为空");
								return;
							} else {
								if (StringUtils.isEmpty(cardpwd)) {
									DialogUtils.showToast(
											XYPayCenterActivity.this,
											"骏网卡密码不能为空");
									return;
								} else {
									XYPayNotContainAlipay(cardid, cardpwd, JUNWANG_CARD_RESULT_CODE);
								}
							}
						}
					}
				} else {
					DialogUtils.showToast(XYPayCenterActivity.this, "请先连接网络");
				}
			}
		};
	}

	// 初始化radiogroup,充值卡选择卡类型(移动，联通,电信)
	private void initPayTypeListener() {
		payTypeClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v.getId() == chinamobileIvId) {// 移动
					card_type = "1";
					chinamobileIv.setImageResource(ResourceUtil.getDrawableId(
							getApplicationContext(),
							"com_xy_xypay_radiobutton_pressed"));
					chinaunicomIv.setImageResource(ResourceUtil.getDrawableId(
							getApplicationContext(),
							"com_xy_xypay_radiobutton_bg"));
					chinateleIv.setImageResource(ResourceUtil.getDrawableId(
							getApplicationContext(),
							"com_xy_xypay_radiobutton_bg"));
				} else if (v.getId() == chinaunicomIvId) {// 联通
					card_type = "2";// 联通
					chinaunicomIv.setImageResource(ResourceUtil.getDrawableId(
							getApplicationContext(),
							"com_xy_xypay_radiobutton_pressed"));
					chinamobileIv.setImageResource(ResourceUtil.getDrawableId(
							getApplicationContext(),
							"com_xy_xypay_radiobutton_bg"));
					chinateleIv.setImageResource(ResourceUtil.getDrawableId(
							getApplicationContext(),
							"com_xy_xypay_radiobutton_bg"));
				} else if (v.getId() == chinateleIvId) {// 电信
					card_type = "3";// 电信
					chinateleIv.setImageResource(ResourceUtil.getDrawableId(
							getApplicationContext(),
							"com_xy_xypay_radiobutton_pressed"));
					chinamobileIv.setImageResource(ResourceUtil.getDrawableId(
							getApplicationContext(),
							"com_xy_xypay_radiobutton_bg"));
					chinaunicomIv.setImageResource(ResourceUtil.getDrawableId(
							getApplicationContext(),
							"com_xy_xypay_radiobutton_bg"));
				}
				// 清空输入的账号密码
				rechargecard_input_cardno.setText("");
				rechargecard_input_cardpwd.setText("");
			}
		};
		// 移动设置监听
		chinamobileIv.setOnClickListener(payTypeClickListener);
		// 联通
		chinaunicomIv.setOnClickListener(payTypeClickListener);
		// 电信
		chinateleIv.setOnClickListener(payTypeClickListener);
	}

	// 左侧支付类型的按钮监听(支付宝、信用卡等。。)
	private void setListeners() {
		back.setOnClickListener(onClickListener);
		// 支付按钮
		creditcardbtn.setOnClickListener(onClickListener);
		creditcard_rlayout.setOnClickListener(onClickListener);
		alipayrbtn.setOnClickListener(onClickListener);
		mo9rbtn.setOnClickListener(onClickListener);
		depositcardrbtn.setOnClickListener(onClickListener);
		rechargeablecardrbtn.setOnClickListener(onClickListener);
		junwangcardrbtn.setOnClickListener(onClickListener);

		rechargecard_textview_paycount.setOnClickListener(onClickListener);
	}

	// 设置付款监听
	private void setPayListeners() {
		creditcard_submitpaybtn.setOnClickListener(payBtnOnClickListener);
		alipay_submitpaybtn.setOnClickListener(payBtnOnClickListener);
		mo9_submitpaybtn.setOnClickListener(payBtnOnClickListener);
		deposit_submitpaybtn.setOnClickListener(payBtnOnClickListener);
		rechargecard_submitpaybtn.setOnClickListener(payBtnOnClickListener);
		junwang_submitpaybtn.setOnClickListener(payBtnOnClickListener);
	}

	private void initDepositBankDatas() {
		String[] banknames = new String[] { "工商银行", "建设银行", "招商银行", "中国银行",
				"光大银行", "邮政储蓄", "兴业银行", "广发银行", "浦发银行", "民生银行", "中信银行", "平安银行",
				"深圳发展", "北京银行", "上海银行", "华夏银行" };
		DepositBankBean bankBean = null;
		for (int i = 0; i < 16; i++) {
			bankBean = new DepositBankBean();
			bankBean.drawableId = ResourceUtil.getDrawableId(
					getApplicationContext(), "com_xy_xypay_bankname_" + i);
			bankBean.name = banknames[i];
			depositBankBeanlist.add(bankBean);
		}
	}

	private void initCreditCardDatas() {
		String[] banknames = new String[] { "工商银行", "建设银行", "招商银行", "中国银行",
				"光大银行", "邮政储蓄", "兴业银行", "广发银行", "浦发银行", "民生银行", "中信银行" };
		CreditCardBean creditCardBean = null;
		for (int i = 0; i < 11; i++) {
			creditCardBean = new CreditCardBean();
			creditCardBean.drawableId = ResourceUtil.getDrawableId(
					getApplicationContext(), "com_xy_xypay_bankname_" + i);
			creditCardBean.name = banknames[i];
			creditCardBeanList.add(creditCardBean);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		/*************************************************
		 * 
		 * 步骤3：处理银联手机支付控件返回的支付结果
		 * 
		 ************************************************/
		// pengsk 这里不应该是银联支付应该是mo9支付的返回
		
		if (requestCode == 100) {
			if (resultCode == 10) {// 交易成功
				status = XYPaySDK.XYPay_RESULT_CODE_SUCCESS;
				// startLodingSuccess(url_mo9_success);
			} else {// 交易取消或者失败
				status = XYPaySDK.XYPay_RESULT_CODE_FAILURE;
			}
			return;
		}
		if (data == null) {
			return;
		}
		/*
		 * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
		 */
		String str = data.getExtras().getString("pay_result");
		if (str.equalsIgnoreCase("success")) {
			status = XYPaySDK.XYPay_RESULT_CODE_SUCCESS;
		} else if (str.equalsIgnoreCase("fail")) {
			status = XYPaySDK.XYPay_RESULT_CODE_FAILURE;
		} else if (str.equalsIgnoreCase("cancel")) {
			status = XYPaySDK.XYPay_RESULT_CODE_CANCEL;
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {// 补货回退键
			if ((dialog == null) || !dialog.isShowing()) {
				alertDialog = DialogUtils.createConfirmExitDialog(
						XYPayCenterActivity.this, mPayCallback, status);
				alertDialog.show();
				return true;
			} else {
				return super.onKeyUp(keyCode, event);
			}
		}
		return super.onKeyUp(keyCode, event);
	}

	// 得到签名方式
	
	/**  
	 * TODO 获得签名方式。
	 *  @return
	 * @return String  签名方式字符串
	 */
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}

	public static boolean getIsDebug() {
		return isDebug;
	}
//
	
	/**  
	 * TODO 除了支付宝的所有支付。 开启线程，连接服务器，获取订单号，成功与否。等等json数据，发送hander消息，
	 *  @param cardid 充值卡的卡号
	 *  @param cardpwd 充值卡密码
	 *  @param messagwhat 消息message的what。1000是信用卡和储蓄卡支付  2000 mo9支付返回code  3000是充值卡支付   4000是支付宝   5000是骏网卡支付
	 */
	private void XYPayNotContainAlipay(final String cardid,
			final String cardpwd, final int messagwhat) {
		loadingDialog = DialogUtils.createLoadingDialog(
				XYPayCenterActivity.this, "");
		loadingDialog.show();
		new Thread() {
			@Override
			public void run() {
				payArgs = XYPaySDK.getPayAgs();
				payArgs.pay_type = card_type;// 中国电信移动联通等
				if (cardid != null) {
					payArgs.card_id = cardid;
				}
				if (cardpwd != null) {
					payArgs.card_pass = cardpwd;
				}
				payArgs.pay_rmb = payCount;

				String result = HttpUtils.doGetRequest(
						XYPayUtiles.genHttpPath(payArgs, path, payType),
						isDebug);
				Message message = Message.obtain(handler);
				message.what = messagwhat;// 各种支付的返回值
				message.obj = result;
				if (message != null) {
					message.sendToTarget();
				}
			}
		}.start();
	}
	//
	
	/**  
	 * TODO 支付宝支付
	 */
	private void XYPayOnlyAlipay(){
		new Thread() {
			@Override
			public void run() {
				payArgs = XYPaySDK.getPayAgs();
				payArgs.pay_rmb = payCount;
				if (isDebug) {
					payArgs.pay_rmb = "0.01";
				}
				// 用alipay的支付控件进行支付
				String info = XYPayUtiles.getNewOrderInfo(
						payArgs.pay_rmb, payArgs);
				String sign = Rsa.sign(info, Keys.PRIVATE);
				try {
					sign = URLEncoder.encode(sign,
							HTTP.UTF_8);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				info += "&sign=\"" + sign + "\"&"
						+ getSignType();
				// start the pay.
				StringUtils.printLog(isDebug, "订单参数",
						info.toString());
				AliPay alipay = new AliPay(
						XYPayCenterActivity.this, handler);
				// 设置为沙箱模式，不设置默认为线上环境
				// alipay.setSandBox(isDebug);
				String result = alipay.pay(info);
				StringUtils.printLog(isDebug, "支付返回的数据",
						"result = " + result);
				Message message = Message.obtain(handler);
				message.what = ALIPAY_RESULT_CODE;// 阿里支付
				message.obj = result;
				if (message != null) {
					message.sendToTarget();
				}
			}
		}.start();
	}
	// //加载支付成功网页
	// private void startLodingSuccess(String url){
	// Intent intent=new Intent(XYPayCenterActivity.this,
	// PayFinishBrowserActivity.class);
	// intent.putExtra("loding_url", url);
	// startActivity(intent);
	// }
	@Override
	public void onDestroy() {
		super.onDestroy();
		// 释放资源
		ReleaseResUtils.releaseListener(back);
		ReleaseResUtils.releaseListener(creditcardbtn);
		ReleaseResUtils.releaseListener(alipayrbtn);
		ReleaseResUtils.releaseListener(mo9rbtn);
		ReleaseResUtils.releaseListener(depositcardrbtn);
		ReleaseResUtils.releaseListener(rechargeablecardrbtn);
		ReleaseResUtils.releaseListener(junwangcardrbtn);
		ReleaseResUtils.releaseListener(creditcard_rlayout);
		ReleaseResUtils.releaseListener(creditcard_submitpaybtn);
		ReleaseResUtils.releaseListener(alipay_submitpaybtn);
		ReleaseResUtils.releaseListener(mo9_submitpaybtn);
		ReleaseResUtils.dismiss(alertDialog);
		ReleaseResUtils.releaseListener(mo9_submitpaybtn);
		ReleaseResUtils.releaseListener(deposit_submitpaybtn);
		ReleaseResUtils.releaseListener(rechargecard_textview_paycount);
		ReleaseResUtils.releaseListener(rechargecard_submitpaybtn);
		ReleaseResUtils.releaseListener(junwang_submitpaybtn);
		ReleaseResUtils.releaseHandler(handler);
		ReleaseResUtils.dismiss(dialog);
		junwangViewInclude = null;
		junwang_input_cardno = null;
		junwang_input_cardpwd = null;
		rechargecardViewInclude = null;
		depositViewInclude = null;
		deposit_gridview = null;
		depositBankAdapter = null;
		depositBankBeanlist = null;
		payCount = null;
		payCountList = null;
		dialog_listview = null;
		builder = null;
		payCountAdapter = null;
		rechargecard_input_cardno = null;
		rechargecard_input_cardpwd = null;
		domain = null;
		payType = null;
		path = null;
		MODE_RELEASE = null;
		sharedPreferences = null;
		MODE_TEST = null;
		payArgs = null;
		onClickListener = null;
		payBtnOnClickListener = null;
		creditcardViewInclude = null;
		creditcard_gridview = null;
		creidCardAdapter = null;
		creditCardBeanList = null;
		alipayViewInclude = null;
		mo9ViewInclude = null;
		status = 5002;
	}
}
