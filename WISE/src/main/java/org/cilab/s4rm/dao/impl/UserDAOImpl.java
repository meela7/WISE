package org.cilab.s4rm.dao.impl;

import java.util.List;
import java.util.Map;

import org.cilab.s4rm.dao.UserDAO;
import org.cilab.s4rm.model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public class UserDAOImpl implements UserDAO {

	/**
	 * Class Name:	UserDAOImpl.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.06.10
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	
	private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

	private SessionFactory sessionFactory;
	
	public UserDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	@Transactional
	public boolean create(User user) {
		try {
			sessionFactory.getCurrentSession().save(user);			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public User read(String userID) {
		return (User) sessionFactory.getCurrentSession().get(User.class, userID);
//		User user = new User();
//		try {
//			Query query = sessionFactory.getCurrentSession().createQuery("FROM User WHERE UserID = :userID");
//			query.setParameter("userID", userID);
//			user = (User) query.uniqueResult();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return user;
	}

	@Override
	@Transactional
	public boolean update(User user) {
		try {
			sessionFactory.getCurrentSession().update(user);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public boolean delete(String userID) {
		
		try {
			Query query = sessionFactory.getCurrentSession().createQuery("DELETE FROM User WHERE UserID = :userID");
			query.setParameter("userID", userID);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public List<User> list() {
		@SuppressWarnings("unchecked")
		List<User> userList = (List<User>) sessionFactory.getCurrentSession().createCriteria(User.class).list();
		
		return userList;
	}

	@Override
	@Transactional
	public User getByUniqueKey(String surName, String givenName) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM User WHERE SurName = :surName and GivenName = :givenName");
		if (surName != null && givenName != null) {
			query.setParameter("surName", surName);
			query.setParameter("givenName", givenName);
		}
		User user = (User) query.uniqueResult();
		return user;
	}

	@Override
	@Transactional
	public List<User> search(Map<String, String> map) {
		Session session = sessionFactory.getCurrentSession();
		String hqlQuery = "FROM User WHERE ";
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
		List<User> userList = (List<User>) query.list();
		return userList;
	}

	@Override
	@Transactional
	public List<User> listSearch(Map<String, List<String>> map) {
		Session session = sessionFactory.getCurrentSession();
		String hqlQuery = "FROM User WHERE ";
		
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
			query.setParameterList(key, map.get(key));
		}
		@SuppressWarnings("unchecked")
		List<User> userList = (List<User>) query.list();
		
		return userList;
	}

}
