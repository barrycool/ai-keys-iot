package com.ai_keys.iot.ui.register;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ai_keys.iot.R;
import com.ai_keys.iot.net.HttpManager;
import com.ai_keys.iot.net.HttpManagerInterface;
import com.ai_keys.iot.tools.XLogger;
import com.ai_keys.iot.util.AccountUtil;
import com.ai_keys.iot.util.EspStrings;
import com.ai_keys.iot.util.ToastUtils;

import org.json.JSONObject;

public class RegisterEmailFragment extends Fragment implements OnClickListener, OnFocusChangeListener
{
    public static final String TAG = "RegisterEmailFragment";

    private EditText mUsernameEdt;
    private EditText mEmailEdt;
    private EditText mPasswordEdt;
    private EditText mPasswordAgainEdt;
    private TextView mWithPhoneTV;
    private EditText mEmailCodeEdt;
    
    private Button mCancelBtn;
    private Button mRegisterBtn;
    private Button mGetEmailCodeBtn;
    
    private Handler mHandler;
    
    private static final int FIND_USERNAME_EXIST = 0;
    private static final int FIND_EMAIL_EXIST = 1;
    
    private RegisterActivity mActivity;
    
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        
        mActivity = (RegisterActivity)activity;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    	XLogger.d("onCreateView");
        View view = inflater.inflate(R.layout.register_email_fragment, container, false);
        
        mUsernameEdt = (EditText)view.findViewById(R.id.register_username);
        mUsernameEdt.addTextChangedListener(new FilterSpaceTextListener(mUsernameEdt));
        mUsernameEdt.setOnFocusChangeListener(this);
        
        mEmailEdt = (EditText)view.findViewById(R.id.register_email);
        mEmailEdt.addTextChangedListener(new FilterSpaceTextListener(mEmailEdt));
        mEmailEdt.setOnFocusChangeListener(this);
        
        mPasswordEdt = (EditText)view.findViewById(R.id.register_password);
        mPasswordAgainEdt = (EditText)view.findViewById(R.id.register_password_again);
        
        mWithPhoneTV = (TextView)view.findViewById(R.id.register_with_phone);
        mWithPhoneTV.setOnClickListener(this);
        mWithPhoneTV.setVisibility(View.GONE);
        
        mCancelBtn = (Button)view.findViewById(R.id.register_cancel);
        mCancelBtn.setOnClickListener(this);
        mRegisterBtn = (Button)view.findViewById(R.id.register_register);
        mRegisterBtn.setOnClickListener(this);
        
        mGetEmailCodeBtn = (Button)view.findViewById(R.id.register_get_email_code);
        mGetEmailCodeBtn.setOnClickListener(this);
        
        mEmailCodeEdt = (EditText)view.findViewById(R.id.register_email_code);
        return view;
    }
    
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
    }
    
    @Override
    public void onClick(View v)
    {
        if (v == mCancelBtn)
        {
            getActivity().finish();
        }
        else if (v == mRegisterBtn)
        {
            registerEmail();
        }
        else if (v == mWithPhoneTV)
        {
            mActivity.showFragment(RegisterPhoneFragment.TAG);
        }
        else if(v == mGetEmailCodeBtn){
        	getEmailCode();
        }
    }
    
    private void getEmailCode(){
        try {
        	final String email = mEmailEdt.getText().toString().trim();
        	if(TextUtils.isEmpty(email)){
        		ToastUtils.showToast(mActivity, "邮箱不能为空");
        		return;
        	}
        	
            HttpManager.getInstances().requestEmailCode(mActivity, email, new HttpManagerInterface() {
    			
    			@Override
    			public void onRequestResult(int flag, String msg) {
    				if(flag == HttpManagerInterface.REQUEST_OK){
    					try {
							JSONObject content = new JSONObject(msg);
							JSONObject result = new JSONObject(content.optString("result"));
							String code = result.optString("code");
							String message = result.optString("msg");
							if("OK".equals(code)){
		    					ToastUtils.showToast(mActivity, "注册码获取成功");
							}else{
								ToastUtils.showToast(mActivity, "注册码获取失败:" + message);
							}
    					}catch(Exception e){
    						
    					}
    				}else{
    					ToastUtils.showToast(mActivity, "注册码获取失败");
    				}
    			}
    		});
		} catch (Exception e) {

		}
    }
    
    private void registerEmail()
    {
        if (!checkAccount())
        {
            return;
        }
        
        if (!mActivity.checkPassword(mPasswordEdt, mPasswordAgainEdt))
        {
            return;
        }
        
        final String username = mUsernameEdt.getText().toString();
        final String email = mEmailEdt.getText().toString();
        final String password = mPasswordEdt.getText().toString();
        final String emailCode = mEmailCodeEdt.getText().toString().trim();
        
    	if(TextUtils.isEmpty(emailCode)){
    		ToastUtils.showToast(mActivity, "验证码不能为空");
    		return;
    	}
    	
        try {
            JSONObject register = new JSONObject();
            register.put("userName", username);
            register.put("passwd", password);
            register.put("userEmail", email);
            register.put("RegisterCode", emailCode);
            
            HttpManager.getInstances().requestRegister(mActivity, register.toString(), new HttpManagerInterface() {
    			
    			@Override
    			public void onRequestResult(int flag, String msg) {
    				if(flag == HttpManagerInterface.REQUEST_OK){
    					try {
							JSONObject content = new JSONObject(msg);
							JSONObject result = new JSONObject(content.optString("result"));
							String code = result.optString("code");
							String message = result.optString("msg");
							if("OK".equals(code)){
		    					ToastUtils.showToast(mActivity, "注册成功");
		    					getActivity().finish();
							}else{
								ToastUtils.showToast(mActivity, "注册失败:" + message);
							}
    					}catch(Exception e){
    						
    					}
    				}else{
    					ToastUtils.showToast(mActivity, "注册失败:" + msg);
    				}
    			}
    		});
		} catch (Exception e) {

		}
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        if (v == mUsernameEdt)
        {
            if (!hasFocus)
            {
                //findAccount(mUsernameEdt, FIND_USERNAME_EXIST);
            }
        }
        else if (v == mEmailEdt)
        {
            if (!hasFocus)
            {
                //findAccount(mEmailEdt, FIND_EMAIL_EXIST);
            }
        }
    }
    
    private class FindAccountResultUIRunnable implements Runnable
    {
        private EditText mEditText;
        private boolean mResult;
        
        public FindAccountResultUIRunnable(EditText editText, boolean result)
        {
            mEditText = editText;
            mResult = result;
        }
        
        @Override
        public void run()
        {
            Drawable[] drawables = mEditText.getCompoundDrawables();
            if (mResult)
            {
                drawables[2] = mEditText.getContext().getResources().getDrawable(R.drawable.esp_register_icon_forbid);
            }
            else
            {
                drawables[2] = null;
            }
            
            mEditText.setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawables[2], drawables[3]);
            mRegisterBtn.setEnabled(!mResult);
            mEditText.setTag(null);
        }
        
    }
    
    private class FilterSpaceTextListener implements TextWatcher
    {
        private EditText mEditText;
        
        public FilterSpaceTextListener(EditText view)
        {
            mEditText = view;
        }
        
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
        }

        @Override
        public void afterTextChanged(Editable s)
        {
            String space = " ";
            String str = s.toString();
            if (str.contains(space))
            {
                String newStr = str.replace(space, "");
                mEditText.setText(newStr);
                mEditText.setSelection(newStr.length());
            }
        }
        
    }
    
    /**
     * Check user name and email
     * 
     * @return
     */
    private boolean checkAccount()
    {
        CharSequence username = mUsernameEdt.getText();
        CharSequence email = mEmailEdt.getText();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email))
        {
            Toast.makeText(getActivity(), R.string.esp_register_account_email_toast, Toast.LENGTH_LONG).show();
            return false;
        }
        else if (!AccountUtil.isEmail(email.toString()))
        {
            Toast.makeText(getActivity(), R.string.esp_register_account_email_format_error, Toast.LENGTH_LONG).show();
            return false;
        }
        else
        {
            return true;
        }
    }
    
    private void registerSuccess(String email, String password)
    {
        Intent data = new Intent();
        data.putExtra(EspStrings.Key.REGISTER_NAME_EMAIL, email);
        data.putExtra(EspStrings.Key.REGISTER_NAME_PASSWORD, password);
        mActivity.registerSuccess(R.string.esp_register_result_success, data);
    }
}
