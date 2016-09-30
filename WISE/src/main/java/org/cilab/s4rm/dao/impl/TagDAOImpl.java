package org.cilab.s4rm.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.cilab.s4rm.dao.TagDAO;
import org.cilab.s4rm.model.Tag;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public class TagDAOImpl implements TagDAO {

	/**
	 * Class Name:	TagDAOImpl.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.06.16
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	
	private static final Logger logger = LoggerFactory.getLogger(TagDAOImpl.class);

	private SessionFactory sessionFactory;
	
	public TagDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	@Transactional
	public boolean create(Tag tag) {
		try {
			sessionFactory.getCurrentSession().save(tag);			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public Tag read(int tagID) {
		return (Tag) sessionFactory.getCurrentSession().get(Tag.class, tagID);
	}

	@Override
	@Transactional
	public boolean update(Tag tag) {
		try {
			sessionFactory.getCurrentSession().update(tag);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public boolean delete(int tagID) {
		try {
			Query query = sessionFactory.getCurrentSession().createQuery("DELETE FROM Tag WHERE TagID = :tagID");
			query.setParameter("tagID", tagID);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public List<Tag> list() {
		@SuppressWarnings("unchecked")
		List<Tag> tagList = (List<Tag>) sessionFactory.getCurrentSession().createCriteria(Tag.class)
				.addOrder(Order.asc("TagID")).list();
		
		return tagList;
	}

	@Override
	@Transactional
	public Tag getByUniqueKey(String name) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM Tag WHERE Name = :name");
		if (name != null) {
			query.setParameter("name", name);
		}
		Tag tag = (Tag) query.uniqueResult();
		return tag;
	}

	@Override
	@Transactional
	public List<Tag> search(Map<String, String> map) {
		Session session = sessionFactory.getCurrentSession();
		String hqlQuery = "FROM Tag WHERE ";
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
		List<Tag> tagList = (List<Tag>) query.list();
		return tagList;
	}

	@Override
	@Transactional
	public List<Tag> listSearch(Map<String, List<String>> map) {
		Session session = sessionFactory.getCurrentSession();
		String hqlQuery = "FROM Tag WHERE ";
		
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
		List<Tag> tagList = (List<Tag>) query.list();
		
		return tagList;
	}

}
