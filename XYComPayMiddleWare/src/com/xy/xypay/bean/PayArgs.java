package com.xy.xypay.bean;

import java.io.Serializable;

/**
 * 这是一个游戏提交的参数类
 * @author ruanwei
 */
public class PayArgs implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     *资源编号（必填）
     */
    public String resource_id="";
    /**
     * 套餐ID（选填，与pay_rmb二选一）
     */
    public String taocan_id="";
    /**
     * 充值金额(选填)
     */
    public String pay_rmb="";
    /**
     * 显示充值结果的页面地址(选填)
     */
    public String jump_url="";
    
    /**
     * 异步回调地址(选填)
     */
    public String callback_url="";
    /**
     * 区服ID(必填，如果不区分默认传0);
     */
    public String sid="";
    /**
     * 平台uid(必填),通过getPassport()方法得到
     */
    public String openuid="";
    /**
     * 当选择捷讯渠道时,需要区分电信、联通、移动卡类型 (1:移动 2:联通 3:电信)
     */
    public String pay_type="10";
    
    /**
     * 捷讯支付渠道时的储值卡号 
     */
    public String card_id="";
    /**
     * 捷讯支付渠道时的储值卡密码
     */
    public String card_pass="";
    
    /**
     * 购买的游戏商品名称（必填）
     */
    public String productname="";
    /**
     * 购买的游戏商品的价格（必填）
     */
    public String productprice="";
    /**
     * 购买的游戏商品的数量（必填）
     */
    public String productcount="";

    /**
     * 当选用易宝支付渠道时,需传递手机设备号
     */
    public  String imei="";
    /**
     * 当选用易宝绑卡支付时,需传递绑定号
     */
    public String bindid="";
    
    /**
     * token验证,通过getToken()方法得到（必填）
     */
    public String token = "";
    
    /**
     * platform平台，通过getPlatform()方法获得（比填）
     */
 
    public String platform = "";
    
    /**
     * 额外扩展参数1
     */
    
    public String appExtra1 = "";
    
    /**
     * 额外参数2
     */
    public String appExtra2 = "";
}
