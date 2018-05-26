package com.ai_keys.iot.ui.device;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ai_keys.iot.R;
import com.ai_keys.iot.net.HttpManager;
import com.ai_keys.iot.net.HttpManagerInterface;
import com.ai_keys.iot.tools.Constant;
import com.ai_keys.iot.ui.main.EspMainActivity;

public class AddDeviceStep4 extends Activity{
	private EditText device_new_name;
	private String deviceId;
	private String friendlyName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_name);

		Intent intent = getIntent();
		deviceId = intent.getStringExtra("deviceId");
		friendlyName = intent.getStringExtra("friendlyName");

		device_new_name = (EditText) findViewById(R.id.device_new_name);
		device_new_name.setText(intent.getStringExtra("friendlyName"));
		device_new_name.setSelection(device_new_name.getText().length());

		Button button = (Button) findViewById(R.id.device_add_finish);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!device_new_name.getText().equals(friendlyName)) {
					HttpManager.getInstances().requestDeviceUpdateFriendlyName(getApplicationContext(), deviceId, device_new_name.getText().toString(), new HttpManagerInterface() {
						@Override
						public void onRequestResult(int flag, String msg) {
							Intent intent = new Intent(Constant.DEVICE_ADD_COMPLETE);
							intent.putExtra("deviceId", deviceId);
							intent.putExtra("friendlyName", device_new_name.getText().toString());
							LocalBroadcastManager.getInstance(AddDeviceStep4.this).sendBroadcast(intent);
						}
					});
				}

				startActivity(new Intent(AddDeviceStep4.this, EspMainActivity.class));
				finish();
			}
		});

		ImageView IV_back = (ImageView) findViewById(R.id.add_device_back);
		IV_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(AddDeviceStep4.this, AddDeviceStep2.class));
				finish();
			}
		});
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		startActivity(new Intent(AddDeviceStep4.this, AddDeviceStep2.class));
		finish();
	}
}
