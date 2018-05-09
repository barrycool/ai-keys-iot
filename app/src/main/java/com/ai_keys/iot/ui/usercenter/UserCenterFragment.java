package com.ai_keys.iot.ui.usercenter;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ai_keys.iot.AiKeysApplication;
import com.ai_keys.iot.R;
import com.ai_keys.iot.account.AccountManager;
import com.ai_keys.iot.tools.XLogger;
import com.ai_keys.iot.ui.main.EspMainActivity;

public class UserCenterFragment extends Fragment implements View.OnClickListener{
	
    private EspMainActivity mActivity;
    
    private TextView mUserNameTxt;
    private TextView mUserPhoneTxt;

    private RelativeLayout mFaq_layout;
    private RelativeLayout mFeedback_layout;
    private RelativeLayout mNick_layout;
    private RelativeLayout mPwd_layout;
    private RelativeLayout mExit_layout;
    private RelativeLayout mAbout_aikey_layout;
    private RelativeLayout mSetting_layout;

    private TextView mAppVersion;
    
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        
        mActivity = (EspMainActivity)activity;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
        View view = inflater.inflate(R.layout.user_center, container, false);

        mUserNameTxt = (TextView) view.findViewById(R.id.user_fragment_nickname);
        mUserPhoneTxt = (TextView) view.findViewById(R.id.user_fragment_phonenumber);

        mFaq_layout = (RelativeLayout) view.findViewById(R.id.faq_layout);
        mFaq_layout.setOnClickListener(this);

        mFeedback_layout = (RelativeLayout) view.findViewById(R.id.feedback_layout);
        mFeedback_layout.setOnClickListener(this);

        mNick_layout = (RelativeLayout) view.findViewById(R.id.modify_nick_layout);
        mNick_layout.setOnClickListener(this);

        mPwd_layout = (RelativeLayout) view.findViewById(R.id.set_pwd_layout);
        mPwd_layout.setOnClickListener(this);

        mAppVersion = (TextView) view.findViewById(R.id.new_tv_version);

        mExit_layout = (RelativeLayout) view.findViewById(R.id.exit_layout);
        mExit_layout.setOnClickListener(this);

        mAbout_aikey_layout = (RelativeLayout) view.findViewById(R.id.about_aikey_layout);
        mAbout_aikey_layout.setOnClickListener(this);

        mSetting_layout = (RelativeLayout) view.findViewById(R.id.setting_layout);
        mSetting_layout.setOnClickListener(this);

        setUserData();
		return view;
	}
	
	private void setUserData(){
		mUserNameTxt.setText(AccountManager.getInstance().getUserInfo(getActivity()).getUserName());
		mUserPhoneTxt.setText(AccountManager.getInstance().getUserInfo(getActivity()).getUserPhone());

        mAppVersion.setText(AiKeysApplication.version);
	}

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.faq_layout:
                startActivity(new Intent(getActivity(), FaqActivity.class));
                break;
            case R.id.feedback_layout:
                startActivity(new Intent(getActivity(), FeedbackActivity.class));
                break;
            case R.id.modify_nick_layout:
                startActivity(new Intent(getActivity(), ModifyNickActivity.class));
                break;
            case R.id.set_pwd_layout:
                startActivity(new Intent(getActivity(), ResetPwdActivity.class));
                break;
            case R.id.setting_layout:
                startActivity(new Intent(getActivity(), UserSettingActivity.class));
                break;
            case R.id.about_aikey_layout:
                startActivity(new Intent(getActivity(), AboutAiKeyActivity.class));
                break;
            case R.id.exit_layout:
                AiKeysApplication.onExitApp(getActivity());
                break;
            default:
                break;
        }
    }
}
