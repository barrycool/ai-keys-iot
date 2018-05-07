package com.ai_keys.iot.ui.main;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ai_keys.iot.R;
import com.ai_keys.iot.tools.XLogger;
import com.ai_keys.iot.ui.device.AddDeviceStep1;
import com.ai_keys.iot.ui.device.DeviceAllFragment;

public class EspMainActivity extends Activity {


    private EspMainDeviceFragment mMainFragment;
    private UserCenterFragment mUserCenterFragment;
    private DeviceAllFragment mDeviceAllFragment;

    public static final int REQUEST_LOGIN = 0x10;
    public static final int REQUEST_ESPTOUCH = 0x11;
    public static final int REQUEST_ENTRY_USERCENTER = 0x12;
    public static final int REQUEST_ADD_DEVICE = 0x13;

    private SharedPreferences mSettingsShared;

    private static final int MENU_ID_SYNC_GROUP = 0x01;
    private static final int MENU_ID_SORT_DEVICE = 0x02;
    
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_esp_activity);

        mFragmentManager = getFragmentManager();
        mMainFragment = new EspMainDeviceFragment();
        mUserCenterFragment = new UserCenterFragment();
        
        mFragmentManager.beginTransaction().replace(R.id.container, mMainFragment).commit();
        
        initBottomView();
    }
    
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButtonDevice;
    private RadioButton mRadioButtonAdd;
    private RadioButton mRadioButtonUser;
    
    private void initBottomView() {
        mRadioGroup = (RadioGroup) findViewById(R.id.bottom_radio_group_button);
        mRadioButtonDevice = (RadioButton) findViewById(R.id.radio_button_device);
        mRadioButtonAdd = (RadioButton) findViewById(R.id.radio_button_add);
        mRadioButtonUser = (RadioButton) findViewById(R.id.radio_button_user);
        
        Drawable drawableDevice = getResources().getDrawable(R.drawable.btn_bottom_device_selector);
        Drawable drawableAdd = getResources().getDrawable(R.drawable.bottom_add);
        drawableAdd.setBounds(0, 0, 128, 128);
        mRadioButtonAdd.setCompoundDrawables(null, drawableAdd, null, null);
        Drawable drawableUser = getResources().getDrawable(R.drawable.btn_bottom_user_selector);
        
        mRadioButtonAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//startActivityForResult(new Intent(EspMainActivity.this, DeviceEspTouchActivity.class), REQUEST_ADD_DEVICE);
				startActivityForResult(new Intent(EspMainActivity.this, AddDeviceStep1.class), REQUEST_ADD_DEVICE);
			}
		});
        
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            Fragment mFragment = null;
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio_button_device:
                    	mFragmentManager.beginTransaction().replace(R.id.container, mMainFragment).commit();
                        break;
                    case R.id.radio_button_add:
                        break;
                    case R.id.radio_button_user:
                    	//startActivityForResult(new Intent(EspMainActivity.this, UserCenterFragment.class), REQUEST_ENTRY_USERCENTER);
                    	mFragmentManager.beginTransaction().replace(R.id.container, mUserCenterFragment).commit();
                        break;
                }
//                if(mFragments!=null){
//                    getSupportFragmentManager().beginTransaction().replace(R.id.container,mFragment).commit();
//                }
            }
        });

        mRadioButtonDevice.setChecked(true);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(R.string.app_name);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LOGIN) {
            if (resultCode == RESULT_OK) {
                //onLogin();
            }
        } else if (requestCode == REQUEST_ESPTOUCH) {
            mMainFragment.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == REQUEST_ENTRY_USERCENTER){
        	mRadioButtonDevice.setChecked(true);
        } else if (requestCode == REQUEST_ADD_DEVICE){
        	//if (resultCode == RESULT_OK) {
	        	//mMainFragment.setArguments(data.getExtras());
	        	mFragmentManager.beginTransaction().replace(R.id.container, mMainFragment).commit();
	        	XLogger.d("on add device complete!");
        	//}
        }
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this).setMessage(R.string.esp_main_exit_message)
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new FinishTask(EspMainActivity.this).execute();
                }
            })
            .setNegativeButton(android.R.string.cancel, null)
            .show();
    }

    private class FinishTask extends AsyncTask<Void, Void, Void> {
        private EspMainActivity mActivity;
        private ProgressDialog mDialog;

        public FinishTask(EspMainActivity activity) {
            mActivity = activity;
        }

        @Override
        protected void onPreExecute() {
            mDialog = new ProgressDialog(mActivity);
            mDialog.setMessage(mActivity.getString(R.string.esp_main_exiting));
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (mDialog != null) {
                mDialog.dismiss();
                mDialog = null;
            }
            mActivity.finish();
        }
    }
}
