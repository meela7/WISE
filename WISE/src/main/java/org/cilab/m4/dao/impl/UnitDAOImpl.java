package org.cilab.m4.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.cilab.m4.dao.UnitDAO;
import org.cilab.m4.model.Unit;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public class UnitDAOImpl implements UnitDAO {

	/**
	 * Class Name:	UnitDAOImpl.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.16
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	
	private static final Logger logger = LoggerFactory.getLogger(UnitDAOImpl.class);

	private SessionFactory sessionFactory;
	
	public UnitDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	@Transactional
	public boolean create(Unit unit) {
		try {
			sessionFactory.getCurrentSession().save(unit);			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public Unit read(int unitID) {
		return (Unit) sessionFactory.getCurrentSession().get(Unit.class, unitID);
	}

	@Override
	@Transactional
	public boolean update(Unit unit) {
		try {
			sessionFactory.getCurrentSession().update(unit);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public boolean delete(int unitID) {
		try {
			Query query = sessionFactory.getCurrentSession().createQuery("DELETE FROM Unit WHERE UnitID = :unitID");
			query.setParameter("unitID", unitID);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public List<Unit> list() {
		@SuppressWarnings("unchecked")
		List<Unit> unitList = (List<Unit>) sessionFactory.getCurrentSession().createCriteria(Unit.class)
				.addOrder(Order.asc("UnitID")).list();
		
		return unitList;
	}

	@Override
	@Transactional
	public Unit getByUniqueKey(String name) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM Unit WHERE UnitName = :unitName ");
		if (name != null) {
			query.setParameter("unitName", name);
		}
		return (Unit)query.uniqueResult();
	}

	@Override
	@Transactional
	public List<Unit> search(Map<String, String> map) {
		Session session = sessionFactory.getCurrentSession();
		String hqlQuery = "FROM Unit WHERE ";
		
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
		List<Unit> unitList = (List<Unit>) query.list();
		return unitList;
	}

	@Override
	@Transactional
	public List<Unit> listSearch(Map<String, List<String>> map) {
		Session session = sessionFactory.getCurrentSession();
		String hqlQuery = "FROM Unit WHERE ";
		
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
			if(key.equals("UnitID")){
				List<Integer> valueList = new ArrayList<Integer>();
				for(String value: map.get(key)){
					valueList.add(Integer.parseInt(value));
				}
				query.setParameterList(key, valueList);
			}else
				query.setParameterList(key, map.get(key));
		}
		@SuppressWarnings("unchecked")
		List<Unit> unitList = (List<Unit>) query.list();
		
		return unitList;
	}

}
