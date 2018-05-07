package com.ai_keys.iot.util;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

	public static void showToast(final Context myContext, final String message) {
		try {
			((Activity) myContext).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(myContext, message, Toast.LENGTH_SHORT).show();
				}
			});
		} catch (Exception e) {

		}
	}

	public static void showToast2(final Context myContext, final String message) {
		try {
			((Activity) myContext).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(myContext, message, Toast.LENGTH_LONG).show();
				}
			});
		} catch (Exception e) {

		}
	}

}
