package com.ai_keys.iot.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.ai_keys.iot.R;
import com.ai_keys.iot.account.AccountManager;
import com.ai_keys.iot.net.HttpManager;
import com.ai_keys.iot.net.HttpManagerInterface;
import com.ai_keys.iot.tools.XLogger;
import com.ai_keys.iot.ui.main.EspMainActivity;
import com.ai_keys.iot.ui.register.RegisterActivity;
import com.ai_keys.iot.util.EspStrings;
import com.ai_keys.iot.util.SharedPrefUtils;
import com.ai_keys.iot.util.ToastUtils;

import org.json.JSONObject;

public class LoginActivity extends Activity implements OnClickListener, OnEditorActionListener {

    private EditText mEmailEdt;
    private EditText mPasswordEdt;

    private Button mLoginBtn;
    private Button mRegisterBtn;
    private TextView mForgetPwdTV;
    private TextView mThirdPartyLoginTV;

    private final static int REQUEST_REGISTER = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);
        init();
    }

    private void init() {
        mEmailEdt = (EditText)findViewById(R.id.login_edt_account);
        mPasswordEdt = (EditText)findViewById(R.id.login_edt_password);
        mPasswordEdt.setOnEditorActionListener(this);

        mLoginBtn = (Button)findViewById(R.id.login_btn_login);
        mLoginBtn.setOnClickListener(this);

        mRegisterBtn = (Button)findViewById(R.id.login_btn_register);
        mRegisterBtn.setOnClickListener(this);

        mThirdPartyLoginTV = (TextView)findViewById(R.id.login_text_third_party);
        mThirdPartyLoginTV.setOnClickListener(this);

        mForgetPwdTV = (TextView)findViewById(R.id.forget_password_text);
        mForgetPwdTV.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v == mLoginBtn) {
            login();
        } else if (v == mRegisterBtn) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivityForResult(intent, REQUEST_REGISTER);
        } else if (v == mThirdPartyLoginTV) {

        } else if (v == mForgetPwdTV) {
            startActivity(new Intent(this, ResetUserPasswordActivity.class));
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v == mPasswordEdt) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login();
                return true;
            }
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_REGISTER) {
            if (resultCode == RESULT_OK) {
                // Register completed, set the new account email and password
                String email = data.getStringExtra(EspStrings.Key.REGISTER_NAME_EMAIL);
                String password = data.getStringExtra(EspStrings.Key.REGISTER_NAME_PASSWORD);
                mEmailEdt.setText(email);
                mPasswordEdt.setText(password);
            }
        }
    }

    private void login() {
    	
        try {
        	final String userName = mEmailEdt.getText().toString().trim();
        	final String passwd = mPasswordEdt.getText().toString().trim();
        	
            JSONObject register = new JSONObject();
            register.put("loginName", userName);
            register.put("passwd", passwd);
            
            HttpManager.getInstances().requestLogin(this, register.toString(), new HttpManagerInterface() {
    			
    			@Override
    			public void onRequestResult(int flag, String msg) {
    				if(flag == HttpManagerInterface.REQUEST_OK){
    					try {
							JSONObject content = new JSONObject(msg);
							JSONObject result = new JSONObject(content.optString("result"));
							String code = result.optString("code");
							String message = result.optString("msg");
							String userInfo = result.optString("userInfo");
							if("OK".equals(code)){
								AccountManager.getInstance().saveUserInfo(LoginActivity.this, userInfo);
								SharedPrefUtils.saveLoginTime(LoginActivity.this, System.currentTimeMillis());
		    					startActivity(new Intent(LoginActivity.this, EspMainActivity.class));
		    					LoginActivity.this.finish();
							}else{
								XLogger.d("showToast");
								ToastUtils.showToast(LoginActivity.this, "登录失败:" + message);
							}
						} catch (Exception e) {
							e.printStackTrace();
							XLogger.d("Exception");
						}
    				}else{
    					ToastUtils.showToast(LoginActivity.this, "登录失败");
    				}
    			}
    		});
		} catch (Exception e) {

		}
    }

    private void loginSuccess() {
        setResult(RESULT_OK);
        finish();
    }
}
