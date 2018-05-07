package com.ai_keys.iot.ui.main;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ai_keys.iot.R;
import com.ai_keys.iot.ui.device.DeviceAllFragment;
import com.ai_keys.iot.ui.device.DeviceScenesFragment;

public class EspMainDeviceFragment extends Fragment implements OnClickListener{

	private TextView mTv_all_device;
	private TextView mTv_scene;
	private DeviceAllFragment mDeviceAllFragment;
	private DeviceScenesFragment mDeviceScenesFragment;
	private FragmentManager mFmanager;
	
	private ImageView mImg_scanDevice;
	
    public static final int REQUEST_SCAN = 1;
    
    public Bundle data;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mFmanager = getFragmentManager();
		data = getArguments();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.device_main_fragment, container, false);
        mTv_all_device = (TextView) view.findViewById(R.id.tv_all_device);
        mTv_all_device.setOnClickListener(this);
        
        mTv_scene = (TextView) view.findViewById(R.id.tv_scene);
        mTv_scene.setOnClickListener(this);
        
        mImg_scanDevice = (ImageView) view.findViewById(R.id.scan_device);
        mImg_scanDevice.setOnClickListener(this);

        mDeviceAllFragment = new DeviceAllFragment();
        mDeviceScenesFragment = new DeviceScenesFragment();
        
        mDeviceAllFragment.setArguments(data);
        mFmanager.beginTransaction().replace(R.id.mydevice_container, mDeviceAllFragment).commit();

		return view;
	}
	
	private void setSelect(TextView view){
		int colorBlue = getResources().getColor(R.color.esp_color_blue);
		int colorWhite = getResources().getColor(R.color.white);
		Drawable left = getResources().getDrawable(R.drawable.device_top_bg_left);
		Drawable right = getResources().getDrawable(R.drawable.device_top_bg_right);
		
		if(view == mTv_all_device){
			mTv_all_device.setTextColor(colorBlue);
			mTv_all_device.setBackgroundDrawable(left);
			
			mTv_scene.setTextColor(colorWhite);
			mTv_scene.setBackgroundDrawable(null);
		}else{
			mTv_all_device.setTextColor(colorWhite);
			mTv_all_device.setBackgroundDrawable(null);
			
			mTv_scene.setTextColor(colorBlue);
			mTv_scene.setBackgroundDrawable(right);
		}
	}
	
	@Override
	public void onClick(View view) {
		if(view == mTv_all_device){
			setSelect((TextView)view);
			mFmanager.beginTransaction().replace(R.id.mydevice_container, mDeviceAllFragment).commit();
		}else if(view == mTv_scene){
			setSelect((TextView)view);
			mFmanager.beginTransaction().replace(R.id.mydevice_container, mDeviceScenesFragment).commit();
		}else if(view == mImg_scanDevice){

		}
	}
	
	public static final String INTENT_EXTRA_KEY_QR_SCAN = "qr_scan_result";

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 扫描结果回调
		if (requestCode == REQUEST_SCAN && resultCode == Activity.RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString(INTENT_EXTRA_KEY_QR_SCAN);
			// 将扫描出的信息显示出来
			Toast.makeText(getActivity(), "result:" + scanResult, Toast.LENGTH_SHORT).show();
		}
	}
}
