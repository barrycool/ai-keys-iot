package com.ai_keys.iot.ui.usercenter;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.ai_keys.iot.R;

public class ResetPwdActivity extends Activity {

    private TextView mTitleTxt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpwd);

        mTitleTxt = (TextView) findViewById(R.id.title_txt);
        mTitleTxt.setText(R.string.change_password_out);
    }
}
