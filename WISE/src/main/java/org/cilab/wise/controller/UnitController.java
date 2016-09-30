package org.cilab.wise.controller;

import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cilab.m4.model.Unit;
import org.cilab.m4.service.UnitService;
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

@Api(value = "units")
@RestController
public class UnitController {

	/**
	 * Class Name: UnitController.java 
	 * Description: CRUD, Service
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.16
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	private static final Logger logger = LoggerFactory.getLogger(UnitController.class);

	@Autowired
	private UnitService unitService;
	
	// -------------------- Read and Search Unit Collection Resource --------------------
	@RequestMapping(value = "/units", method = RequestMethod.GET)
	public ResponseEntity<List<Unit>> list(@RequestParam(required = false) MultiValueMap<String, String> params) {
		// read Unit collection resource when there is no parameter.
		if (params.isEmpty()) {
			logger.info("Reading Unit Collection Resource ...");
			List<Unit> unitList = unitService.readCollection();
			if (unitList.isEmpty()) {
				logger.info("No Units found.");
				return new ResponseEntity<List<Unit>>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<List<Unit>>(unitList, HttpStatus.OK);
		}
		// search Unit collection resource with parameters.
		else {
			logger.info("Searching Unit Resource ...");

			PropertyDescriptor[] props = BeanUtils.getPropertyDescriptors(Unit.class);
			List<String> variables = new ArrayList<String>();
			for (PropertyDescriptor desc : props) {
				variables.add(desc.getName());
			}
			
			Map<String, List<String>> map = new HashMap<String, List<String>>();
			for (String key : params.keySet()) {
				if(variables.contains(key)){
					// uppercase first letter of property name
					String param = key.substring(0,1).toUpperCase();
					param = param + key.substring(1);				
				
					List<String> values = new ArrayList<String>();
					// set forceEncodingFilter in the web.xml, therefore need decode every value.
					for(String value: params.get(key)){
						try {
							values.add(new String(value.getBytes("8859_1"), "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
					map.put(param, values);			
				}
				else
					logger.info("Unexpected Parameter :{} has been removed.", key);				
			}
			if(map.keySet().size() == 0){
				return new ResponseEntity<List<Unit>>(HttpStatus.BAD_REQUEST);
			}else{
				List<Unit> unitList = this.unitService.listSearch(map);
				if (unitList.isEmpty() || unitList == null)
					return new ResponseEntity<List<Unit>>(unitList, HttpStatus.NOT_FOUND);
				else
					return new ResponseEntity<List<Unit>>(unitList, HttpStatus.OK);
			}
		}
	}

	// -------------------- Create a Unit Instance Resource ------------------
	@RequestMapping(value = "/units/new", method = RequestMethod.POST)
	public ResponseEntity<Boolean> create(@RequestBody Map<String, String> map){
		 
		Unit unit = new Unit();
		unit.setUnitName(map.get("unitName"));
		unit.setUnitNameLong(map.get("unitNameLong"));
		
		boolean createdID = unitService.newInstance(unit);
		
		
		return new ResponseEntity<Boolean>(createdID, HttpStatus.CREATED);
	}

	// -------------------- Read a Unit Instance Resource --------------------
	@RequestMapping(value = "/units/{id}", method = RequestMethod.GET)
	public ResponseEntity<Unit> read(@PathVariable("id") int unitID) {
		logger.info("Reading Unit Instance Resource of ID: {} ...", unitID);
		Unit unit = this.unitService.readInstance(unitID);
		if (unit == null) {
			logger.info("Unit Instance Resource of ID: {}, not found.", unitID);
			return new ResponseEntity<Unit>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Unit>(unit, HttpStatus.OK);
	}

	// -------------------- Update a Unit Instance Resource ------------------
	@RequestMapping(value = "/units/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Boolean> update(@RequestBody Unit unit, @PathVariable("id") int unitID) {
		logger.info("Updating Unit Instance Resource of ID: {} ...", unit.getUnitID());

		if (unitID != unit.getUnitID()) {
			logger.info("Unit Instance Resource of ID: {} , {} doesn't match.", unitID, unit.getUnitID());
			return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
		} else {
			Unit oldUnit = this.unitService.readInstance(unitID);
			if (oldUnit == null) {
				logger.info("Unit Instance Resource of ID: {}, not found.", unitID);
				return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
			}
		}
		// set the null of Unit with oldUnit

		Boolean res = this.unitService.updateInstance(unit);
		if (res)
			return new ResponseEntity<Boolean>(res, HttpStatus.OK);
		else
			return new ResponseEntity<Boolean>(res, HttpStatus.CONFLICT);
	}

	// -------------------- Delete a Unit Instance Resource ------------------
	@RequestMapping(value = "/units/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> delete(@PathVariable("id") int unitID) {
		logger.info("Reading & Deleting Unit Instance Resource of ID: {} ...", unitID);
		Unit unit = this.unitService.readInstance(unitID);
		if (unit == null) {
			logger.info("Unable to delete. Unit Instance Resource of ID: {}, not found.", unitID);
			return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
		}
		Boolean res = this.unitService.deleteInstance(unitID);
		if (res)
			return new ResponseEntity<Boolean>(res, HttpStatus.OK);
		else
			return new ResponseEntity<Boolean>(res, HttpStatus.CONFLICT); 
	}

	// -------------------- Search for Unit Resource --------------------
	@RequestMapping(value = "/units", method = RequestMethod.POST)
	public ResponseEntity<List<Unit>> search(@RequestBody Map<String, List<String>> reqMap) {
		logger.info("Searching Unit Resource ...");

		PropertyDescriptor[] params = BeanUtils.getPropertyDescriptors(Unit.class);
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
		List<Unit> unitList = this.unitService.listSearch(map);
		if (unitList.isEmpty() || unitList == null)
			return new ResponseEntity<List<Unit>>(unitList, HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<List<Unit>>(unitList, HttpStatus.OK);
	}

}
