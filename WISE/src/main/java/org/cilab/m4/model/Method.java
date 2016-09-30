package org.cilab.m4.model;

public class Method {
	
	/**
	 * Class Name: Method.java 
	 * Description: Hibernate Mapping pojo File
	 * 
	 * @author Meilan Jiang
	 * @since 2016.02.04
	 * @version 1.2
	 * 
	 * @modified 2016.05.13 - hibernate joined sub-class inheritance strategy
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	private int MethodID;
	private String MethodType;
	private String MethodName;
	
	public int getMethodID() {
		return MethodID;
	}
	public void setMethodID(int methodID) {
		MethodID = methodID;
	}
	public String getMethodType() {
		return MethodType;
	}
	public void setMethodType(String methodType) {
		MethodType = methodType;
	}
	public String getMethodName() {
		return MethodName;
	}
	public void setMethodName(String methodName) {
		MethodName = methodName;
	}
	
}
