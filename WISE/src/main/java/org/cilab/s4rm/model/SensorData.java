package org.cilab.s4rm.model;

public class SensorData {
	/**
	 * Class Name: SensorData.java 
	 * Description: CSN Mapping pojo File
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.25
	 * @version 1.2
	 * 
	 * @modified 2016.05.13 - hibernate joined sub-class inheritance strategy
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	private String id;
	private String timestamp;
	private double value;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	
}
