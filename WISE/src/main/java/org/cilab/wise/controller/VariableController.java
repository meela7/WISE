package org.cilab.wise.controller;

import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cilab.m4.model.Variable;
import org.cilab.m4.service.VariableService;
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

@Api(value = "variables")
@RestController
public class VariableController {

	/**
	 * Class Name: VariableController.java 
	 * Description: CRUD, Service
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.16
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	private static final Logger logger = LoggerFactory.getLogger(VariableController.class);

	@Autowired
	private VariableService variableService;
	
	// -------------------- Read and Search Variable Collection Resource --------------------
	@RequestMapping(value = "/variables", method = RequestMethod.GET)
	public ResponseEntity<List<Variable>> list(@RequestParam(required = false) MultiValueMap<String, String> params) {
		// read Variable collection resource when there is no parameter.
		if (params.isEmpty()) {
			logger.info("Reading Variable Collection Resource ...");
			List<Variable> variableList = variableService.readCollection();
			if (variableList.isEmpty()) {
				logger.info("No Variables found.");
				return new ResponseEntity<List<Variable>>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<List<Variable>>(variableList, HttpStatus.OK);
		}
		// search Variable collection resource with parameters.
		else {
			logger.info("Searching Variable Resource ...");

			PropertyDescriptor[] props = BeanUtils.getPropertyDescriptors(Variable.class);
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
				return new ResponseEntity<List<Variable>>(HttpStatus.BAD_REQUEST);
			}else{
				List<Variable> variableList = this.variableService.listSearch(map);
				if (variableList.isEmpty() || variableList == null)
					return new ResponseEntity<List<Variable>>(variableList, HttpStatus.NOT_FOUND);
				else
					return new ResponseEntity<List<Variable>>(variableList, HttpStatus.OK);
			}
		}
	}

	// -------------------- Create a Variable Instance Resource ------------------
	@RequestMapping(value = "/variables/new", method = RequestMethod.POST)
	public ResponseEntity<Boolean> create(@RequestBody Variable variable){
				
//		Variable variable = new Variable();
//		variable.setVariableName(map.get("variableName"));
//		Unit unit  = new Unit();
//		if(map.containsKey("unitID")){
//			int unitID = Integer.parseInt(map.get("unitID"));
//			unit  = this.unitService.readInstance(unitID);
//			variable.setUnit(unit);
//		}else if(map.containsKey("unitName")){
//			unit.setUnitName(map.get("unitName"));
//			unit.setUnitNameLong(map.get("unitNameLong"));
//			this.unitService.newInstance(unit);
//			variable.setUnit(this.unitService.getInstanceByUniqueKey((map.get("unitName"))));
//		}
//		
//		variable.setValueType(map.get("valueType"));
//		variable.setDescription(map.get("description"));
		if(this.variableService.isInstanceExist(variable.getVariableName(), variable.getUnit().getUnitID())){
			return new ResponseEntity<Boolean>(false, HttpStatus.CONFLICT);
		}else{
			boolean createdID = variableService.newInstance(variable);		
			
			return new ResponseEntity<Boolean>(createdID, HttpStatus.CREATED);
		}
			
	}

	// -------------------- Read a Variable Instance Resource --------------------
	@RequestMapping(value = "/variables/{id}", method = RequestMethod.GET)
	public ResponseEntity<Variable> read(@PathVariable("id") int variableID) {
		logger.info("Reading Variable Instance Resource of ID: {} ...", variableID);
		Variable variable = this.variableService.readInstance(variableID);
		if (variable == null) {
			logger.info("Variable Instance Resource of ID: {}, not found.", variableID);
			return new ResponseEntity<Variable>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Variable>(variable, HttpStatus.OK);
	}

	// -------------------- Update a Variable Instance Resource ------------------
	@RequestMapping(value = "/variables/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Boolean> update(@RequestBody Variable variable, @PathVariable("id") int variableID) {
		logger.info("Updating Variable Instance Resource of ID: {} ...", variable.getVariableID());

		if (variableID != variable.getVariableID()) {
			logger.info("Variable Instance Resource of ID: {} , {} doesn't match.", variableID, variable.getVariableID());
			return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
		} else {
			Variable oldVariable = this.variableService.readInstance(variableID);
			if (oldVariable == null) {
				logger.info("Variable Instance Resource of ID: {}, not found.", variableID);
				return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
			}
		}
		// set the null of Variable with oldVariable

		Boolean res = this.variableService.updateInstance(variable);
		if (res)
			return new ResponseEntity<Boolean>(res, HttpStatus.OK);
		else
			return new ResponseEntity<Boolean>(res, HttpStatus.CONFLICT);
	}

	// -------------------- Delete a Variable Instance Resource ------------------
	@RequestMapping(value = "/variables/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> delete(@PathVariable("id") int variableID) {
		logger.info("Reading & Deleting Variable Instance Resource of ID: {} ...", variableID);
		Variable variable = this.variableService.readInstance(variableID);
		if (variable == null) {
			logger.info("Unable to delete. Variable Instance Resource of ID: {}, not found.", variableID);
			return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
		}
		Boolean res = this.variableService.deleteInstance(variableID);
		if (res)
			return new ResponseEntity<Boolean>(res, HttpStatus.OK);
		else
			return new ResponseEntity<Boolean>(res, HttpStatus.CONFLICT); 
	}

	// -------------------- Search for Variable Resource --------------------
	@RequestMapping(value = "/variables", method = RequestMethod.POST)
	public ResponseEntity<List<Variable>> search(@RequestBody Map<String, List<String>> reqMap) {
		logger.info("Searching Variable Resource ...");

		PropertyDescriptor[] params = BeanUtils.getPropertyDescriptors(Variable.class);
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
		List<Variable> variableList = this.variableService.listSearch(map);
		if (variableList.isEmpty() || variableList == null)
			return new ResponseEntity<List<Variable>>(variableList, HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<List<Variable>>(variableList, HttpStatus.OK);
	}

}
