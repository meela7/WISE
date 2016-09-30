package org.cilab.s4rm.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.cilab.s4rm.dao.StreamDAO;
import org.cilab.s4rm.model.Stream;
import org.cilab.s4rm.model.Stream_Meta;
import org.cilab.s4rm.model.Stream_Tag;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public class StreamDAOImpl implements StreamDAO {

	/**
	 * Class Name:	StreamDAOImpl.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.16
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	
	private static final Logger logger = LoggerFactory.getLogger(StreamDAOImpl.class);

	private SessionFactory sessionFactory;
	
	public StreamDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	@Transactional
	public boolean create(Stream stream) throws HibernateException{
		
		try {
			Session session = sessionFactory.getCurrentSession();
			
			// inverse-false Set doesn't insert child to DB. therefore, need to insert in before hand.
			for(Stream_Meta meta: stream.getMetas()){
				session.save(meta);
			}
			for(Stream_Tag tag: stream.getTags()){
				session.save(tag);
			}
			session.save(stream);
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public Stream read(String streamID) throws HibernateException{
		return (Stream) sessionFactory.getCurrentSession().get(Stream.class, streamID);
	}

	@Override
	@Transactional
	public boolean update(Stream stream) {
		try {
			// inverse-false Set doesn't insert child to DB. therefore, need to insert new child manually.
			sessionFactory.getCurrentSession().saveOrUpdate(stream);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional 
	public boolean delete(String streamID) throws HibernateException{
		try {
			Query query = sessionFactory.getCurrentSession().createQuery("DELETE FROM Stream WHERE StreamID = :streamID");
			query.setParameter("streamID", streamID);
			query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional
	public List<Stream> list() throws HibernateException{
		@SuppressWarnings("unchecked")
		List<Stream> streamList = (List<Stream>) sessionFactory.getCurrentSession().createCriteria(Stream.class).list();
		
		return streamList;
	}

	@Override
	@Transactional
	public Stream getByUniqueKey(String createdAt, String sensorID) throws HibernateException{
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM Stream WHERE CreatedAt = :createdAt and SensorID = :sensorID ");
		if (createdAt != null) {
			query.setParameter("createdAt", createdAt);
			query.setParameter("sensorID", sensorID);
		}
		return (Stream)query.uniqueResult();
	}

	@Override
	@Transactional
	public List<Stream> search(Map<String, String> map) throws HibernateException{
		Session session = sessionFactory.getCurrentSession();
		String hqlQuery = "FROM Stream WHERE ";
		
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
		List<Stream> streamList = (List<Stream>) query.list();
		return streamList;
	}

	@Override
	@Transactional
	public List<Stream> listSearch(Map<String, List<String>> map) throws HibernateException{
		Session session = sessionFactory.getCurrentSession();
		String hqlQuery = "FROM Stream WHERE ";
		
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
			if(key.equals("StreamID")){
				List<Integer> valueList = new ArrayList<Integer>();
				for(String value: map.get(key)){
					valueList.add(Integer.parseInt(value));
				}
				query.setParameterList(key, valueList);
			}else
				query.setParameterList(key, map.get(key));
		}
		@SuppressWarnings("unchecked")
		List<Stream> streamList = (List<Stream>) query.list();
		
		return streamList;
	}

}
