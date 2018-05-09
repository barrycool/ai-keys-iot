package com.ai_keys.iot.ui.device;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ai_keys.iot.R;

public class PairFailActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair_failure);

        TextView textView = (TextView) findViewById(R.id.tv_pair_failure_retry);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PairFailActivity.this, AddDeviceStep2.class));
                finish();
            }
        });

        ImageView IV_back = (ImageView) findViewById(R.id.add_device_back);
        IV_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PairFailActivity.this, AddDeviceStep2.class));
                finish();
            }
        });
    }
}
