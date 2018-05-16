package com.ai_keys.iot.ui.device;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ai_keys.iot.R;

public class DeviceSettingMoreActivity extends Activity {
    private TextView mTitleTxt;
    private String device_id;
    private String friendlyName;
    private TextView deviceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_setting_more);

        Intent intent = getIntent();
        device_id = intent.getStringExtra("deviceId");
        friendlyName = intent.getStringExtra("friendlyName");

        deviceName = (TextView) findViewById(R.id.tv_devicename);
        deviceName.setText(friendlyName);

        mTitleTxt = (TextView) findViewById(R.id.title_txt);
        mTitleTxt.setText(R.string.device_setting_more_title);

        ImageView IV_back = (ImageView) findViewById(R.id.add_device_back);
        IV_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
