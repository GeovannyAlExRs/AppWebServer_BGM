package com.ec.busgeomap.web.app.model;

public class ReCaptchaResponse {
	private boolean success;
	private String challege_ts;
	private String hostName;
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getChallege_ts() {
		return challege_ts;
	}
	public void setChallege_ts(String challege_ts) {
		this.challege_ts = challege_ts;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	
	

}
