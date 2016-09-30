package org.cilab.wise.controller;

import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cilab.s4rm.model.User;
import org.cilab.s4rm.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@Api(value = "users")
@RestController
public class UserController {

	/**
	 * Class Name: UserController.java 
	 * Description: CRUD, Service
	 * 
	 * @author Meilan Jiang
	 * @since 2016.06.16
	 * @version 1.2
	 * 
	 *          Copyright(c) 2016 by CILAB All right reserved.
	 */
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	// -------------------- Read and Search User Collection Resource
	// --------------------
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public ResponseEntity<List<User>> list(@RequestParam(required = false) MultiValueMap<String, String> params) {
		// read User collection resource when there is no parameter.
		if (params.isEmpty()) {
			logger.info("Reading User Collection Resource ...");
			List<User> users = userService.readCollection();
			if (users.isEmpty()) {
				logger.info("No Users found.");
				return new ResponseEntity<List<User>>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<List<User>>(users, HttpStatus.OK);
		}
		// search User collection resource with parameters.
		else {
			logger.info("Searching User Resource ...");

			PropertyDescriptor[] props = BeanUtils.getPropertyDescriptors(User.class);
			List<String> variables = new ArrayList<String>();
			for (PropertyDescriptor desc : props) {
				variables.add(desc.getName());
			}
			Map<String, List<String>> map = new HashMap<String, List<String>>();
			for (String key : params.keySet()) {
				if (variables.contains(key)) {
					// uppercase first letter of property name
					String param = key.substring(0, 1).toUpperCase();
					param = param + key.substring(1);

					List<String> values = new ArrayList<String>();
					for(String value: params.get(key)){
						// decode parameters
						try {
							values.add(new String(value.getBytes("8859_1"), "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
					map.put(param, values);
				} else
					logger.info("Unexpected Parameter :{} has been removed.", key);
			}
			if (map.keySet().size() == 0) {
				return new ResponseEntity<List<User>>(HttpStatus.BAD_REQUEST);
			} else {
				List<User> users = this.userService.listSearch(map);
				if (users.isEmpty() || users == null)
					return new ResponseEntity<List<User>>(HttpStatus.NOT_FOUND);
				else
					return new ResponseEntity<List<User>>(users, HttpStatus.OK);
			}
		}
	}

	// -------------------- Create a User Instance Resource ------------------
	@RequestMapping(value = "/users/new", method = RequestMethod.POST)
	public ResponseEntity<Boolean> create(@RequestBody User user) {
		logger.info("Creating User Instance Resource of Name: {} ..." + user.getGivenName());
		// check if user contains the Not Null field in the database.
		if (user.getUserName() == null)
			return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);

		if (userService.isInstanceExist(user.getSurName(), user.getGivenName())) {
			logger.info("A User with name {} already exist.", user.getGivenName());
			return new ResponseEntity<Boolean>(HttpStatus.CONFLICT);
		}

		boolean createRes = userService.newInstance(user);
		return new ResponseEntity<Boolean>(createRes, HttpStatus.CREATED);
	}

	// -------------------- Read a User Instance Resource --------------------
	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
	public ResponseEntity<User> read(@PathVariable("id") String userID) {
		logger.info("Reading User Instance Resource of ID: {} ...", userID);
		User user = this.userService.readInstance(userID);
		if (user == null) {
			logger.info("User Instance Resource of ID: {}, not found.", userID);
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	// -------------------- Update a User Instance Resource ------------------
	@RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Boolean> update(@RequestBody User user, @PathVariable("id") String userID) {
		logger.info("Updating User Instance Resource of ID: {} ...", user.getUserID());

		if (userID.equals(user.getUserID())) {
			logger.info("User Instance Resource of ID: {} , {} doesn't match.", userID, user.getUserID());
			return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
		} else {
			User oldUser = this.userService.readInstance(userID);
			if (oldUser == null) {
				logger.info("User Instance Resource of ID: {}, not found.", userID);
				return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
			}
		}
		// set the null of User with oldUser

		Boolean res = this.userService.updateInstance(user);
		if (res)
			return new ResponseEntity<Boolean>(res, HttpStatus.OK);
		else
			return new ResponseEntity<Boolean>(res, HttpStatus.CONFLICT);
	}

	// -------------------- Delete a User Instance Resource ------------------
	@RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> delete(@PathVariable("id") String userID) {
		logger.info("Reading & Deleting User Instance Resource of ID: {} ...", userID);
		User user = this.userService.readInstance(userID);
		if (user == null) {
			logger.info("Unable to delete. User Instance Resource of ID: {}, not found.", userID);
			return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
		}
		Boolean res = this.userService.deleteInstance(userID);
		if (res)
			return new ResponseEntity<Boolean>(res, HttpStatus.OK);
		else
			return new ResponseEntity<Boolean>(res, HttpStatus.CONFLICT); // when User has existing related Sites
	}

	// -------------------- Search for User Resource --------------------
	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public ResponseEntity<List<User>> search(@RequestBody Map<String, List<String>> reqMap) {
		logger.info("Searching User Resource ...");

		// remove the parameters which doesn't match with column in the list
		PropertyDescriptor[] params = BeanUtils.getPropertyDescriptors(User.class);
		List<String> variables = new ArrayList<String>();
		for (PropertyDescriptor desc : params) {
			variables.add(desc.getName());
		}
		
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for (String key : reqMap.keySet()) {
			if(variables.contains(key)){
				String param = key.substring(0,1).toUpperCase();
				param = param + key.substring(1);
				map.put(param, reqMap.get(key));
			}
			else
				logger.info("Unexpected Parameter :{} has been removed.", key);
		}
		List<User> users = this.userService.listSearch(map);
		if (users.isEmpty() || users == null)
			return new ResponseEntity<List<User>>(HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}

}
