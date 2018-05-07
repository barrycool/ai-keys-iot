package com.ai_keys.iot.ui.device;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.ai_keys.iot.R;
import com.ai_keys.iot.esptouch.EspWifiAdminSimple;

public class AddDeviceStep2 extends Activity implements View.OnClickListener{
    private Button bt_next;
    private TextView TV_ssid;
    private TextView TV_pwd;
    private SharedPreferences sharedPreferences;
    private EspWifiAdminSimple mWifiAdmin;
    private TextView tv_change_net;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_device);

        mWifiAdmin = new EspWifiAdminSimple(this);

        String ssid = mWifiAdmin.getWifiConnectedSsid();
        if (ssid == null)
        {
            ssid = "";
        }
        TV_ssid = (TextView) findViewById(R.id.tv_ssid);
        TV_ssid.setText(ssid);

        sharedPreferences = getSharedPreferences("wifiInfo", Context.MODE_PRIVATE);
        String wifiSSID = sharedPreferences.getString("wifiSSID", "");
        final String wifiPWD = sharedPreferences.getString("wifiPWD", "");

        TV_pwd = (TextView) findViewById(R.id.tv_pwd);
        if (wifiSSID.equals(ssid)) {
            TV_pwd.setText(wifiPWD);
        }

        bt_next = (Button) findViewById(R.id.bt_confirm);
        bt_next.setOnClickListener(this);

        tv_change_net = (TextView) findViewById(R.id.tv_change_net);
        tv_change_net.setOnClickListener(this);

        ImageView IV_back = (ImageView) findViewById(R.id.add_device_back);
        IV_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
	}

    @Override
    protected void onResume() {
        super.onResume();
        String ssid = mWifiAdmin.getWifiConnectedSsid();
        if (ssid == null)
        {
            ssid = "";
        }
        TV_ssid.setText(ssid);
    }

    @Override
    public void onClick(View view) {
        if (view == bt_next) {
            String wifiSSID = mWifiAdmin.getWifiConnectedSsid();
            if (wifiSSID == null || wifiSSID.length() == 0) {
                new AlertDialog.Builder(this)
                        .setTitle("wifi SSID")
                        .setMessage("please connect wifi first")
                        .setNegativeButton("comfirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create()
                        .show();

                return;
            }

            if (TV_pwd.getText().toString().length() < 8) {
                new AlertDialog.Builder(this)
                        .setTitle("wifi password")
                        .setMessage("passwd is too short")
                        .setNegativeButton("comfirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create()
                        .show();

                return;
            }

            CheckBox save_wifi = (CheckBox) findViewById(R.id.cb_save);
            if (save_wifi.isChecked()) {
                SharedPreferences.Editor sharedPreferences_edit = sharedPreferences.edit();
                sharedPreferences_edit.putString("wifiSSID", mWifiAdmin.getWifiConnectedSsid());
                sharedPreferences_edit.putString("wifiPWD", TV_pwd.getText().toString());
                sharedPreferences_edit.apply();
            }

            Intent intent = new Intent(AddDeviceStep2.this, AddDeviceStep3.class);
            intent.putExtra("wifiSSID", mWifiAdmin.getWifiConnectedSsid());
            intent.putExtra("wifiPWD", TV_pwd.getText().toString());

            startActivity(intent);
        }
        else if (view == tv_change_net) {
            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        }
    }
}
