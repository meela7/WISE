package org.cilab.s4rm.model;

import java.util.List;

public class Member {
	
	/**
	 * Class Name: SensorNetwok.java 
	 * Description: 
	 * 
	 * @author Meilan Jiang
	 * @since 2016.06.10
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	
	private String ID;
//	private String Name;	// No Mapping field in the DB
	private List<Sensor> Members;
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public List<Sensor> getCsnList() {
		return Members;
	}
	public void setSensors(List<Sensor> members) {
		Members = members;
	}
}
