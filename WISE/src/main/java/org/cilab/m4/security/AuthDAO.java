package org.cilab.m4.security;

import java.util.List;

import com.stormpath.sdk.resource.ResourceException;

public interface AuthDAO {
	
	/**
	 * Class Name:	AuthDAO.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.06.29
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */

	public User createUser(User user) throws ResourceException ;
	public User readUser(User user) throws ResourceException ;
	public User updateUser(User user) throws ResourceException ;
	public User deleteUser(User user) throws ResourceException ;
	public List<User> listUser(String id, String password) throws ResourceException ;
}
