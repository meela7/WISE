package org.cilab.s4rm.dao;

import java.util.List;
import java.util.Map;

import org.cilab.s4rm.model.Sensor;


public interface SensorDAO {
	
	/**
	 * Class Name:	SiteDAO.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.13
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	public boolean create(Sensor sensor);
	public Sensor read(String sensorID);
	public boolean update(Sensor sensor);
	public boolean delete(String sensorID);
	
	public List<Sensor> list();
	public Sensor getByUniqueKey(String streamID, String createdAt);
	public List<Sensor> search(Map<String, String> map);
	public List<Sensor> listSearch(Map<String, List<String>> map);

}
