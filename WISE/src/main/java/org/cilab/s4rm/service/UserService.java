package org.cilab.s4rm.service;

import java.util.List;
import java.util.Map;

import org.cilab.s4rm.model.User;

public interface UserService {
	
	/**
	 * Class Name:	UnitService.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.06.10
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	public boolean newInstance(User user);
	public User readInstance(String userID);
	public boolean updateInstance(User user);
	public boolean deleteInstance(String userID);	
	public List<User> readCollection();
	
	public boolean isInstanceExist(String surName, String givenName);
	public List<User> search(Map<String, String> map);
	public List<User> listSearch(Map<String, List<String>> map);
}
