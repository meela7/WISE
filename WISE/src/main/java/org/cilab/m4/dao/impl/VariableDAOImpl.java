package org.cilab.m4.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.cilab.m4.dao.VariableDAO;
import org.cilab.m4.model.Variable;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public class VariableDAOImpl implements VariableDAO {

	/**
	 * Class Name:	VariableDAOImpl.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.16
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	
	private static final Logger logger = LoggerFactory.getLogger(VariableDAOImpl.class);

	private SessionFactory sessionFactory;
	
	public VariableDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	@Transactional
	public boolean create(Variable variable) {
		try {
			sessionFactory.getCurrentSession().save(variable);			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public Variable read(int variableID) {
		return (Variable) sessionFactory.getCurrentSession().get(Variable.class, variableID);
	}

	@Override
	@Transactional
	public boolean update(Variable variable) {
		try {
			sessionFactory.getCurrentSession().update(variable);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public boolean delete(int variableID) {
		try {
			Query query = sessionFactory.getCurrentSession().createQuery("DELETE FROM Variable WHERE VariableID = :variableID");
			query.setParameter("variableID", variableID);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public List<Variable> list() {
		@SuppressWarnings("unchecked")
		List<Variable> variableList = (List<Variable>) sessionFactory.getCurrentSession().createCriteria(Variable.class)
				.addOrder(Order.asc("VariableID")).list();
		
		return variableList;
	}

	@Override
	@Transactional
	public Variable getByUniqueKey(String name, int unitID) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery("SELECT * FROM Variable WHERE VariableName = :variableName and UnitID = :unitID");
		if (name != null) {
			query.setParameter("variableName", name);
			query.setParameter("unitID", unitID);
		}
		Variable variable = (Variable) query.uniqueResult();
		return variable;
	}

	@Override
	@Transactional
	public List<Variable> search(Map<String, String> map) {
		Session session = sessionFactory.getCurrentSession();
		String hqlQuery = "FROM Variable WHERE ";
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
		List<Variable> variableList = (List<Variable>) query.list();
		return variableList;
	}

	@Override
	@Transactional
	public List<Variable> listSearch(Map<String, List<String>> map) {
		Session session = sessionFactory.getCurrentSession();
		String hqlQuery = "FROM Variable WHERE ";
		
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
		List<Variable> variableList = (List<Variable>) query.list();
		
		return variableList;
	}

}
