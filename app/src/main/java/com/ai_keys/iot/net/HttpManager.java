package com.ai_keys.iot.net;

import android.content.Context;
import android.text.TextUtils;

import com.ai_keys.iot.tools.Constant;
import com.ai_keys.iot.tools.XLogger;

import org.json.JSONObject;

public class HttpManager {
	
	private volatile static HttpManager instances;
	
	private AsyncRequest mAsyncRequest;
	
	private HttpManager() {
		mAsyncRequest = new AsyncRequest();
	}

	public static HttpManager getInstances() {
		if(instances == null){
			synchronized (HttpManager.class) {
				if(instances == null){
					instances = new HttpManager();
				}
			}
		}
		return instances;
	}
	
	public void requestSmsCode(final Context context, String message, final HttpManagerInterface listener){
		try {
            
			mAsyncRequest.requestMessage(Constant.REGISTER, "", new AsyncRequest.RequestListener() {
				
				@Override
				public void onComplete(String response) {
					if(TextUtils.isEmpty(response)){
						listener.onRequestResult(HttpManagerInterface.REQUEST_ERROR, "");
						return;
					}
					listener.onRequestResult(HttpManagerInterface.REQUEST_OK, response);
				}
			});
		} catch (Exception e) {
			listener.onRequestResult(HttpManagerInterface.REQUEST_ERROR, "");
		}
	}
	
	/*
	 * 新用户注册
	 */
	public void requestRegister(final Context context, String message, final HttpManagerInterface listener){
		
		try {
			JSONObject msg = new JSONObject(message);
            JSONObject register = new JSONObject();
            register.put("name_space", "AccountManagement");
            register.put("name", "AddUser");
            register.put("userName", msg.optString("userName"));
            register.put("passwd", msg.optString("passwd"));
            register.put("userPhone", msg.optString("userPhone"));
            register.put("userEmail", msg.optString("userEmail"));
            register.put("RegisterCode", msg.optString("RegisterCode"));
            
            XLogger.d("Register:" + register.toString());
            
			mAsyncRequest.requestMessage(Constant.REGISTER, register.toString(), new AsyncRequest.RequestListener() {
				
				@Override
				public void onComplete(String response) {
					XLogger.d(response);
					if(TextUtils.isEmpty(response)){
						listener.onRequestResult(HttpManagerInterface.REQUEST_ERROR, "");
						return;
					}
					listener.onRequestResult(HttpManagerInterface.REQUEST_OK, response);
				}
			});
		} catch (Exception e) {
			listener.onRequestResult(HttpManagerInterface.REQUEST_ERROR, "");
		}
	}
	
	/*
	 * 获取邮箱验证码
	 */
	public void requestEmailCode(final Context context, String email, final HttpManagerInterface listener){
		
		try {
            JSONObject requestCode = new JSONObject();
            requestCode.put("name_space", "AccountManagement");
            requestCode.put("name", "RequestRegisterCode");
            requestCode.put("userAccount", email);
            
            XLogger.d("RequestCode:" + requestCode.toString());
            
			mAsyncRequest.requestMessage(Constant.REGISTER, requestCode.toString(), new AsyncRequest.RequestListener() {
				
				@Override
				public void onComplete(String response) {
					XLogger.d(response);
					if(TextUtils.isEmpty(response)){
						listener.onRequestResult(HttpManagerInterface.REQUEST_ERROR, "");
						return;
					}
					listener.onRequestResult(HttpManagerInterface.REQUEST_OK, response);
				}
			});
		} catch (Exception e) {
			listener.onRequestResult(HttpManagerInterface.REQUEST_ERROR, "");
		}
	}
	
	/*
	 * 用户登录
	 */
	public void requestLogin(final Context context, String message, final HttpManagerInterface listener){
		
		try {
			JSONObject msg = new JSONObject(message);
            JSONObject loginObj = new JSONObject();
            loginObj.put("name_space", "AccountManagement");
            loginObj.put("name", "Login");
            loginObj.put("loginName", msg.optString("loginName"));
            loginObj.put("passwd", msg.optString("passwd"));
            
            XLogger.d("Login:" + loginObj.toString());
            
			mAsyncRequest.requestMessage(Constant.REGISTER, loginObj.toString(), new AsyncRequest.RequestListener() {
				
				@Override
				public void onComplete(String response) {
					XLogger.d(response);
					if(TextUtils.isEmpty(response)){
						listener.onRequestResult(HttpManagerInterface.REQUEST_ERROR, "");
						return;
					}
					listener.onRequestResult(HttpManagerInterface.REQUEST_OK, response);
				}
			});
		} catch (Exception e) {
			listener.onRequestResult(HttpManagerInterface.REQUEST_ERROR, "");
		}
	}
	
	/*
	 * 设备注册
	 */
	public void requestDeviceRegister(final Context context, String message, final HttpManagerInterface listener){
		
		try {
			JSONObject msg = new JSONObject(message);
            JSONObject register = new JSONObject();
            register.put("name_space", "DeviceManagement");
            register.put("name", "AddDevice");
            register.put("deviceId", msg.optString("deviceId"));
            register.put("deviceType", msg.optString("deviceType"));
            register.put("friendlyName", msg.optString("friendlyName"));
            register.put("manufacturerName", msg.optString("manufacturerName"));
            register.put("userId", msg.optString("userId"));
            
            XLogger.d("DeviceRegister:" + register.toString());
            
			mAsyncRequest.requestMessage(Constant.REGISTER, register.toString(), new AsyncRequest.RequestListener() {
				
				@Override
				public void onComplete(String response) {
					XLogger.d(response);
					if(TextUtils.isEmpty(response)){
						listener.onRequestResult(HttpManagerInterface.REQUEST_ERROR, "");
						return;
					}
					listener.onRequestResult(HttpManagerInterface.REQUEST_OK, response);
				}
			});
		} catch (Exception e) {
			listener.onRequestResult(HttpManagerInterface.REQUEST_ERROR, "");
		}
	}
	
	/*
	 * 查询用户绑定的设备列表
	 */
	public void requestDeviceList(final Context context, String message, final HttpManagerInterface listener){
		
		try {
			JSONObject msg = new JSONObject(message);
            JSONObject devicelist = new JSONObject();
            devicelist.put("name_space", "Alexa.Discovery");
            devicelist.put("name", "Discovery");
            devicelist.put("token", msg.optString("token"));
            devicelist.put("userId", msg.optString("userId"));
            
            XLogger.d("deviceList:" + devicelist.toString());
            
			mAsyncRequest.requestMessage(Constant.DEVICELIST, devicelist.toString(), new AsyncRequest.RequestListener() {
				
				@Override
				public void onComplete(String response) {
					XLogger.d(response);
					if(TextUtils.isEmpty(response)){
						listener.onRequestResult(HttpManagerInterface.REQUEST_ERROR, "");
						return;
					}
					listener.onRequestResult(HttpManagerInterface.REQUEST_OK, response);
				}
			});
		} catch (Exception e) {
			listener.onRequestResult(HttpManagerInterface.REQUEST_ERROR, "");
		}
	}
	
	/*
	 * 设备开关
	 */
	public void requestDeviceSwitch(final Context context, JSONObject device, final HttpManagerInterface listener){
		
		try {
		    device.put("name_space", "Alexa.PowerController");
            device.put("token", "token");
            
            XLogger.d("deviceSwitch:" + device.toString());
            
			mAsyncRequest.requestMessage(Constant.SWITCHER, device.toString(), new AsyncRequest.RequestListener() {
				
				@Override
				public void onComplete(String response) {
					XLogger.d(response);
					if(TextUtils.isEmpty(response)){
						listener.onRequestResult(HttpManagerInterface.REQUEST_ERROR, "");
						return;
					}
					listener.onRequestResult(HttpManagerInterface.REQUEST_OK, response);
				}
			});
		} catch (Exception e) {
			listener.onRequestResult(HttpManagerInterface.REQUEST_ERROR, "");
		}
	}

	public void requestDeviceCtrl(final Context context, String info, final HttpManagerInterface listener){
		try {
            mAsyncRequest.requestMessage(Constant.SWITCHER, info, new AsyncRequest.RequestListener() {

	        @Override
	        public void onComplete(String response) {
		            XLogger.d(response);
		            if(TextUtils.isEmpty(response)){
		                listener.onRequestResult(HttpManagerInterface.REQUEST_ERROR, "");
		                return;
		            }
		            listener.onRequestResult(HttpManagerInterface.REQUEST_OK, response);
				}
			});
		} catch (Exception e) {
			listener.onRequestResult(HttpManagerInterface.REQUEST_ERROR, "");
		}
	}
	
	/*
	 * 设备状态查询
	 */
	public void requestDeviceStatus(final Context context, String message, final HttpManagerInterface listener){
		
		try {
			JSONObject msg = new JSONObject(message);
            JSONObject status = new JSONObject();
            status.put("name_space", "Alexa");
            status.put("name", "ReportState");
            status.put("deviceId", msg.optString("deviceId"));
            status.put("token", msg.optString("token"));
            
            XLogger.d("deviceStatus:" + status.toString());
            
			mAsyncRequest.requestMessage(Constant.SWITCHER, status.toString(), new AsyncRequest.RequestListener() {
				
				@Override
				public void onComplete(String response) {
					XLogger.d(response);
					if(TextUtils.isEmpty(response)){
						listener.onRequestResult(HttpManagerInterface.REQUEST_ERROR, "");
						return;
					}
					listener.onRequestResult(HttpManagerInterface.REQUEST_OK, response);
				}
			});
		} catch (Exception e) {
			listener.onRequestResult(HttpManagerInterface.REQUEST_ERROR, "");
		}
	}
}
