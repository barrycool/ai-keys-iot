package com.ai_keys.iot.account;

import org.json.JSONObject;

public class UserInfo {
	
	private String userId = "";
	private String userName = "";
	private String userPhone = "";
	private String userEmail = "";
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
	public String toString(){
		String userinfo = "";
		try {
			JSONObject user = new JSONObject();
			user.put("userId", userId);
			user.put("userName", userName);
			user.put("userPhone", userPhone);
			user.put("userEmail", userEmail);
			userinfo = user.toString();
		} catch (Exception e) {

		}
		return userinfo;
	}
}
