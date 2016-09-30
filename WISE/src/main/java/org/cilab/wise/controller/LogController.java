package org.cilab.wise.controller;

import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cilab.s4rm.model.Log;
import org.cilab.s4rm.service.LogService;
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

@Api(value = "logs")
@RestController
public class LogController {

	/**
	 * Class Name: PersistenceLogController.java 
	 * Description: CRUD, Service
	 * 
	 * @author Meilan Jiang
	 * @since 2016.06.16
	 * @version 1.2
	 * 
	 *          Copyright(c) 2016 by CILAB All right reserved.
	 */
	private static final Logger logger = LoggerFactory.getLogger(LogController.class);

	@Autowired
	LogService logService;

	// -------------------- Read and Search PersistenceLog Collection Resource
	// --------------------
	@RequestMapping(value = "/logs", method = RequestMethod.GET)
	public ResponseEntity<List<Log>> list(@RequestParam(required = false) MultiValueMap<String, String> params) {
		// read PersistenceLog collection resource when there is no parameter.
		if (params.isEmpty()) {
			logger.info("Reading PersistenceLog Collection Resource ...");
			List<Log> logs = logService.readCollection();
			if (logs.isEmpty()) {
				logger.info("No PersistenceLogs found.");
				return new ResponseEntity<List<Log>>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<List<Log>>(logs, HttpStatus.OK);
		}
		// search PersistenceLog collection resource with parameters.
		else {
			logger.info("Searching PersistenceLog Resource ...");

			PropertyDescriptor[] props = BeanUtils.getPropertyDescriptors(Log.class);
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
				return new ResponseEntity<List<Log>>(HttpStatus.BAD_REQUEST);
			} else {
				List<Log> logs = this.logService.listSearch(map);
				if (logs.isEmpty() || logs == null)
					return new ResponseEntity<List<Log>>(HttpStatus.NOT_FOUND);
				else
					return new ResponseEntity<List<Log>>(logs, HttpStatus.OK);
			}
		}
	}

	// -------------------- Create a PersistenceLog Instance Resource ------------------
	@RequestMapping(value = "/logs/new", method = RequestMethod.POST)
	public ResponseEntity<Boolean> create(@RequestBody Log log) {
		logger.info("Creating PersistenceLog Instance Resource of Stream: {} ..." + log.getStreamID());
		// check if log contains the Not Null field in the database.
		if (log.getStreamID() == null)
			return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);

		if (logService.isInstanceExist(log.getStartedAt(), log.getStreamID())) {
			logger.info("A PersistenceLog with Stream: {} already exist.", log.getStreamID());
			return new ResponseEntity<Boolean>(HttpStatus.CONFLICT);
		}

		boolean createRes = logService.newInstance(log);
		return new ResponseEntity<Boolean>(createRes, HttpStatus.CREATED);
	}

	// -------------------- Read a PersistenceLog Instance Resource --------------------
	@RequestMapping(value = "/logs/{id}", method = RequestMethod.GET)
	public ResponseEntity<Log> read(@PathVariable("id") int logID) {
		logger.info("/logs/:id GET ===== Reading PersistenceLog Instance Resource of ID: {} ...", logID);
		Log log = this.logService.readInstance(logID);
		if (log == null) {
			logger.info("PersistenceLog Instance Resource of ID: {}, not found.", logID);
			return new ResponseEntity<Log>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Log>(log, HttpStatus.OK);
	}

	// -------------------- Update a PersistenceLog Instance Resource ------------------
	@RequestMapping(value = "/logs/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Boolean> update(@RequestBody Log log, @PathVariable("id") int logID) {
		logger.info("Updating PersistenceLog Instance Resource of ID: {} ...", log.getLogID());

		if (logID != log.getLogID()) {
			logger.info("PersistenceLog Instance Resource of ID: {} doesn't match the Log: {}.", logID, log.getLogID());
			return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
		} else {
			Log oldPersistenceLog = this.logService.readInstance(logID);
			if (oldPersistenceLog == null) {
				logger.info("PersistenceLog Instance Resource of ID: {}, not found.", logID);
				return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
			}
		}
		// set the null of PersistenceLog with oldPersistenceLog

		Boolean res = this.logService.updateInstance(log);
		if (res)
			return new ResponseEntity<Boolean>(res, HttpStatus.OK);
		else
			return new ResponseEntity<Boolean>(res, HttpStatus.CONFLICT);
	}

	// -------------------- Delete a PersistenceLog Instance Resource ------------------
	@RequestMapping(value = "/logs/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> delete(@PathVariable("id") int logID) {
		logger.info("Reading & Deleting PersistenceLog Instance Resource of ID: {} ...", logID);
		Log log = this.logService.readInstance(logID);
		if (log == null) {
			logger.info("Unable to delete. PersistenceLog Instance Resource of ID: {}, not found.", logID);
			return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
		}
		Boolean res = this.logService.deleteInstance(logID);
		if (res)
			return new ResponseEntity<Boolean>(res, HttpStatus.OK);
		else
			return new ResponseEntity<Boolean>(res, HttpStatus.CONFLICT); // when PersistenceLog has existing related Sites
	}

	// -------------------- Search for PersistenceLog Resource --------------------
	@RequestMapping(value = "/logs", method = RequestMethod.POST)
	public ResponseEntity<List<Log>> search(@RequestBody Map<String, List<String>> reqMap) {
		logger.info("Searching PersistenceLog Resource ...");

		// remove the parameters which doesn't match with column in the list
		PropertyDescriptor[] params = BeanUtils.getPropertyDescriptors(Log.class);
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
		List<Log> logs = this.logService.listSearch(map);
		if (logs.isEmpty() || logs == null)
			return new ResponseEntity<List<Log>>(HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<List<Log>>(logs, HttpStatus.OK);
	}

}
