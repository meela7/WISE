package org.cilab.m4.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.cilab.m4.dao.SourceDAO;
import org.cilab.m4.model.Source;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public class SourceDAOImpl implements SourceDAO {

	/**
	 * Class Name:	SourceDAOImpl.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.14
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	private static final Logger logger = LoggerFactory.getLogger(SourceDAOImpl.class);

	private SessionFactory sessionFactory;
	
	public SourceDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	@Transactional
	public boolean create(Source source) {
		try {
			sessionFactory.getCurrentSession().save(source);			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public Source read(int sourceID) {
		return (Source)this.sessionFactory.getCurrentSession().get(Source.class, sourceID);
	}

	@Override
	@Transactional
	public boolean update(Source source) {
		try {
			sessionFactory.getCurrentSession().update(source);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public boolean delete(int sourceID) {
		try {
			Query query = sessionFactory.getCurrentSession().createQuery("DELETE FROM Source WHERE SourceID = :sourceID");
			query.setParameter("sourceID", sourceID);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public List<Source> list() {
		@SuppressWarnings("unchecked")
		List<Source> siteList = (List<Source>) sessionFactory.getCurrentSession().createCriteria(Source.class)
				.addOrder(Order.asc("SourceID")).list();
		
		return siteList;
	}

	@Override
	@Transactional
	public Source getByUniqueKey(String institution, String name) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM Source WHERE Institution = :institution and ContactName = :name ");
		if (name != null) {
			query.setParameter("institution", institution);
			query.setParameter("name", name);
		}
		Source source = (Source) query.uniqueResult();
		return source;
	}

	@Override
	@Transactional
	public List<Source> search(Map<String, String> map) {
		Session session = sessionFactory.getCurrentSession();
		String hqlQuery = "FROM Source WHERE ";

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
		List<Source> sourceList = (List<Source>) query.list();
		return sourceList;
	}

	@Override
	@Transactional
	public List<Source> listSearch(Map<String, List<String>> map) {
		Session session = sessionFactory.getCurrentSession();
		String hqlQuery = "FROM Source WHERE ";
			
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
			if(key.equals("SourceID")){
				List<Integer> valueList = new ArrayList<Integer>();
				for(String value: map.get(key)){
					valueList.add(Integer.parseInt(value));
				}
				query.setParameterList(key, valueList);
			}else
				query.setParameterList(key, map.get(key));
		}
		@SuppressWarnings("unchecked")
		List<Source> sourceList = (List<Source>) query.list();
		
		return sourceList;
	}

}
