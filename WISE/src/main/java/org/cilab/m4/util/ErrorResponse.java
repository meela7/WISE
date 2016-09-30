package org.cilab.m4.util;

public class ErrorResponse {

	private int code;
	private String infoMsg;
	private String devMsg;
	private String url;
	
	public ErrorResponse(int code, String infoMsg, String devMsg, String url) {
		this.code = code;
		this.infoMsg = infoMsg;
		this.devMsg = devMsg;
		this.url = url;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getInfoMsg() {
		return infoMsg;
	}
	public void setInfoMsg(String infoMsg) {
		this.infoMsg = infoMsg;
	}
	public String getDevMsg() {
		return devMsg;
	}
	public void setDevMsg(String devMsg) {
		this.devMsg = devMsg;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
