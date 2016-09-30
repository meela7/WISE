package org.cilab.s4rm.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.cilab.s4rm.dao.LogDAO;
import org.cilab.s4rm.model.Log;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public class LogDAOImpl implements LogDAO {

	/**
	 * Class Name:	LDAOImpl.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.14
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	private static final Logger logger = LoggerFactory.getLogger(LogDAOImpl.class);

	private SessionFactory sessionFactory;
	
	public LogDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	@Transactional
	public boolean create(Log log) {
		try {
			sessionFactory.getCurrentSession().save(log);			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public Log read(int logID) {
		return (Log)this.sessionFactory.getCurrentSession().get(Log.class, logID);
	}

	@Override
	@Transactional
	public boolean update(Log log) {
		try {
			sessionFactory.getCurrentSession().update(log);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public boolean delete(int logID) {
		try {
			Query query = sessionFactory.getCurrentSession().createQuery("DELETE FROM PersistenceLog WHERE LogID = :logID");
			query.setParameter("logID", logID);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public List<Log> list() {
		@SuppressWarnings("unchecked")
		List<Log> logList = (List<Log>) sessionFactory.getCurrentSession().createCriteria(Log.class)
				.addOrder(Order.asc("LogID")).list();
		
		return logList;
	}

	@Override
	@Transactional
	public Log getByUniqueKey(String startTime, String streamID) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM PersistenceLog WHERE StreamID = :streamID and StartTime = :startTime");
		if (startTime != null) {
			query.setParameter("startTime", startTime);
			query.setParameter("streamID", streamID);
		}
		Log log = (Log) query.uniqueResult();
		return log;
	}

	@Override
	@Transactional
	public List<Log> search(Map<String, String> map) {
		Session session = sessionFactory.getCurrentSession();
		String hqlQuery = "FROM PersistenceLog WHERE ";
		
//		// get all the columns of the PersistenceLog
//		// remove the parameters which doesn't match with column in the list
//		AbstractEntityPersister aep=((AbstractEntityPersister)sessionFactory.getClassMetadata(PersistenceLog.class));  
//		String[] properties = aep.getPropertyNames();
//		List<String> columnNames = Arrays.asList(properties);
//		
//		// remove parameters that doesn't match the scheme.
//		for(String key : map.keySet()){
//			if(!columnNames.contains(key))
//				map.remove(key);
//		}
//		
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
		List<Log> logList = (List<Log>) query.list();
		return logList;
	}

	@Override
	@Transactional
	public List<Log> listSearch(Map<String, List<String>> map) {
		Session session = sessionFactory.getCurrentSession();
		String hqlQuery = "FROM PersistenceLog WHERE ";
			
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
			if(key.equals("LogID")){
				List<Integer> valueList = new ArrayList<Integer>();
				for(String value: map.get(key)){
					valueList.add(Integer.parseInt(value));
				}
				query.setParameterList(key, valueList);
			}else
				query.setParameterList(key, map.get(key));
		}
		@SuppressWarnings("unchecked")
		List<Log> logList = (List<Log>) query.list();
		return logList;
	}

}
