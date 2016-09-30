package org.cilab.m4.service.impl;

import java.util.List;

import org.cilab.m4.security.AuthDAO;
import org.cilab.m4.security.User;
import org.cilab.m4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired; 

public class UserServiceImpl implements UserService { 
 
    @Autowired 
    AuthDAO authDao; 
 
    public void setAuthDao(AuthDAO authDao){
    	this.authDao = authDao;
	}
    
    @Override
    public User createUser(User user) {
    	return this.authDao.createUser(user);
	}

    @Override
	public User readUser(User user) {
    	return this.authDao.readUser(user);
	}

	@Override
	public User updateUser(User user) {
		return this.authDao.updateUser(user);
	}
	
	@Override
	public User deleteUser(User user) {
		return this.authDao.deleteUser(user);
	}

	@Override
	public List<User> listUser(String id, String password) {
		return this.authDao.listUser(id, password);
	} 
}