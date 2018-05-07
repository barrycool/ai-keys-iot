package com.ai_keys.iot.ui.device;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ai_keys.iot.R;

public class DeviceScenesFragment extends Fragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.device_add_scenes_fragment, container, false);
        
		return view;
	}
}
