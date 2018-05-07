package com.ai_keys.iot.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.ai_keys.iot.tools.XLogger;

public class SharedPrefUtils {

	private static final String SM_SP = "smary_connet";
	
	private static final String KEY_USERINFO = "smsp001";
	
	private static final String KEY_LOGINTIME = "smsp002";
	
	public static void saveUserInfo(Context context, String value) {
		SharedPrefUtils.saveString(context, KEY_USERINFO, value);
	}

	public static String getUserInfo(Context context) {
		return SharedPrefUtils.getString(context, KEY_USERINFO);
	}
	
	public static void saveLoginTime(Context context, long value) {
		SharedPrefUtils.saveLong(context, KEY_LOGINTIME, value);
	}

	public static long getLoginTime(Context context) {
		return SharedPrefUtils.getLong(context, KEY_LOGINTIME, 0l);
	}
	
	public static String getString(Context mContext, String key) {
		try {
			SharedPreferences sp = mContext.getSharedPreferences(SM_SP, Context.MODE_PRIVATE);
			return sp.getString(key, "");
			
		} catch (Exception e) {
			XLogger.e("get sp " + key + " error!");
			saveString(mContext, key, "");
			return "";
		}
	}

	public static void saveString(Context mContext, String key, String value) {
		try {
			SharedPreferences sp = mContext.getSharedPreferences(SM_SP, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
			editor.putString(key, value);
			editor.commit();
		} catch (Exception e) {
			XLogger.e("save sp " + key + " error!");
		}
	}
	
	public static int getInt(Context mContext, String key, int defaultValue) {
		try {
			SharedPreferences sp = mContext.getSharedPreferences(SM_SP, Context.MODE_PRIVATE);
			return sp.getInt(key, defaultValue);
			
		} catch (Exception e) {
			XLogger.e("get sp " + key + " error!");
			saveInt(mContext, key, defaultValue);
			return defaultValue;
		}
	}

	public static void saveInt(Context mContext, String key, int value) {
		try {
			SharedPreferences sp = mContext.getSharedPreferences(SM_SP, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
			editor.putInt(key, value);
			editor.commit();
		} catch (Exception e) {
			XLogger.e("save sp " + key + " error!");
		}
	}
	
	public static long getLong(Context mContext, String key, long defaultValue) {
		try {
			SharedPreferences sp = mContext.getSharedPreferences(SM_SP, Context.MODE_PRIVATE);
			return sp.getLong(key, defaultValue);
			
		} catch (Exception e) {
			XLogger.e("get sp " + key + " error!");
			saveLong(mContext, key, defaultValue);
			return defaultValue;
		}
	}

	public static void saveLong(Context mContext, String key, long value) {
		try {
			SharedPreferences sp = mContext.getSharedPreferences(SM_SP, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
			editor.putLong(key, value);
			editor.commit();
		} catch (Exception e) {
			XLogger.e("save sp " + key + " error!");
		}
	}
	
	public static boolean getBoolean(Context mContext, String key, boolean defaultValue) {
		try {
			SharedPreferences sp = mContext.getSharedPreferences(SM_SP, Context.MODE_PRIVATE);
			return sp.getBoolean(key, defaultValue);
			
		} catch (Exception e) {
			XLogger.e("get sp " + key + " error!");
			saveBoolean(mContext, key, defaultValue);
			return defaultValue;
		}
	}

	public static void saveBoolean(Context mContext, String key, boolean value) {
		try {
			SharedPreferences sp = mContext.getSharedPreferences(SM_SP, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
			editor.putBoolean(key, value);
			editor.commit();
		} catch (Exception e) {
			XLogger.e("save sp " + key + " error!");
		}
	}
}
