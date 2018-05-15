package com.ai_keys.iot.ui.device;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ai_keys.iot.R;

public class DeviceSettingMoreActivity extends Activity {
    private TextView mTitleTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_setting_more);

        mTitleTxt = (TextView) findViewById(R.id.title_txt);
        mTitleTxt.setText(R.string.device_setting_more_title);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
