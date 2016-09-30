package org.cilab.m4.util;

import java.nio.charset.Charset;

import org.apache.commons.codec.binary.Base64;
import org.cilab.m4.security.UserCredential;

public class AuthUtil {
	public static UserCredential toUserCredential(String auth) {
		UserCredential cred = new UserCredential();
		String[] values = null;
		
		if (auth != null && auth.startsWith("Basic")) {
		        // Authorization: Basic base64credentials
		        String base64Credentials = auth.substring("Basic".length()).trim();
		        String credentials = new String(Base64.decodeBase64(base64Credentials), Charset.forName("UTF-8"));

		        // credentials = username:password
		        values = credentials.split(":", 2);
		        cred.setUserId(values[0].toString());
		        cred.setPassword(values[1].toString());
		 }

		return cred;
	}
}
