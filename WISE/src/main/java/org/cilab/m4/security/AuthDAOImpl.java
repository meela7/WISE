package org.cilab.m4.security;

import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.account.AccountList;
import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.authc.AuthenticationRequest;
import com.stormpath.sdk.authc.AuthenticationResult;
import com.stormpath.sdk.authc.UsernamePasswordRequests;
import com.stormpath.sdk.api.ApiKey;
import com.stormpath.sdk.api.ApiKeys;
import com.stormpath.sdk.client.Client;
import com.stormpath.sdk.client.Clients;
import com.stormpath.sdk.resource.ResourceException;

@Component
public class AuthDAOImpl implements AuthDAO {
	
	/**
	 * Class Name:	AuthDAOImpl.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.06.29
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	
	Client client;
	Application app;
	
	public AuthDAOImpl() {
//		InputStream is = getClass().getResourceAsStream("/apiKey.properties");
		Properties properties = new Properties();
		try {
//			properties.load(is);
			properties.setProperty("apiKey.id", "6WPMKL8EBTLNRRLGCFC5SWZ6D");
			properties.setProperty("apiKey.secret",
					"gjTz3VOEy+asNjEJWax1L7vFUd9l0Gx01DXRkbPsA0Y");
			//meelankang@gmail.com 
			ApiKey apiKey = ApiKeys.builder().setProperties(properties).build();
			this.client = Clients.builder().setApiKey(apiKey).build();
			this.app = client
					.getResource(
							"https://api.stormpath.com/v1/applications/1Nr6BYCIz2gYsECWMEH5VR",
							Application.class);
			//Stormpath Application
		} catch (Exception e) { //IOException을 Exception으로 변경
			e.printStackTrace();
		}
		
	}
	
	@Override
	public User createUser(User user) throws ResourceException {
		Account acc = client.instantiate(Account.class)
				.setGivenName(user.getGivenName())
				.setSurname(user.getSurName())
				.setEmail(user.getEmail())
				.setUsername(user.getUserName())
				.setPassword(user.getPassword());
		
		try {
			app.createAccount(acc);
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
		return null;
	}

	@Override
	public User readUser(User user) throws ResourceException {
		Account acc = authenticate(user.getEmail(), user.getPassword());
		
		if (acc != null) {
			user.setGivenName(acc.getGivenName());
			user.setSurName(acc.getSurname());
			return user;
		} else {
			return null;
		}
	}

	@Override
	public User updateUser(User user) throws ResourceException {
		Account acc = authenticate(user.getEmail(), user.getPassword());
		if (acc != null) {
			if (user.getGivenName() != null) acc.setGivenName(user.getGivenName()).save();
			if (user.getPassword() != null) acc.setPassword(user.getPassword()).save();
			if (user.getSurName() != null) acc.setSurname(user.getSurName()).save();
			if (user.getUserName() != null) acc.setUsername(user.getUserName()).save();

			return user;
		} else {
			return null;
		}
	}

	@Override
	public User deleteUser(User user) throws ResourceException {
		Account acc = authenticate(user.getEmail(), user.getPassword());		
		if (acc != null) {
			user.setEmail(acc.getEmail());
			user.setGivenName(acc.getGivenName());
			user.setSurName(acc.getSurname());
			user.setUserName(acc.getUsername());
			acc.delete();
			return user;
		} else {
			return null;
		}

	}
	
	@Override
	public List<User> listUser(String id, String password) throws ResourceException {
		Account acc = authenticate(id, password);
		List<User> list = Lists.newArrayList();
		
		if (acc != null) {
			AccountList accounts = app.getAccounts();
			for (Account account : accounts) {
				User user = new User();
				user.setEmail(account.getEmail());
				user.setGivenName(account.getGivenName());
				user.setSurName(account.getSurname());
				user.setUserName(account.getUsername());
				list.add(user);
			}
			return list;
		}
		
		return null;
	}

	public Account authenticate(String email, String password) {
		Account authenticated = null;
		try {
		    AuthenticationRequest authenticationRequest = UsernamePasswordRequests.builder() // @modified UsernamePasswordRequest to UsernamePasswordRequests At 0630 (deprecated) 
		            .setUsernameOrEmail(email)
		            .setPassword(password)
		            .build();
		    AuthenticationResult result = app.authenticateAccount(authenticationRequest); 
		    authenticated = result.getAccount();
		} catch (ResourceException ex) {
		    System.out.println(ex.getStatus()); // Will output: 400
		    System.out.println(ex.getCode()); // Will output: 400
		    System.out.println(ex.getMessage()); // Will output: "Invalid username or password."
		    System.out.println(ex.getDeveloperMessage()); // Will output: "Invalid username or password."
		    System.out.println(ex.getMoreInfo()); // Will output: "mailto:support@stormpath.com"
		}
		
		return authenticated;
	}

}
