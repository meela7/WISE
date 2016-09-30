package org.cilab.m4.security;

import java.util.Properties;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.api.ApiKey;
import com.stormpath.sdk.api.ApiKeys;
import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.authc.AuthenticationRequest;
import com.stormpath.sdk.authc.AuthenticationResult;
import com.stormpath.sdk.authc.UsernamePasswordRequests;
import com.stormpath.sdk.client.Client;
import com.stormpath.sdk.client.Clients;
import com.stormpath.sdk.oauth.Authenticators;
import com.stormpath.sdk.oauth.JwtAuthenticationRequest;
import com.stormpath.sdk.oauth.JwtAuthenticationResult;
import com.stormpath.sdk.oauth.Oauth2Requests;
import com.stormpath.sdk.resource.ResourceException;

public class AuthenticationServiceImpl implements AuthenticationService {
	
	/**
	 * Class Name:	AuthenticationServiceImpl.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.06.29
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	
	private Client client;
	private Application application;
	private String administratorGroupURL = "https://api.stormpath.com/v1/groups/30MySVnumYLVW0688bu9WU";
	private String userGroupURL = "https://api.stormpath.com/v1/groups/3izlU37XeKsdGDEmM8Mpyk";
	
	public AuthenticationServiceImpl() {
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
			this.application = client
					.getResource(
							"https://api.stormpath.com/v1/applications/1Nr6BYCIz2gYsECWMEH5VR",
							Application.class);
			//WISE Application
		} catch (Exception e) { //IOException을 Exception으로 변경
			e.printStackTrace();
		}
		
	}
	
	@Override
	public Account authenticate(String email, String password) throws ResourceException {
		Account authenticated = null;
		
	    AuthenticationRequest authenticationRequest = UsernamePasswordRequests.builder() // @modified UsernamePasswordRequest to UsernamePasswordRequests At 0630 (deprecated) 
	            .setUsernameOrEmail(email)
	            .setPassword(password)
	            .build();
	    AuthenticationResult result = application.authenticateAccount(authenticationRequest); 
	    authenticated = result.getAccount();		
		
		return authenticated;
	}

	@Override
	public Account authenticate(String jwt) throws ResourceException {
		Account authenticated = null;
		
		JwtAuthenticationRequest jwtRequest = Oauth2Requests.JWT_AUTHENTICATION_REQUEST.builder() 
				.setJwt(jwt) 
				.build(); 
		JwtAuthenticationResult jwtAuthenticationResult = Authenticators.JWT_AUTHENTICATOR 
				.forApplication(application) 
				.authenticate(jwtRequest);
		authenticated = jwtAuthenticationResult.getAccount();
		
		return authenticated;
	}

	@Override
	public boolean hasPermission(Account account, String role) throws ResourceException {
        boolean permission = false;
        if (account.getGroupMemberships().iterator().hasNext()) {
            String groupHref = account.getGroupMemberships().iterator().next().getGroup().getHref();
            if (role.equalsIgnoreCase("ADMIN")) {
            	permission = groupHref.equals(administratorGroupURL);
            } else if (role.equalsIgnoreCase("USER")) {
            	permission = groupHref.equals(userGroupURL);
            }
        }

        return permission;	   
	}

}
