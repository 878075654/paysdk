package com.xy.xypay.bean;

/**
 * @time	2014年1月16日14:01:57
 * @author ruanwei
 * @detail
 * 		支持的信用卡实体数据bean,关联到的适配器是CreditCardAdapter;
 *
 */
public class CreditCardBean {

	public CreditCardBean(){}
	
	public String name;//信用卡对应的银行名称
	public int drawableId;//信用卡对应的银行图片资源id
	
	public CreditCardBean(String bankname,int drawableId){
		this.name = bankname;
		this.drawableId = drawableId;
	}
	
}
