package org.cilab.m4.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.cilab.m4.dao.InstrumentDAO;
import org.cilab.m4.model.Instrument;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public class InstrumentDAOImpl implements InstrumentDAO {
	
	/**
	 * Class Name:	InstrumentDAOImpl.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.13
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	private static final Logger logger = LoggerFactory.getLogger(InstrumentDAOImpl.class);

	private SessionFactory sessionFactory;
	
	public InstrumentDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	@Transactional
	public boolean create(Instrument inst) {		
		logger.info("=====> Create a Instrument Type is {}", inst.getMethodType());
		try {
			sessionFactory.getCurrentSession().save(inst);
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public Instrument read(int instID) {
		Instrument inst = (Instrument) sessionFactory.getCurrentSession().get(Instrument.class, instID);
		
		return inst;
	}

	@Override
	@Transactional
	public boolean update(Instrument inst) {
		try {
			sessionFactory.getCurrentSession().update(inst);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public boolean delete(int instID) {
		try {
			Query query = sessionFactory.getCurrentSession().createQuery("DELETE FROM Instrument WHERE MethodID = :instID");
			query.setParameter("instID", instID);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public List<Instrument> list() {
		@SuppressWarnings("unchecked")
		List<Instrument> instList = (List<Instrument>) sessionFactory.getCurrentSession().createCriteria(Instrument.class).list();
		
		return instList;
	}

	@Override
	@Transactional
	public List<Instrument> search(Map<String, String> map) {
		Session session = sessionFactory.getCurrentSession();
		String hqlQuery = "FROM Instrument WHERE ";
		
		// get all the columns of the Instrument
		// remove the parameters which doesn't match with column in the list
		AbstractEntityPersister aep=((AbstractEntityPersister)sessionFactory.getClassMetadata(Instrument.class));  
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
		List<Instrument> inst = (List<Instrument>) query.list();
		return inst;
	}

	@Override
	@Transactional
	public List<Instrument> listSearch(Map<String, List<String>> map) {
		Session session = sessionFactory.getCurrentSession();
		String hqlQuery = "FROM Instrument WHERE ";
		
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
				List<Integer> values = new ArrayList<Integer>();
				for(String value: map.get(key)){
					values.add(Integer.parseInt(value));
				}
				query.setParameterList(key, values);
			}else
				query.setParameterList(key, map.get(key));
		}
		@SuppressWarnings("unchecked")
		List<Instrument> inst = (List<Instrument>) query.list();
		
		return inst;
	}

}
