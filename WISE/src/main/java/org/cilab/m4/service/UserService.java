package org.cilab.m4.service;

import java.util.List;

import org.cilab.m4.security.User;

public interface UserService { 
	 
	public User createUser(User user);
	public User readUser(User user);
	public User updateUser(User user);
	public User deleteUser(User user);
	public List<User> listUser(String id, String password);
 
}
