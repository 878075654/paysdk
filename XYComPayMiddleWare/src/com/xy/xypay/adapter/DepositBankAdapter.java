package com.xy.xypay.adapter;

import java.util.List;

import android.R.raw;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.xy.xycompaymiddleware.R;
import com.xy.xypay.bean.DepositBankBean;
import com.xy.xypay.utils.ResourceUtil;

/**
 * @time	2014年1月16日16:04:32
 * @author ruanwei
 * @detail
 * 			储蓄银行展示列表的适配器，用到的实体数据Bean是DepositBankBean;	
 */
public class DepositBankAdapter extends BaseAdapter{
	private Context mContext;
	private ViewHolder holder;
	private List<DepositBankBean> mList;
	
	public DepositBankAdapter(){}
	
	public DepositBankAdapter(Context context,List<DepositBankBean> list){
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
		ImageView bankIView;//银行图标
		TextView bankTView;//银行名称
	}
	@Override
	public View getView(final  int position, View convertView, ViewGroup parent) {
		if (convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(ResourceUtil.getLayoutId(mContext, "adapter_depositbank_gridview_item"), null);
			holder.bankIView = (ImageView)convertView.findViewById(ResourceUtil.getId(mContext, "bankiv"));
			holder.bankTView = (TextView)convertView.findViewById(ResourceUtil.getId(mContext, "banktv"));
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		DepositBankBean depositBankBean = mList.get(position);
		if (depositBankBean != null){
			holder.bankTView.setText(depositBankBean.name);
			holder.bankIView.setBackgroundResource(depositBankBean.drawableId);
		}
		return convertView;
	}

}
