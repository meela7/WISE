package org.cilab.m4.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.cilab.m4.dao.PredictionDAO;
import org.cilab.m4.model.Prediction;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public class PredictionDAOImpl implements PredictionDAO {

	/**
	 * Class Name:	PredictionDAOImpl.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.13
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	private static final Logger logger = LoggerFactory.getLogger(PredictionDAOImpl.class);

	private SessionFactory sessionFactory;
	
	public PredictionDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	@Transactional
	public boolean create(Prediction pred) {
		try {
			sessionFactory.getCurrentSession().save(pred);			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public Prediction read(int predID) {
		Prediction pred = (Prediction) sessionFactory.getCurrentSession().get(Prediction.class, predID);
		return pred;
	}

	@Override
	@Transactional
	public boolean update(Prediction pred) {
		try {
			sessionFactory.getCurrentSession().update(pred);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public boolean delete(int predID) {
		try {
			Query query = sessionFactory.getCurrentSession().createQuery("DELETE FROM Prediction WHERE MethodID = :predictionID");
			query.setParameter("predictionID", predID);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public List<Prediction> list() {
		@SuppressWarnings("unchecked")
		List<Prediction> predList = (List<Prediction>) sessionFactory.getCurrentSession().createCriteria(Prediction.class).list();
		
		return predList;
	}

	@Override
	@Transactional
	public List<Prediction> search(Map<String, String> map) {
		Session session = sessionFactory.getCurrentSession();
		String hqlQuery = "FROM Prediction WHERE ";
		
		// get all the columns of the Instrument
		// remove the parameters which doesn't match with column in the list
		AbstractEntityPersister aep=((AbstractEntityPersister)sessionFactory.getClassMetadata(Prediction.class));  
		String[] properties = aep.getPropertyNames();
		List<String> columnNames = Arrays.asList(properties);
		
		for(String key : map.keySet()){
			if(!columnNames.contains(key))
				map.remove(key);
		}
		
		// get parameters from map and Uppercase the first letter
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
			query.setParameter(key, map.get(key));
		}
		@SuppressWarnings("unchecked")
		List<Prediction> predList = (List<Prediction>) query.list();
		return predList;
	}

	@Override
	@Transactional
	public List<Prediction> listSearch(Map<String, List<String>> map) {
		Session session = sessionFactory.getCurrentSession();
		String hqlQuery = "FROM Prediction WHERE ";
		
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
			if(key.equals("MethodID")){
				List<Integer> valueList = new ArrayList<Integer>();
				for(String value: map.get(key)){
					valueList.add(Integer.parseInt(value));
				}
				query.setParameterList(key, valueList);
			}else
				query.setParameterList(key, map.get(key));
		}
		@SuppressWarnings("unchecked")
		List<Prediction> predList = (List<Prediction>) query.list();
		
		return predList;
	}

}
