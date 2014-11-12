package com.xy.xypay.inters;

/**
 * 支付的回调接口
 * @param status 支付的状码(有支付成功、失败、取消三种状态,常量值见PayUtils类)
 */
public interface XYPayCallback {
	public void onPayFinished(int status);
}
