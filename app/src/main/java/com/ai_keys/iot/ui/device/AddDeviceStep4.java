package com.ai_keys.iot.ui.device;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ai_keys.iot.R;
import com.ai_keys.iot.ui.main.EspMainActivity;

public class AddDeviceStep4 extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_name);

		Button button = (Button) findViewById(R.id.device_add_finish);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(AddDeviceStep4.this, EspMainActivity.class));
				finish();
			}
		});

		TextView textView = (TextView) findViewById(R.id.tv_wait_paring_cancel);
		textView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {finish();
			}
		});
	}
}
