package org.cilab.wise.controller;

import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cilab.m4.model.Method;
import org.cilab.m4.service.MethodService;
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

@Api(value = "methods")
@RestController
public class MethodController {

	/**
	 * Class Name: MethodController.java 
	 * Description: CRUD, Service
	 * 
	 * @author Meilan Jiang
	 * @since 2016.02.04
	 * @version 1.0
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	private static final Logger logger = LoggerFactory.getLogger(MethodController.class);

	@Autowired
	private MethodService methodService;
//	@Autowired
//	private InstrumentService instrumentService;
//	@Autowired
//	private PredictionService predictionService;


	// -------------------- Read and Search Method Collection Resource --------------------
	@RequestMapping(value = "/methods", method = RequestMethod.GET)
	public ResponseEntity<List<Method>> list(@RequestParam(required = false) MultiValueMap<String, String> params) {
		// read Method collection resource when there is no parameter.
		if (params.isEmpty()) {
			logger.info("Reading Method Collection Resource ...");
			List<Method> methods = methodService.readCollection();
			if (methods.isEmpty()) {
				logger.info("No Methods found.");
				return new ResponseEntity<List<Method>>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<List<Method>>(methods, HttpStatus.OK);
		}
		// search Method collection resource with parameters.
		else {
			logger.info("Searching Method Resource ...");

			PropertyDescriptor[] props = BeanUtils.getPropertyDescriptors(Method.class);
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
				return new ResponseEntity<List<Method>>(HttpStatus.BAD_REQUEST);
			}else{
				List<Method> methods = this.methodService.listSearch(map);
				if (methods.isEmpty() || methods == null)
					return new ResponseEntity<List<Method>>(methods, HttpStatus.NOT_FOUND);
				else
					return new ResponseEntity<List<Method>>(methods, HttpStatus.OK);
			}
		}
	}

	// -------------------- Create a Method Instance Resource ------------------
	@RequestMapping(value = "/methods/new", method = RequestMethod.POST)
	public ResponseEntity<Boolean> create(@RequestBody Method method){
		boolean createdID = this.methodService.newInstance(method);
//		if(map.containsKey("methodType")){
//			if(map.get("methodType").equals("instrument")){
//				Instrument inst = new Instrument();
//				inst.setMethodName(map.get("methodName"));
//				inst.setMethodType(map.get("methodType"));
//				inst.setProductModel(map.get("productModel"));
//				inst.setManufacturer(map.get("manufacturer"));
//				inst.setMeasureType(map.get("measureType"));
//				inst.setDescription(map.get("description"));
//				createdID = instrumentService.newInstance(inst);
//			}else if(map.get("methodType").equals("prediction")){
//				Prediction pred = new Prediction();
//				pred.setMethodName(map.get("methodName"));
//				pred.setMethodType(map.get("methodType"));
//				pred.setModelingMethod(map.get("modelingMethod"));
//				pred.setExecuteEnvironment(map.get("excuteEnvironment"));
//				pred.setCreator(map.get("creator"));
//				pred.setCreatedDate(map.get("createdDate"));
//				pred.setDescription(map.get("description"));
//				createdID = predictionService.newInstance(pred);
//			}
//		}		
		return new ResponseEntity<Boolean>(createdID, HttpStatus.CREATED);
	}

	// -------------------- Read a Method Instance Resource --------------------
	@RequestMapping(value = "/methods/{id}", method = RequestMethod.GET)
	public ResponseEntity<Method> read(@PathVariable("id") int methodID) {
		logger.info("Reading Method Instance Resource of ID: {} ...", methodID);
		Method method = this.methodService.readInstance(methodID);
		if (method == null) {
			logger.info("Method Instance Resource of ID: {}, not found.", methodID);
			return new ResponseEntity<Method>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Method>(method, HttpStatus.OK);
	}

	// -------------------- Update a Method Instance Resource ------------------
	@RequestMapping(value = "/methods/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Boolean> update(@RequestBody Method method, @PathVariable("id") int methodID) {
		logger.info("Updating Method Instance Resource of ID: {} ...", method.getMethodID());

		if (methodID != method.getMethodID()) {
			logger.info("Method Instance Resource of ID: {} , {} doesn't match.", methodID, method.getMethodID());
			return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
		} else {
			Method oldMethod = this.methodService.readInstance(methodID);
			if (oldMethod == null) {
				logger.info("Method Instance Resource of ID: {}, not found.", methodID);
				return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
			}
		}
		// set the null of Method with oldMethod

		Boolean res = this.methodService.updateInstance(method);
		if (res)
			return new ResponseEntity<Boolean>(res, HttpStatus.OK);
		else
			return new ResponseEntity<Boolean>(res, HttpStatus.CONFLICT);
	}

	// -------------------- Delete a Method Instance Resource ------------------
	@RequestMapping(value = "/methods/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> delete(@PathVariable("id") int methodID) {
		logger.info("Reading & Deleting Method Instance Resource of ID: {} ...", methodID);
		Method method = this.methodService.readInstance(methodID);
		if (method == null) {
			logger.info("Unable to delete. Method Instance Resource of ID: {}, not found.", methodID);
			return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
		}
		Boolean res = this.methodService.deleteInstance(methodID);
		if (res)
			return new ResponseEntity<Boolean>(res, HttpStatus.OK);
		else
			return new ResponseEntity<Boolean>(res, HttpStatus.CONFLICT); 
	}

	// -------------------- Search for Method Resource --------------------
	@RequestMapping(value = "/methods", method = RequestMethod.POST)
	public ResponseEntity<List<Method>> search(@RequestBody Map<String, List<String>> reqMap) {
		logger.info("Searching Method Resource ...");

		PropertyDescriptor[] params = BeanUtils.getPropertyDescriptors(Method.class);
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
		List<Method> methods = this.methodService.listSearch(map);
		if (methods.isEmpty() || methods == null)
			return new ResponseEntity<List<Method>>(methods, HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<List<Method>>(methods, HttpStatus.OK);
	}

}
