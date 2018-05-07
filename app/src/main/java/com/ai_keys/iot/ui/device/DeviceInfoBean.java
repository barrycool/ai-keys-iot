package com.ai_keys.iot.ui.device;

import java.io.Serializable;

public class DeviceInfoBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String device_name;
	private String device_status;
	private String device_connectivity_status;
	private String device_id;
	private String device_ip;
	
	public String getDevice_name() {
		return device_name;
	}
	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}
	public String getDevice_status() {
		return device_status;
	}
	public void setDevice_status(String device_status) {
		this.device_status = device_status;
	}
	public String getDevice_connectivity_status() {
		return device_connectivity_status;
	}
	public void setDevice_connectivity_status(String connectivity_status) {
		this.device_connectivity_status = connectivity_status;
	}
	public String getDevice_id() {
		return device_id;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	public String getDevice_ip() {
		return device_ip;
	}
	public void setDevice_ip(String device_ip) {
		this.device_ip = device_ip;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
