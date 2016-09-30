package org.cilab.s4rm.model;

import java.util.List;

public class User {

	/**
	 * Class Name: User.java 
	 * Description: 
	 * 
	 * @author Meilan Jiang
	 * @since 2016.06.10
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	
	private String UserID;
	private String SurName;
	private String GivenName;
	private String UserName;
	private String Email;
	private String Profile;
	
	public String getUserID() {
		return UserID;
	}
	public void setUserID(String userID) {
		UserID = userID;
	}
	public String getSurName() {
		return SurName;
	}
	public void setSurName(String surName) {
		SurName = surName;
	}
	public String getGivenName() {
		return GivenName;
	}
	public void setGivenName(String givenName) {
		GivenName = givenName;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getProfile() {
		return Profile;
	}
	public void setProfile(String profile) {
		Profile = profile;
	}	
}
