package org.cilab.m4.security;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.resource.ResourceException;

public interface AuthenticationService{
	public Account authenticate(String email, String password) throws ResourceException;
	public Account authenticate(String  jwt) throws ResourceException;
	
	public boolean hasPermission(Account account, String role) throws ResourceException;
}
