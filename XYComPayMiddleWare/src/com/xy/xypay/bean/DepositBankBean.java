package com.xy.xypay.bean;

/**
 * @time	2014年1月16日14:01:57
 * @author ruanwei
 * @detail
 * 		支持的储蓄卡实体数据bean,关联到的适配器是DepositBankAdapter;
 *
 */
public class DepositBankBean {

	public DepositBankBean(){}
	
	public String name;//银行名称
	public int drawableId;//对应的银行图片资源id
	
	public DepositBankBean(String bankname,int drawableId){
		this.name = bankname;
		this.drawableId = drawableId;
	}
	
}
