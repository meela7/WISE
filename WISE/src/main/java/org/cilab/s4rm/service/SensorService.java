package org.cilab.s4rm.service;

import java.util.List;
import java.util.Map;

import org.cilab.s4rm.model.Sensor;

public interface SensorService {
	
	/**
	 * Class Name:	SensorService.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.14
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	public boolean newInstance(Sensor sensor);
	public Sensor readInstance(String sensorID);
	public boolean updateInstance(Sensor sensor);
	public boolean deleteInstance(String sensorID);	
	public List<Sensor> readCollection();
	
	public boolean isInstanceExist(String streamID, String createdAt);
	public List<Sensor> search(Map<String, String> map);
	public List<Sensor> listSearch(Map<String, List<String>> map);

}
