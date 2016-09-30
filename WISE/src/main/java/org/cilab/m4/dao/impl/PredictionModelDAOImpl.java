package org.cilab.m4.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.cilab.m4.dao.PredictionModelDAO;
import org.cilab.m4.model.PredictionModel;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public class PredictionModelDAOImpl implements PredictionModelDAO {

	/**
	 * Class Name:	PredictionModelDAOImpl.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.07.05
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	private static final Logger logger = LoggerFactory.getLogger(PredictionModelDAOImpl.class);

	private SessionFactory sessionFactory;
	
	public PredictionModelDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	@Transactional
	public boolean create(PredictionModel model) {
		try {
			sessionFactory.getCurrentSession().save(model);			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public PredictionModel read(int modelID) {
		PredictionModel model = (PredictionModel) sessionFactory.getCurrentSession().get(PredictionModel.class, modelID);
		return model;
	}

	@Override
	@Transactional
	public boolean update(PredictionModel model) {
		try {
			sessionFactory.getCurrentSession().update(model);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public boolean delete(int modelID) {
		try {
			Query query = sessionFactory.getCurrentSession().createQuery("DELETE FROM PredictionModel WHERE MethodID = :modelID");
			query.setParameter("modelID", modelID);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public List<PredictionModel> list() {
		@SuppressWarnings("unchecked")
		List<PredictionModel> modelList = (List<PredictionModel>) sessionFactory.getCurrentSession().createCriteria(PredictionModel.class).list();
		
		return modelList;
	}

	@Override
	@Transactional
	public List<PredictionModel> search(Map<String, String> map) {
		Session session = sessionFactory.getCurrentSession();
		String hqlQuery = "FROM PredictionModel WHERE ";
		
		// get all the columns of the Instrument
		// remove the parameters which doesn't match with column in the list
		AbstractEntityPersister aep=((AbstractEntityPersister)sessionFactory.getClassMetadata(PredictionModel.class));  
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
		List<PredictionModel> modelList = (List<PredictionModel>) query.list();
		return modelList;
	}

	@Override
	@Transactional
	public List<PredictionModel> listSearch(Map<String, List<String>> map) {
		Session session = sessionFactory.getCurrentSession();
		String hqlQuery = "FROM PredictionModel WHERE ";
		
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
		List<PredictionModel> modelList = (List<PredictionModel>) query.list();
		
		return modelList;
	}

}
