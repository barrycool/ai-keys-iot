package com.ai_keys.iot.ui.device;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.ai_keys.iot.R;

public class AddDeviceStep3 extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.esp_wait_page);

		ProgressBar progressBar = (ProgressBar) findViewById(R.id.pb_wait_paring_progress);
	}
}
