package com.ai_keys.iot.tools;

import android.util.Log;

import com.ai_keys.iot.AiKeysApplication;

public class XLogger {
	
	private static final String TAG = "AiKey";
	
	public static final boolean DEBUG = true;
	
	public static void i(String msg) {
		if (DEBUG) {
			Log.i(TAG, AiKeysApplication.version + " " + msg);
		}
	}
	
	public static void d(String msg) {
		if (DEBUG) {
			Log.i(TAG, AiKeysApplication.version + " " + msg);
		}
	}

	public static void e(String msg) {
		if (DEBUG) {
			Log.e(TAG, AiKeysApplication.version +  " " + msg);
		}
	}
	
	public static void exception(Exception e) {
		if (DEBUG) {
			e.printStackTrace();
		}
	}
}
