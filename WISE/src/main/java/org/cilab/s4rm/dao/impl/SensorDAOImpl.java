package org.cilab.s4rm.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.cilab.s4rm.dao.SensorDAO;
import org.cilab.s4rm.model.Sensor;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public class SensorDAOImpl implements SensorDAO {

	/**
	 * Class Name:	SensorDAOImpl.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.14
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	private static final Logger logger = LoggerFactory.getLogger(SensorDAOImpl.class);

	private SessionFactory sessionFactory;
	
	public SensorDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	@Transactional
	public boolean create(Sensor sensor) {
		
		try {
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createSQLQuery("INSERT INTO Sensor (SensorID, Name, CreatedAt, CreatedBy, Status, StreamID, Members, Description) VALUES (:sensorID, :name, :createdAt, :createdBy, :status, :streamID, :members, :description) ");
			query.setParameter("sensorID", sensor.getId());
			query.setParameter("name", sensor.getName());
			query.setParameter("createdAt", sensor.getCreatedAt());
			query.setParameter("createdBy", sensor.getCreatedBy());
			query.setParameter("status", sensor.getStatus());
			query.setParameter("streamID", sensor.getStreamID());
			query.setParameter("members", sensor.getMembers());
			query.setParameter("description", sensor.getDesc());
			query.executeUpdate();
			
			for (int i=0; i<sensor.getMetas().size();i++){
				query = session.createSQLQuery("INSERT INTO Sensor_Meta(SensorID, `Key`, `Value`) VALUES (:sensorID, :key, :value)");
				query.setParameter("sensorID", sensor.getId());
				query.setParameter("key", sensor.getMetas().get(i).getKey());
				query.setParameter("value", sensor.getMetas().get(i).getValue());
				query.executeUpdate();
			}
			
			for (int i=0; i<sensor.getTags().size();i++){
				query = session.createSQLQuery("INSERT INTO Sensor_Tag(SensorID, Name) VALUES (:sensorID, :tag)");
				query.setParameter("sensorID", sensor.getId());
				query.setParameter("tag", sensor.getTags().get(i).getName());
				query.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
		
//		try {
//			List<Sensor_Meta> metas = new ArrayList<Sensor_Meta>();
//			for(Sensor_Meta meta : sensor.getMetas()){
//				meta.setSensorID(sensor.getId());
//				metas.add(meta);
//			}
//			sensor.setMetas(metas);
//			sessionFactory.getCurrentSession().save(sensor);			
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//		return true;
	}

	@Override
	@Transactional
	public Sensor read(String sensorID) {
		return (Sensor)this.sessionFactory.getCurrentSession().get(Sensor.class, sensorID);
	}

	@Override
	@Transactional
	public boolean update(Sensor sensor) {
		try {
			sessionFactory.getCurrentSession().update(sensor);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public boolean delete(String sensorID) {
		try {
			Query query = sessionFactory.getCurrentSession().createQuery("DELETE FROM Sensor WHERE SensorID = :sensorID");
			query.setParameter("sensorID", sensorID);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public List<Sensor> list() {
		@SuppressWarnings("unchecked")
		List<Sensor> siteList = (List<Sensor>) sessionFactory.getCurrentSession().createCriteria(Sensor.class).list();
		
		return siteList;
	}

	@Override
	@Transactional
	public Sensor getByUniqueKey(String streamID, String createdAt) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM Sensor WHERE StreamID = :streamID and CreatedAt = :createdAt ");
		
		query.setParameter("streamID", streamID);
		query.setParameter("createdAt", createdAt);
		
		Sensor sensor = (Sensor) query.uniqueResult();
		return sensor;
	}

	@Override
	@Transactional
	public List<Sensor> search(Map<String, String> map) {
		Session session = sessionFactory.getCurrentSession();
		String hqlQuery = "FROM Sensor WHERE ";

		int index = 0;
		for(String key : map.keySet()){
			if(index == 0 )
				hqlQuery = hqlQuery + key + " = :" + key ;
			else
				hqlQuery = hqlQuery + " and " + key + " = :" + key ;
			index++;
		}
		
		// execute HQL Query
		logger.info("Execute Query: {}", hqlQuery);
		Query query = session.createQuery(hqlQuery);
		for(String key : map.keySet()){
			query.setParameter(key, map.get(key));	// convert type if the values are not String type
		}
		@SuppressWarnings("unchecked")
		List<Sensor> sensorList = (List<Sensor>) query.list();
		return sensorList;
	}

	@Override
	@Transactional
	public List<Sensor> listSearch(Map<String, List<String>> map) {
		Session session = sessionFactory.getCurrentSession();
		String hqlQuery = "FROM Sensor WHERE ";
			
		// create HQL Statement
		int index = 0;
		for(String key : map.keySet()){			
			if(index == 0 )
				hqlQuery = hqlQuery + key + " in :" + key ;
			else
				hqlQuery = hqlQuery + " and " + key + " in :" + key ;
			index++;
		}
		
		// execute HQL Query
		logger.info("Execute Query: {}", hqlQuery);
		Query query = session.createQuery(hqlQuery);
		for(String key : map.keySet()){
			if(key.equals("SensorID")){
				List<Integer> valueList = new ArrayList<Integer>();
				for(String value: map.get(key)){
					valueList.add(Integer.parseInt(value));
				}
				query.setParameterList(key, valueList);
			}else
				query.setParameterList(key, map.get(key));
		}
		@SuppressWarnings("unchecked")
		List<Sensor> sensorList = (List<Sensor>) query.list();
		
		return sensorList;
	}

}
