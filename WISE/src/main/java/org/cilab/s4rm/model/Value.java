package org.cilab.s4rm.model;

public class Value {

	/**
	 * Class Name: Value.java 
	 * Description: 
	 * 
	 * @author Meilan Jiang
	 * @since 2016.06.13
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	
	private int ValueID;
	private String StreamID;
	private String DateTime;
	private double Value;	
	
	public int getValueID() {
		return ValueID;
	}
	public void setValueID(int valueID) {
		ValueID = valueID;
	}
	public String getStreamID() {
		return StreamID;
	}
	public void setStreamID(String streamID) {
		StreamID = streamID;
	}
	public String getDateTime() {
		return DateTime;
	}
	public void setDateTime(String dateTime) {
		DateTime = dateTime;
	}
	public double getValue() {
		return Value;
	}
	public void setValue(double value) {
		Value = value;
	}
	
	
}
