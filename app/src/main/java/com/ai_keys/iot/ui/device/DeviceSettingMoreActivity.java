package com.ai_keys.iot.ui.device;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ai_keys.iot.R;
import com.ai_keys.iot.net.HttpManager;

public class DeviceSettingMoreActivity extends Activity {
    private TextView mTitleTxt;
    private String deviceId;
    private String friendlyName;
    private TextView deviceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_setting_more);

        mTitleTxt = (TextView) findViewById(R.id.title_txt);
        mTitleTxt.setText(R.string.device_setting_more_title);

        Intent intent = getIntent();
        deviceId = intent.getStringExtra("deviceId");
        friendlyName = intent.getStringExtra("friendlyName");

        deviceName = (TextView) findViewById(R.id.tv_devicename);
        deviceName.setText(friendlyName);



        ImageView IV_modifyName = (ImageView) findViewById(R.id.setting_name);
        IV_modifyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNewName();
            }
        });

        ImageView IV_back = (ImageView) findViewById(R.id.add_device_back);
        IV_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void getNewName() {
        final EditText newName = new EditText(this);
        newName.setText(friendlyName);
        newName.setSelection(friendlyName.length());
        new AlertDialog.Builder(this)
            .setTitle(R.string.device_setting_more_rename_device)
            .setView(newName)
            .setPositiveButton(R.string.device_setting_more_confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String name = newName.getText().toString();
                    Toast.makeText(getApplicationContext(),  name, Toast.LENGTH_SHORT).show();
                    if (!name.isEmpty() && !name.equals(friendlyName)) {
                        friendlyName = name;
                        deviceName.setText(friendlyName);
                        HttpManager.getInstances().requestDeviceUpdateFriendlyName(getApplicationContext(), deviceId, friendlyName, null);
                    }
                }
            })
            .setNegativeButton(R.string.device_setting_more_cancel, null)
            .show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
