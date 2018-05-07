package com.ai_keys.iot.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.ai_keys.iot.AiKeysApplication;
import com.ai_keys.iot.R;
import com.ai_keys.iot.ui.login.LoginActivity;
import com.ai_keys.iot.ui.widget.adapter.EspPagerAdapter;
import com.ai_keys.iot.util.SharedPrefUtils;
import com.ai_keys.iot.util.ToastUtils;
import com.espressif.iot.ui.widget.view.EspViewPager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends Activity
{
    private ImageView mMainIV;
    
    private EspViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private List<View> mPagerViewList;
    
    private final static String NAME_USE_INFO = "use_info";
    private final static String KEY_LAST_USE_VERSION_CODE = "key_last_use_version_code";
    
    private final static int MSG_SHOW_PAGER = 0x10;
    private final static int MSG_LOGIN = 0x11;
    
    private Handler mHandler;
    
    private SharedPreferences mShared;
    
    private AiKeysApplication mApplication;
    
	private Timer timer = new Timer();
	private int welcomeTime = 3; //欢迎页面展示时间
	private Runnable runnable;
	
	private static final long twoDay = 2 * 24 * 60 * 60 * 1000;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.welcome_activity);
        
        mApplication = AiKeysApplication.sharedInstance();
        
        mMainIV = (ImageView)findViewById(R.id.welcome_main_img);
        mPager = (EspViewPager)findViewById(R.id.welcome_pager);
        mPager.setInterceptTouchEvent(false);
        mPagerViewList = new ArrayList<View>();
        initPagerItem();
        mPagerAdapter = new EspPagerAdapter(mPagerViewList);
        mPager.setAdapter(mPagerAdapter);
        
        mHandler = new WelcomeHandler(this);
        mShared = getSharedPreferences(NAME_USE_INFO, Context.MODE_PRIVATE);
        int lastUseVersion = mShared.getInt(KEY_LAST_USE_VERSION_CODE, 0);
        int currentVersion = mApplication.getVersionCode();
        
        if (currentVersion > lastUseVersion){
            mHandler.sendEmptyMessageDelayed(MSG_SHOW_PAGER, 1000);
        }else{
    		timer.schedule(task, 1000, 1000);// 等待时间一秒，停顿时间一秒

    		mHandler.postDelayed(runnable = new Runnable() {
    			@Override
    			public void run() {
    				login();
    			}
    		}, 3000);
        }
    }

	private TimerTask task = new TimerTask() {
		@Override
		public void run() {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					welcomeTime--;
					if (welcomeTime < 0) {
						timer.cancel();
						timer = null;
					}
				}
			});
		}
	};
	
    @Override
    protected void onDestroy() {
        super.onDestroy();

        mHandler.removeMessages(MSG_LOGIN);
        mHandler.removeMessages(MSG_SHOW_PAGER);
    }

    private void initPagerItem()
    {
        for (int i = 0; i < 3; i++)
        {
            ImageView pagerItem = (ImageView)View.inflate(this, R.layout.welcome_pager_item, null);
            switch (i)
            {
                case 0:
                    pagerItem.setImageResource(R.drawable.esp_welcome_pager_src_0);
                    break;
                case 1:
                    pagerItem.setImageResource(R.drawable.esp_welcome_pager_src_1);
                    break;
                case 2:
                    pagerItem.setImageResource(R.drawable.esp_welcome_pager_src_2);
                    pagerItem.setOnClickListener(mToLoginListener);
                    break;
            }
            
            mPagerViewList.add(pagerItem);
        }
    }
    
    private View.OnClickListener mToLoginListener = new View.OnClickListener()
    {
        
        @Override
        public void onClick(View v)
        {
            login();
            
            int versionCode = mApplication.getVersionCode();
            mShared.edit().putInt(KEY_LAST_USE_VERSION_CODE, versionCode).apply();
        }
    };
    
    private void showPager()
    {
        Animation mainOutAnim = AnimationUtils.loadAnimation(this, R.anim.esp_welcome_translate_out);
        mainOutAnim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mMainIV.clearAnimation();
                mMainIV.setVisibility(View.GONE);
                mPager.setInterceptTouchEvent(true);
            }
        });
        
        Animation pagerInAnim = AnimationUtils.loadAnimation(this, R.anim.esp_welcome_translate_in);
        pagerInAnim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mPager.clearAnimation();
            }
        });

        mMainIV.startAnimation(mainOutAnim);
        mPager.setVisibility(View.VISIBLE);
        mPager.startAnimation(pagerInAnim);
    }
    
    private void login(){
    	if(System.currentTimeMillis() - SharedPrefUtils.getLoginTime(this) > twoDay){
    		ToastUtils.showToast(this, "登录账号已经过期,请重新登录");
            startActivity(new Intent(this, LoginActivity.class));
//        	startActivity(new Intent(this, EspMainActivity.class));
    	}else{
        	startActivity(new Intent(this, EspMainActivity.class));
    	}
        finish();
        
        try {
            if (runnable != null) {
    			mHandler.removeCallbacks(runnable);
    		}
		} catch (Exception e) {

		}
    }
    
    private static class WelcomeHandler extends Handler
    {
        private WeakReference<WelcomeActivity> mActivity;
        
        public WelcomeHandler(WelcomeActivity activity)
        {
            mActivity = new WeakReference<WelcomeActivity>(activity);
        }
        
        @Override
        public void handleMessage(Message msg)
        {
            WelcomeActivity activity = mActivity.get();
            if (activity == null)
            {
                return;
            }
            
            switch (msg.what)
            {
                case MSG_SHOW_PAGER:
                    activity.showPager();
                    break;
                case MSG_LOGIN:
                    activity.login();
                    break;
            }
        }
    }
}
