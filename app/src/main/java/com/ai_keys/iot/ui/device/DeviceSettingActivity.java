package com.ai_keys.iot.ui.device;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.print.PrinterId;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ai_keys.iot.R;
import com.ai_keys.iot.net.HttpManager;
import com.ai_keys.iot.net.HttpManagerInterface;
import com.ai_keys.iot.tools.XLogger;

import org.json.JSONArray;
import org.json.JSONObject;

public class DeviceSettingActivity extends Activity{
    private String device_id;
    private TextView device_name;
    private TextView tv_error_tip;
    private ImageView device_status;
    private Handler uiHandler = new Handler(Looper.getMainLooper());
    private String lastPowerState;
    private RelativeLayout device_setting_bg;

    private LinearLayout mPopSetting;
    private LinearLayout mPopDelete;
    private LinearLayout mPopRecord;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dimming_lamp_detail_layout);

        device_setting_bg = (RelativeLayout) findViewById(R.id.rl_device_setting);

        Intent intent = getIntent();
        device_id = intent.getStringExtra("device_id");


        device_name = (TextView) findViewById(R.id.device_detail_title);
        device_name.setText(intent.getStringExtra("device_name"));
        tv_error_tip = (TextView) findViewById(R.id.tv_error_tip);

        final ImageView iv_setting = (ImageView) findViewById(R.id.device_detail_more_setting);
        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(DeviceSettingActivity.this, DeviceSettingMoreActivity.class));
                View popupView = DeviceSettingActivity.this.getLayoutInflater().inflate(R.layout.setting_popup, null);

                PopupWindow window = new PopupWindow(popupView, 300, 400);
                window.setFocusable(true);
                window.setOutsideTouchable(true);
                window.update();
                window.showAsDropDown(iv_setting, 0, 0);

                mPopSetting = (LinearLayout) popupView.findViewById(R.id.popup_setting_layout);
                mPopSetting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(DeviceSettingActivity.this, DeviceSettingMoreActivity.class));
                    }
                });

                mPopDelete = (LinearLayout) popupView.findViewById(R.id.popup_delete_layout);
                mPopDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                mPopRecord = (LinearLayout) popupView.findViewById(R.id.popup_record_layout);
                mPopRecord.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        });

        device_status = (ImageView) findViewById(R.id.iv_switch);
        device_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctrl_device_power();
            }
        });

		ImageView imageView = (ImageView) findViewById(R.id.device_detail_back);
		imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setDevice_status(intent.getStringExtra("device_power_status"), intent.getStringExtra("device_connectivity_status"));

        fresh_device_state();
	}

    @Override
    protected void onResume() {
        super.onResume();
        fresh_device_state();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void setDevice_status(final String powerState, final String connectivityState) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                if ("OK".equals(connectivityState)) {
                    device_setting_bg.setBackgroundResource(R.color.esp_color_blue);
                    tv_error_tip.setVisibility(View.INVISIBLE);
                    if (powerState.equals("ON")) {
                        device_status.setImageResource(R.drawable.dimming_lamp_detail_on);
                    } else {
                        device_status.setImageResource(R.drawable.dimming_lamp_detail_off);
                    }
                    lastPowerState = powerState;
                }
                else {
                    device_setting_bg.setBackgroundResource(R.color.devicelist_offline);
                    tv_error_tip.setVisibility(View.VISIBLE);
                    device_status.setImageResource(R.drawable.dimming_lamp_detail_offline);
                }
            }
        });
    }

    private void ctrl_device_power() {
	    String powerCtrl;
	    if (lastPowerState.equals("ON")) {
            powerCtrl = "TurnOff";
        }
        else {
            powerCtrl = "TurnOn";
        }

        HttpManager.getInstances().requestDevicePowerCtrl(this, device_id, powerCtrl, new HttpManagerInterface() {
            @Override
            public void onRequestResult(int flag, String msg) {
                if(flag == HttpManagerInterface.REQUEST_OK){
                    try {
                        JSONObject content = new JSONObject(msg);
                        JSONArray prop = content.optJSONArray("properties");
                        String powerState = "OFF";
                        String connectivityState = "UNREACHABLE";
                        for(int i=0; i<prop.length(); i++){
                            JSONObject status = prop.optJSONObject(i);
                            String namespace = status.optString("name_space");
                            if("Alexa.PowerController".equals(namespace)){
                                powerState = status.optString("value");
                                XLogger.d("Alexa.PowerController:" + powerState);
                            }else if("Alexa.EndpointHealth".equals(namespace)){
                                JSONObject connectivity_value = status.optJSONObject("value");
                                connectivityState = connectivity_value.optString("value");
                                XLogger.d("connectivity:" + connectivityState);
                            }
                        }

                        setDevice_status(powerState, connectivityState);
                    }catch (Exception e){
                    }
                }
            }
        });
    }

    private void fresh_device_state() {
        HttpManager.getInstances().requestDeviceStatus(this, device_id, new HttpManagerInterface() {
            @Override
            public void onRequestResult(int flag, String msg) {
                if(flag == HttpManagerInterface.REQUEST_OK){
                    try {
                        JSONObject content = new JSONObject(msg);
                        JSONArray prop = content.optJSONArray("properties");
                        String powerState = "OFF";
                        String connectivityState = "UNREACHABLE";
                        for(int i=0; i<prop.length(); i++){
                            JSONObject status = prop.optJSONObject(i);
                            String namespace = status.optString("name_space");
                            if("Alexa.PowerController".equals(namespace)){
                                powerState = status.optString("value");
                                XLogger.d("Alexa.PowerController:" + powerState);
                            }else if("Alexa.EndpointHealth".equals(namespace)){
                                JSONObject connectivity_value = status.optJSONObject("value");
                                connectivityState = connectivity_value.optString("value");
                                XLogger.d("connectivity:" + connectivityState);
                            }
                        }

                        setDevice_status(powerState, connectivityState);
                    }catch (Exception e){

                    }
                }
            }
        });
    }
}
