package org.cilab.m4.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.cilab.m4.dao.EntityDAO;
import org.cilab.m4.model.Entity;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public class EntityDAOImpl implements EntityDAO {

	/**
	 * Class Name:	MethodDAOImpl.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.16
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	private static final Logger logger = LoggerFactory.getLogger(EntityDAOImpl.class);

	private SessionFactory sessionFactory;
	
	public EntityDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	@Transactional
	public boolean create(Entity entity) {
		try {
			sessionFactory.getCurrentSession().save(entity);			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public Entity read(int entityID) {
		return (Entity) sessionFactory.getCurrentSession().get(Entity.class, entityID);
	}

	@Override
	@Transactional
	public boolean update(Entity entity) {
		try {
			sessionFactory.getCurrentSession().update(entity);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public boolean delete(int entityID) {
		try {
			Query query = sessionFactory.getCurrentSession().createQuery("DELETE FROM Entity WHERE EntityID = :entityID");
			query.setParameter("entityID", entityID);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public List<Entity> list() {
		@SuppressWarnings("unchecked")
		List<Entity> methodList = (List<Entity>) sessionFactory.getCurrentSession().createCriteria(Entity.class)
				.addOrder(Order.asc("EntityID")).list();
		
		return methodList;
	}

	@Override
	@Transactional
	public Entity getByUniqueKey(String name) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM Entity WHERE EntityName = :entityName ");
		if (name != null) {
			query.setParameter("entityName", name);
		}
		Entity entity = (Entity) query.uniqueResult();
		return entity;
	}

	@Override
	@Transactional
	public List<Entity> search(Map<String, String> map) {
		Session session = sessionFactory.getCurrentSession();
		String hqlQuery = "FROM Entity WHERE ";
		
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
		List<Entity> entityList = (List<Entity>) query.list();
		return entityList;
	}

	@Override
	@Transactional
	public List<Entity> listSearch(Map<String, List<String>> map) {
		Session session = sessionFactory.getCurrentSession();
		String hqlQuery = "FROM Entity WHERE ";
		
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
			if(key.equals("EntityID")){
				List<Integer> valueList = new ArrayList<Integer>();
				for(String value: map.get(key)){
					valueList.add(Integer.parseInt(value));
				}
				query.setParameterList(key, valueList);
			}else
				query.setParameterList(key, map.get(key));
		}
		@SuppressWarnings("unchecked")
		List<Entity> entityList = (List<Entity>) query.list();
		
		return entityList;
	}

}
