package com.ai_keys.iot.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.ai_keys.iot.R;
import com.ai_keys.iot.ui.main.EspActivityAbs;
import com.ai_keys.iot.util.AccountUtil;

public class ResetUserPasswordActivity extends EspActivityAbs implements OnClickListener
{
    
    private EditText mEmailET;
    private Button mConfirmBtn;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.reset_password_activity);
        setTitle(getString(R.string.esp_reset_password));
        
        mEmailET = (EditText)findViewById(R.id.resetpwd_email);
        mEmailET.addTextChangedListener(mEmailWatcher);
        
        mConfirmBtn = (Button)findViewById(R.id.resetpwd_confirm);
        mConfirmBtn.setOnClickListener(this);
        mConfirmBtn.setEnabled(false);
    }

    @Override
    public void onClick(View v)
    {
        if (v == mConfirmBtn)
        {
            // Hide the soft keyboard
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mEmailET.getWindowToken(), 0);
        }
    }
    
    private TextWatcher mEmailWatcher = new TextWatcher()
    {
        
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
        }
        
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
        }
        
        @Override
        public void afterTextChanged(Editable s)
        {
            // Check Email address format is legal
            if (AccountUtil.isEmail(s.toString()))
            {
                mConfirmBtn.setEnabled(true);
            }
            else
            {
                mConfirmBtn.setEnabled(false);
            }
        }
    };
}
