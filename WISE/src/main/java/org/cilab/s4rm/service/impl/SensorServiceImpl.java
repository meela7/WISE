package org.cilab.s4rm.service.impl;

import java.util.List;
import java.util.Map;

import org.cilab.s4rm.dao.SensorDAO;
import org.cilab.s4rm.model.Sensor;
import org.cilab.s4rm.service.SensorService;

public class SensorServiceImpl implements SensorService {

	/**
	 * Class Name:	SensorServiceImpl.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.06.13
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	SensorDAO sensorDao;
	
	public void setSensorDao(SensorDAO sensorDao){
		this.sensorDao = sensorDao;
	}
	@Override
	public boolean newInstance(Sensor sensor) {
		return this.sensorDao.create(sensor);
	}

	@Override
	public Sensor readInstance(String sensorID) {
		return this.sensorDao.read(sensorID);
	}

	@Override
	public boolean updateInstance(Sensor sensor) {
		return this.sensorDao.update(sensor);
	}

	@Override
	public boolean deleteInstance(String sensorID) {
		return this.sensorDao.delete(sensorID);
	}

	@Override
	public List<Sensor> readCollection() {
		return this.sensorDao.list();
	}

	@Override
	public boolean isInstanceExist(String streamID, String createdAt) {
		if(this.sensorDao.getByUniqueKey(streamID, createdAt) != null)
			return true;
		return false;
	}

	@Override
	public List<Sensor> search(Map<String, String> map) {
		return this.sensorDao.search(map);
	}

	@Override
	public List<Sensor> listSearch(Map<String, List<String>> map) {
		return this.sensorDao.listSearch(map);
	}

}
