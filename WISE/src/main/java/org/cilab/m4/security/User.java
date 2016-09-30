package org.cilab.m4.security;

import com.stormpath.sdk.account.Account;

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
	
//	private String id;
	private String userName;
	private String password;
	private String givenName;
	private String surName;	
	private String email;
//	private String groupURL;
//	private Account account;
	
	public User(User user) {
        if (user != null) {
//            setAccount(user.getAccount());
            setEmail(user.getEmail());
            setGivenName(user.getGivenName());
//            setId(user.getId());
            setSurName(user.getSurName());
            setPassword(user.getPassword());
            setUserName(user.getUserName());
        }
    }

    public User(Account account) {
        if (account != null) {
            setEmail(account.getEmail());
            setGivenName(account.getGivenName());
            setSurName(account.getSurname());
            setUserName(account.getUsername());
//            setAccount(account);
        }
    }
    
	public User() {

	}

//	public String getId() {
//		return id;
//	}
//	public void setId(String id) {
//		this.id = id;
//	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getGivenName() {
		return givenName;
	}
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}
	public String getSurName() {
		return surName;
	}
	public void setSurName(String surName) {
		this.surName = surName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
//	public String getGroupURL() {
//		return groupURL;
//	}
//	public void setGroupURL(String groupURL) {
//		this.groupURL = groupURL;
//	}
//	public Account getAccount() {
//		return account;
//	}
//	public void setAccount(Account account) {
//		this.account = account;
//	}	
}
