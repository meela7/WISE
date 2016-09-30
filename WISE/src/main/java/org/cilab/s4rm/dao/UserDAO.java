package org.cilab.s4rm.dao;

import java.util.List;
import java.util.Map;

import org.cilab.s4rm.model.User;

public interface UserDAO {
	
	/**
	 * Class Name:	SourceDAO.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.06.10
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	public boolean create(User user);
	public User read(String userID);
	public boolean update(User user);
	public boolean delete(String userID);
	
	public List<User> list();
	public User getByUniqueKey(String surName, String givenName);
	public List<User> search(Map<String, String> map);
	public List<User> listSearch(Map<String, List<String>> map);

}
