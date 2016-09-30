package org.cilab.s4rm.service.impl;

import java.util.List;
import java.util.Map;

import org.cilab.s4rm.dao.UserDAO;
import org.cilab.s4rm.model.User;
import org.cilab.s4rm.service.UserService;

public class UserServiceImpl implements UserService {

	/**
	 * Class Name:	UnitServiceImpl.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.06.13
	 * @version 1.0
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	
	UserDAO userDao;
	
	public void setUserDao(UserDAO userDao){
		this.userDao = userDao;
	}
	
	@Override
	public boolean newInstance(User user) {
		return this.userDao.create(user);
	}

	@Override
	public User readInstance(String userID) {
		return this.userDao.read(userID);
	}

	@Override
	public boolean updateInstance(User user) {
		return this.userDao.update(user);
	}

	@Override
	public boolean deleteInstance(String userID) {
		return this.userDao.delete(userID);
	}

	@Override
	public List<User> readCollection() {
		return this.userDao.list();
	}

	@Override
	public boolean isInstanceExist(String surName, String givenName) {
		if(this.userDao.getByUniqueKey(surName, givenName) != null)
			return true;
		else
			return false;
	}

	@Override
	public List<User> search(Map<String, String> map) {
		return this.userDao.search(map);
	}

	@Override
	public List<User> listSearch(Map<String, List<String>> map) {
		return this.userDao.listSearch(map);
	}

}
