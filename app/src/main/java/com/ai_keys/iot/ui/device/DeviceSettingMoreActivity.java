package com.ai_keys.iot.ui.device;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ai_keys.iot.R;

public class DeviceSettingMoreActivity extends Activity {
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_setting_more);

        title = (TextView) findViewById(R.id.device_detail_title);
        title.setText(R.string.device_setting_more);

        ImageView iv_setting = (ImageView) findViewById(R.id.device_detail_more_setting);
        iv_setting.setVisibility(View.INVISIBLE);

        ImageView imageView = (ImageView) findViewById(R.id.device_detail_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
