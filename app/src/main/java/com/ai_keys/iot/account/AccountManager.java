package com.ai_keys.iot.account;

import android.content.Context;
import android.text.TextUtils;
import com.ai_keys.iot.tools.XLogger;
import com.ai_keys.iot.util.SharedPrefUtils;
import org.json.JSONObject;

public class AccountManager {
	
	private static volatile AccountManager instance;
	
	private UserInfo mUserInfo;
	
	private AccountManager(){

	}
	
	public static AccountManager getInstance(){
		if(instance == null){
			synchronized (AccountManager.class) {
				if(instance == null){
					instance = new AccountManager();
				}
			}
		}
		return instance;
	}
	
	public void saveUserInfo(Context context, String userInfo){
		if(TextUtils.isEmpty(userInfo)){
			return;
		}
		mUserInfo = strtoUserInfo(userInfo);
		SharedPrefUtils.saveUserInfo(context, mUserInfo.toString());
	}
	
	public UserInfo getUserInfo(Context context){
		if(mUserInfo == null){
			String userInfo = SharedPrefUtils.getUserInfo(context);
			XLogger.d("getSPuserInfo:" + userInfo);
			mUserInfo = strtoUserInfo(userInfo);
		}
		return mUserInfo;
	}
	
	public boolean isLogin(Context context){
		if(getUserInfo(context) != null){
			return true;
		}else{
			return false;
		}
	}
	
	private UserInfo strtoUserInfo(String user){
		UserInfo userInfo = new UserInfo();
		try {
			JSONObject userObj = new JSONObject(user);
			userInfo.setUserId(userObj.optString("userId"));
			userInfo.setUserName(userObj.optString("userName"));
			userInfo.setUserPhone(userObj.optString("userPhone"));
			userInfo.setUserEmail(userObj.optString("userEmail"));
		} catch (Exception e) {

		}
		return userInfo;
	}
}
