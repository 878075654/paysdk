package com.xy.xypay.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.xy.xypay.bean.CreditCardBean;
import com.xy.xypay.utils.ResourceUtil;

/**
 * @time	2014年1月20日17:45:12
 * @author ruanwei
 * @detail
 * 			用来展示充值面额的适配器;	
 */
public class PayCountAdapter extends BaseAdapter{
	private Context mContext;
	private ViewHolder holder;
	private List<String> mList;
	
	public PayCountAdapter(){}
	
	public PayCountAdapter(Context context,List<String> list){
		this.mContext = context;
		this.mList = list;
	}
	@Override
	public int getCount() {
		if (mList != null){
			return mList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (mList != null){
			mList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	class ViewHolder{
		TextView textView;//面额
	}
	@Override
	public View getView(final  int position, View convertView, ViewGroup parent) {
		if (convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(ResourceUtil.getLayoutId(mContext, "adapter_paycount_listview_item"), null);
			holder.textView = (TextView)convertView.findViewById(ResourceUtil.getId(mContext, "textview"));
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		String payCount = mList.get(position);
		if (payCount != null){
			holder.textView.setText(payCount);
		}
		return convertView;
	}

}
