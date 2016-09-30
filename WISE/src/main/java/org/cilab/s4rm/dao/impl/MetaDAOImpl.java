package org.cilab.s4rm.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.cilab.s4rm.dao.MetaDAO;
import org.cilab.s4rm.model.Metadata;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public class MetaDAOImpl implements MetaDAO {

	/**
	 * Class Name:	MetaDAOImpl.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.06.16
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	
	private static final Logger logger = LoggerFactory.getLogger(MetaDAOImpl.class);

	private SessionFactory sessionFactory;
	
	public MetaDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	@Transactional
	public boolean create(Metadata meta) {
		try {
			sessionFactory.getCurrentSession().save(meta);			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public Metadata read(int metaID) {
		return (Metadata) sessionFactory.getCurrentSession().get(Metadata.class, metaID);

	}

	@Override
	@Transactional
	public boolean update(Metadata meta) {
		try {
			sessionFactory.getCurrentSession().update(meta);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public boolean delete(int metaID) {
		try {
			Query query = sessionFactory.getCurrentSession().createQuery("DELETE FROM Metadata WHERE MetadataID = :metaID");
			query.setParameter("metaID", metaID);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public List<Metadata> list() {
		@SuppressWarnings("unchecked")
		List<Metadata> metaList = (List<Metadata>) sessionFactory.getCurrentSession().createCriteria(Metadata.class)
				.addOrder(Order.asc("MetadataID")).list();
		
		return metaList;
	}

	@Override
	@Transactional
	public Metadata getByUniqueKey(String key, String value) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM Metadata WHERE Key = :key and Value = :value");
		if (key != null && value != null) {
			query.setParameter("key", key);
			query.setParameter("value", value);
		}
		Metadata meta = (Metadata) query.uniqueResult();
		return meta;
	}

	@Override
	@Transactional
	public List<Metadata> search(Map<String, String> map) {
		Session session = sessionFactory.getCurrentSession();
		String hqlQuery = "FROM Metadata WHERE ";
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
		List<Metadata> metaList = (List<Metadata>) query.list();
		return metaList;
	}

	@Override
	@Transactional
	public List<Metadata> listSearch(Map<String, List<String>> map) {
		Session session = sessionFactory.getCurrentSession();
		String hqlQuery = "FROM Metadata WHERE ";
		
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
			if(key.equals("TagID")){
				List<Integer> valueList = new ArrayList<Integer>();
				for(String value: map.get(key)){
					valueList.add(Integer.parseInt(value));
				}
				query.setParameterList(key, valueList);
			}else
				query.setParameterList(key, map.get(key));
		}
		@SuppressWarnings("unchecked")
		List<Metadata> metaList = (List<Metadata>) query.list();
		
		return metaList;
	}

}
