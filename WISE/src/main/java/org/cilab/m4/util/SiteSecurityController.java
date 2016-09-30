package org.cilab.m4.util;

import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cilab.m4.model.Site;
import org.cilab.m4.security.AuthenticationServiceImpl;
import org.cilab.m4.security.UserCredential;
import org.cilab.m4.service.SiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.resource.ResourceException;

import io.swagger.annotations.Api;

@Api(value = "sites")
@RestController
public class SiteSecurityController {

	/**
	 * Class Name: SiteController.java Description: CRUD, Service
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.14
	 * @version 1.2
	 * 
	 *          Copyright(c) 2016 by CILAB All right reserved.
	 */
	private static final Logger logger = LoggerFactory.getLogger(SiteSecurityController.class);

	@Autowired
	AuthenticationServiceImpl authService;

	@Autowired
	private SiteService siteService;

	// ========== Read and Search Site Collection Resource ==========
	@RequestMapping(value = "/sites", method = RequestMethod.GET)
	public ResponseEntity<? extends Object> list(@RequestParam(required = false) MultiValueMap<String, String> params,
			@RequestHeader Map<String, String> headers) {
		if (!headers.containsKey("authorization")) {
			return new ResponseEntity<ErrorResponse>(new ErrorResponse(1, "Missing authorization header!", "", ""),
					HttpStatus.NON_AUTHORITATIVE_INFORMATION);
		}
		
		UserCredential cred = AuthUtil.toUserCredential(headers.get("authorization"));
		logger.debug(" ========== UserCredential: {} ========== ", cred.toString());

		Account account = null;
		try {
			account = this.authService.authenticate(cred.getUserId(), cred.getPassword());
		} catch (ResourceException e) {
			e.printStackTrace();
			return new ResponseEntity<ErrorResponse>(
					new ErrorResponse(2, e.getMessage(), e.getDeveloperMessage(), e.getMoreInfo()),
					HttpStatus.NON_AUTHORITATIVE_INFORMATION);
		}
		logger.debug(" ========== Validated User: {}  ========== ", account.getGivenName());
		try {
			if (!this.authService.hasPermission(account, "USER")) {
				return new ResponseEntity<ErrorResponse>(new ErrorResponse(3, "Permission denied!", "", ""),
						HttpStatus.NON_AUTHORITATIVE_INFORMATION);
			}
		} catch (ResourceException ex) {
			ex.printStackTrace();
			return new ResponseEntity<ErrorResponse>(new ErrorResponse(4, ex.getMessage(),
					ex.getDeveloperMessage(), ex.getMoreInfo()), HttpStatus.NON_AUTHORITATIVE_INFORMATION);
		}
		
		// read collection resource
		if (params.isEmpty()) {
			logger.debug(" ========== Reading Site Collection Resource ========== ");
			List<Site> siteList = null;
			try {
				siteList = siteService.readCollection();
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<ErrorResponse>(
						new ErrorResponse(5, e.getLocalizedMessage(), Arrays.toString(e.getStackTrace()), ""),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
			if (siteList.isEmpty()) {
				logger.debug(" ========== No Site Collection Resource found. ========== ");
				return new ResponseEntity<ErrorResponse>(new ErrorResponse(6, "Resource not found!", "", ""), HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<List<Site>>(siteList, HttpStatus.OK);
		}		
		
		logger.debug(" ========== Searching Site Resource ========== ");
		PropertyDescriptor[] props = BeanUtils.getPropertyDescriptors(Site.class);
		List<String> variables = new ArrayList<String>();
		for (PropertyDescriptor desc : props) {
			variables.add(desc.getName());
		}
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for (String key : params.keySet()) {
			if (variables.contains(key)) {
				logger.debug(" ========== Parameter: {} ========== ", key);
				// uppercase first letter of property name
				String param = key.substring(0, 1).toUpperCase();
				param = param + key.substring(1);

				List<String> values = new ArrayList<String>();
				// set forceEncodingFilter in the web.xml, therefore need decode every value.
				for (String value : params.get(key)) {
					try {
						values.add(new String(value.getBytes("8859_1"), "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				map.put(param, values);
			} else{
				logger.debug(" ========== Unexpected Parameter :{} has been removed. ========== ", key);
				return new ResponseEntity<ErrorResponse>(new ErrorResponse(7, "Unexpected parameter: " + key, "Expected Parameters: " + variables.toString(), ""), HttpStatus.BAD_REQUEST);
			}
		}
		
		List<Site> siteList = null;
		try {
			siteList = this.siteService.listSearch(map);
			logger.debug(" ========== Search Result: {} . ========== ", siteList.size());
			if (siteList.isEmpty() || siteList == null) {
				return new ResponseEntity<ErrorResponse>(new ErrorResponse(8, "No matching result found!", "", ""),
						HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<List<Site>>(siteList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<ErrorResponse>(
					new ErrorResponse(5, e.getLocalizedMessage(), Arrays.toString(e.getStackTrace()), ""),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}

	// ========== Create a Site Instance Resource ==========
	@RequestMapping(value = "/sites/new", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<? extends Object> create(@RequestBody Site site, @RequestHeader Map<String, String> headers) {

		if (!headers.containsKey("authorization")) {
			return new ResponseEntity<ErrorResponse>(new ErrorResponse(1, "must be authenticated..", "", ""),
					HttpStatus.NON_AUTHORITATIVE_INFORMATION);
		}

		UserCredential cred = AuthUtil.toUserCredential(headers.get("authorization"));
		logger.debug(" ========== UserCredential: {} ========== ", cred.toString());

		Account account = null;
		try {
			account = this.authService.authenticate(cred.getUserId(), cred.getPassword());
		} catch (ResourceException e) {
			e.printStackTrace();
			return new ResponseEntity<ErrorResponse>(
					new ErrorResponse(2, e.getMessage(), e.getDeveloperMessage(), e.getMoreInfo()),
					HttpStatus.NON_AUTHORITATIVE_INFORMATION);
		}
		try {
			if (!this.authService.hasPermission(account, "ADMIN")) {
				return new ResponseEntity<ErrorResponse>(new ErrorResponse(5, "Permission Denied!", "", ""),
						HttpStatus.BAD_REQUEST);
			}
		} catch (ResourceException ex) {
			ex.printStackTrace();
			return new ResponseEntity<ErrorResponse>(new ErrorResponse(6, "ERROR WHILE CHECKING PERMISSION!",
					ex.getDeveloperMessage(), ex.getMoreInfo()), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		logger.debug(" ========== Creating Site Instance Resource ========== ");
		if (site.getSiteName() == null || site.getLatitude() == null || site.getLongitude() == null) {
			return new ResponseEntity<ErrorResponse>(new ErrorResponse(4, "Required fields are missing !", "", ""),
					HttpStatus.BAD_REQUEST);
		}

		try {
			if (siteService.isInstanceExist(site.getSiteName())) {
				return new ResponseEntity<ErrorResponse>(
						new ErrorResponse(4, "Resource already exist!", site.getSiteName(), ""), HttpStatus.CONFLICT);
			}

			siteService.newInstance(site);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<ErrorResponse>(
					new ErrorResponse(5, e.getLocalizedMessage(), Arrays.toString(e.getStackTrace()), ""),
					HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return new ResponseEntity<Site>(site, HttpStatus.CREATED);

	}

	// -------------------- Read a Site Instance Resource --------------------
	@RequestMapping(value = "/sites/{id}", method = RequestMethod.GET)
	public ResponseEntity<? extends Object> read(@PathVariable("id") int siteID,
			@RequestHeader Map<String, String> headers) {
		// User Validation
		logger.debug(" ========== Checking authorization ========== ");
		if (!headers.containsKey("authorization")) {
			return new ResponseEntity<ErrorResponse>(new ErrorResponse(1, "must be authenticated..", "", ""),
					HttpStatus.NON_AUTHORITATIVE_INFORMATION);
		}

		Account account = null;
		UserCredential cred = AuthUtil.toUserCredential(headers.get("authorization"));
		logger.debug(" ========== UserCredential: {} ========== ", cred.toString());

		try {
			account = authService.authenticate(cred.getUserId(), cred.getPassword());
		} catch (ResourceException e) {
			e.printStackTrace();
			return new ResponseEntity<ErrorResponse>(
					new ErrorResponse(2, e.getMessage(), e.getDeveloperMessage(), e.getMoreInfo()),
					HttpStatus.NON_AUTHORITATIVE_INFORMATION);
		}
		logger.debug(" ========== Validated User: {}  ========== ", account.getGivenName());

		// Read Resource
		logger.debug(" ========== Reading Method Instance Resource of ID: {}  ========== ", siteID);
		try {
			Site site = this.siteService.readInstance(siteID);
			if (site == null) {
				logger.debug(" ========== Site Instance Resource of ID: {}, not found. ========== ", siteID);
				return new ResponseEntity<ErrorResponse>(new ErrorResponse(3, "Resource not found!", "", ""),
						HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<Site>(site, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<ErrorResponse>(
					new ErrorResponse(4, e.getLocalizedMessage(), Arrays.toString(e.getStackTrace()), ""),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// -------------------- Update a Site Instance Resource ------------------
	@RequestMapping(value = "/sites/{id}", method = RequestMethod.PUT)
	public ResponseEntity<? extends Object> update(@RequestBody Site site, @PathVariable("id") int siteID,
			@RequestHeader Map<String, String> headers) {
		// User Validation
		logger.debug(" ========== Checking authorization ========== ");
		if (!headers.containsKey("authorization")) {
			return new ResponseEntity<ErrorResponse>(new ErrorResponse(1, "must be authenticated..", "", ""),
					HttpStatus.NON_AUTHORITATIVE_INFORMATION);
		}

		Account account = null;
		UserCredential cred = AuthUtil.toUserCredential(headers.get("authorization"));
		logger.debug(" ========== UserCredential: {} ========== ", cred.toString());

		try {
			account = authService.authenticate(cred.getUserId(), cred.getPassword());
		} catch (ResourceException e) {
			e.printStackTrace();
			return new ResponseEntity<ErrorResponse>(
					new ErrorResponse(2, e.getMessage(), e.getDeveloperMessage(), e.getMoreInfo()),
					HttpStatus.NON_AUTHORITATIVE_INFORMATION);
		}
		logger.debug(" ========== Validated User: {}  ========== ", account.getGivenName());

		// Update Resource
		logger.debug(" ========== Updating Site Instance Resource: {} ========== ", site.getSiteName());

		try {
			Site oldSite = this.siteService.readInstance(siteID);
			if (oldSite == null) {
				logger.debug(" ========== Site Instance Resource of ID: {}, not found. ========== ", siteID);
				return new ResponseEntity<ErrorResponse>(new ErrorResponse(3, "Resource doesn't exist!", "", ""),
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<ErrorResponse>(
					new ErrorResponse(4, e.getLocalizedMessage(), Arrays.toString(e.getStackTrace()), ""),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (siteID == site.getSiteID()) {
			try {
				this.siteService.updateInstance(site);
				return new ResponseEntity<Site>(site, HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<ErrorResponse>(
						new ErrorResponse(6, e.getLocalizedMessage(), Arrays.toString(e.getStackTrace()), ""),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}else {
			logger.debug(" ========== Site Instance Resource of ID: {} , {} doesn't match. ========== ", siteID, site.getSiteID());
			return new ResponseEntity<ErrorResponse>(new ErrorResponse(5, "Resource doesn't match!", "", ""),
					HttpStatus.BAD_REQUEST);
		}
	}

	// -------------------- Delete a Site Instance Resource ------------------
	@RequestMapping(value = "/sites/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<? extends Object> delete(@PathVariable("id") int siteID,
			@RequestHeader Map<String, String> headers) {
		// User Validation
		logger.debug(" ========== Checking authorization ========== ");
		if (!headers.containsKey("authorization")) {
			return new ResponseEntity<ErrorResponse>(new ErrorResponse(1, "must be authenticated..", "", ""),
					HttpStatus.NON_AUTHORITATIVE_INFORMATION);
		}

		Account account = null;
		UserCredential cred = AuthUtil.toUserCredential(headers.get("authorization"));
		logger.debug(" ========== UserCredential: {} ========== ", cred.toString());

		try {
			account = authService.authenticate(cred.getUserId(), cred.getPassword());
		} catch (ResourceException e) {
			e.printStackTrace();
			return new ResponseEntity<ErrorResponse>(
					new ErrorResponse(2, e.getMessage(), e.getDeveloperMessage(), e.getMoreInfo()),
					HttpStatus.NON_AUTHORITATIVE_INFORMATION);
		}
		logger.debug(" ========== Validated User: {}  ========== ", account.getGivenName());

		// delete Resource
		logger.debug(" ========== Reading & Deleting Site Instance Resource(ID: {}) ========== ", siteID);
		Site site = null;
		try {
			site = this.siteService.readInstance(siteID);
			if (site == null) {
				logger.debug(" ========== Site Instance Resource of ID: {}, not found. ========== ", siteID);
				return new ResponseEntity<ErrorResponse>(new ErrorResponse(3, "Resource doesn't exist!", "", ""),
						HttpStatus.NOT_FOUND);
			}
		
			this.siteService.deleteInstance(siteID);
			return new ResponseEntity<Site>(site, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<ErrorResponse>(
					new ErrorResponse(5, e.getLocalizedMessage(), Arrays.toString(e.getStackTrace()), ""),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// -------------------- Search for Site Resource --------------------
	@RequestMapping(value = "/sites", method = RequestMethod.POST)
	public ResponseEntity<? extends Object> search(@RequestBody Map<String, List<String>> reqMap,
			@RequestHeader Map<String, String> headers) {
		// User Validation
		logger.debug(" ========== Checking authorization ========== ");
		if (!headers.containsKey("authorization")) {
			return new ResponseEntity<ErrorResponse>(new ErrorResponse(1, "must be authenticated..", "", ""),
					HttpStatus.NON_AUTHORITATIVE_INFORMATION);
		}

		Account account = null;
		UserCredential cred = AuthUtil.toUserCredential(headers.get("authorization"));
		logger.debug(" ========== UserCredential: {} ========== ", cred.toString());

		try {
			account = authService.authenticate(cred.getUserId(), cred.getPassword());
		} catch (ResourceException e) {
			e.printStackTrace();
			return new ResponseEntity<ErrorResponse>(
					new ErrorResponse(2, e.getMessage(), e.getDeveloperMessage(), e.getMoreInfo()),
					HttpStatus.NON_AUTHORITATIVE_INFORMATION);
		}
		logger.debug(" ========== Validated User: {}  ========== ", account.getGivenName());

		// Search Resource
		logger.debug(" ========== Searching Site Collection Resource ========== ");

		PropertyDescriptor[] params = BeanUtils.getPropertyDescriptors(Site.class);
		List<String> variables = new ArrayList<String>();
		for (PropertyDescriptor desc : params) {
			variables.add(desc.getName());
		}

		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for (String key : reqMap.keySet()) {
			if (variables.contains(key)) {
				String param = key.substring(0, 1).toUpperCase();
				param = param + key.substring(1);
				map.put(param, reqMap.get(key));
			} else {
				logger.debug(" ========== Unexpected Parameter :{} has been removed. ========== ", key);
				return new ResponseEntity<ErrorResponse>(
						new ErrorResponse(3, "Unexpected Parameters included!", "", ""), HttpStatus.BAD_REQUEST);
			}
		}
		List<Site> siteList = null;
		try {
			siteList = this.siteService.listSearch(map);
			if (siteList.isEmpty() || siteList == null) {
				return new ResponseEntity<ErrorResponse>(new ErrorResponse(4, "No matching result found!", "", ""),
						HttpStatus.NOT_FOUND);
			}else
				return new ResponseEntity<List<Site>>(siteList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<ErrorResponse>(
					new ErrorResponse(5, e.getLocalizedMessage(), Arrays.toString(e.getStackTrace()), ""),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

}