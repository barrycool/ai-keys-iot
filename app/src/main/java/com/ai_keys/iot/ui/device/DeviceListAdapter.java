package com.ai_keys.iot.ui.device;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ai_keys.iot.R;

import java.util.List;

public class DeviceListAdapter extends BaseAdapter{

	private Context mContext;
	private List<DeviceInfoBean> mDeviceInfoList;
	private ListClickListener mListClickListener;
	
	public DeviceListAdapter(Context context, List<DeviceInfoBean> deviceInfoList) {
		mContext = context;
		mDeviceInfoList = deviceInfoList;
	}
	
	public void setListClickListener(ListClickListener listClickListener){
		mListClickListener = listClickListener;
	}
	
	public interface ListClickListener{
		void onSwitchClick(int position);
		void onDeleteClick(int position);
		void onClockClick(int position);
		void onWifiClick(int position);
		void onItemClick(int position);
	}
	
	@Override
	public int getCount() {
		return mDeviceInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		if(position < mDeviceInfoList.size() && position >= 0){
			return mDeviceInfoList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	class ViewHolder{
		RelativeLayout device_item;
		ImageView switch_img;
		ImageView wifi_img;
		ImageView clock_img;
		ImageView delete_img;
		TextView device_name;
		TextView device_status;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.device_item, null);
			mHolder = new ViewHolder();
			mHolder.switch_img = (ImageView) convertView.findViewById(R.id.device_switch);
			mHolder.wifi_img = (ImageView) convertView.findViewById(R.id.device_wifi);
			//mHolder.clock_img = (ImageView) convertView.findViewById(R.id.device_clock);
			//mHolder.delete_img = (ImageView) convertView.findViewById(R.id.device_delete);
			mHolder.device_name = (TextView) convertView.findViewById(R.id.device_name);
			mHolder.device_status = (TextView) convertView.findViewById(R.id.device_status);
			mHolder.device_item = (RelativeLayout) convertView.findViewById(R.id.device_item);
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		
		mHolder.device_name.setText(mDeviceInfoList.get(position).getDevice_name());

		if (mDeviceInfoList.get(position).getDevice_connectivity_status() != null &&
		    mDeviceInfoList.get(position).getDevice_connectivity_status().equals("OK")) {
			mHolder.wifi_img.setImageResource(R.drawable.device_online_wifi);
			mHolder.device_status.setText(R.string.device_connection_online_status);
			mHolder.device_item.setBackgroundResource(R.color.device_online);
		}
		else {
			mHolder.device_item.setBackgroundResource(R.color.devicelist_offline);
			mHolder.wifi_img.setImageResource(R.drawable.device_offline_wifi);
			mHolder.device_status.setText(R.string.device_connection_offline_status);
		}

		if (mDeviceInfoList.get(position).getDevice_status() != null &&
				mDeviceInfoList.get(position).getDevice_status().equals("ON")) {
			mHolder.switch_img.setImageResource(R.drawable.detail_power_on);
		}
		else {
			mHolder.switch_img.setImageResource(R.drawable.detail_power_off);
		}

		mHolder.switch_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mListClickListener.onSwitchClick(position);
			}
		});
		
		mHolder.wifi_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mListClickListener.onWifiClick(position);
			}
		});

		mHolder.device_item.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				mListClickListener.onItemClick(position);
			}
		});
		
		/*mHolder.clock_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mListClickListener.onClockClick(position);
			}
		});
		mHolder.delete_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mListClickListener.onDeleteClick(position);
			}
		});*/
		
		return convertView;
	}
}
