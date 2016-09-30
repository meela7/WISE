package org.cilab.wise.controller;

import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cilab.m4.model.Prediction;
import org.cilab.m4.service.PredictionService;
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

@Api(value = "predictions")
@RestController
public class PredictionController {

	/**
	 * Class Name: PredictionController.java 
	 * Description: CRUD, Service
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.13
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	private static final Logger logger = LoggerFactory.getLogger(PredictionController.class);

	@Autowired
	private PredictionService predictionService;

	// -------------------- Read and Search Prediction Collection Resource --------------------
	@RequestMapping(value = "/predictions", method = RequestMethod.GET)
	public ResponseEntity<List<Prediction>> list(@RequestParam(required = false) MultiValueMap<String, String> params) {
		// read Prediction collection resource when there is no parameter.
		if (params.isEmpty()) {
			logger.info("Reading Prediction Collection Resource ...");
			List<Prediction> predList = predictionService.readCollection();
			if (predList.isEmpty()) {
				logger.info("No Predictions found.");
				return new ResponseEntity<List<Prediction>>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<List<Prediction>>(predList, HttpStatus.OK);
		}
		// search Prediction collection resource with parameters.
		else {
			logger.info("Searching Prediction Resource ...");

			PropertyDescriptor[] props = BeanUtils.getPropertyDescriptors(Prediction.class);
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
				return new ResponseEntity<List<Prediction>>(HttpStatus.BAD_REQUEST);
			}else{
				List<Prediction> predList = this.predictionService.listSearch(map);
				if (predList.isEmpty() || predList == null)
					return new ResponseEntity<List<Prediction>>(predList, HttpStatus.NOT_FOUND);
				else
					return new ResponseEntity<List<Prediction>>(predList, HttpStatus.OK);
			}
		}
	}

	// -------------------- Create a Prediction Instance Resource ------------------
	@RequestMapping(value = "/predictions/new", method = RequestMethod.POST)
	public ResponseEntity<Boolean> create(@RequestBody Prediction pred) {
		// check if Prediction contains the Not Null field in the database.
		pred.setMethodType("prediction");
		boolean createdID = predictionService.newInstance(pred);
		return new ResponseEntity<Boolean>(createdID, HttpStatus.CREATED);
	}

	// -------------------- Read a Prediction Instance Resource --------------------
	@RequestMapping(value = "/predictions/{id}", method = RequestMethod.GET)
	public ResponseEntity<Prediction> read(@PathVariable("id") int predID) {
		logger.info("Reading Prediction Instance Resource of ID: {} ...", predID);
		Prediction pred = this.predictionService.readInstance(predID);
		if (pred == null) {
			logger.info("Prediction Instance Resource of ID: {}, not found.", predID);
			return new ResponseEntity<Prediction>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Prediction>(pred, HttpStatus.OK);
	}

	// -------------------- Update a Prediction Instance Resource ------------------
	@RequestMapping(value = "/predictions/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Boolean> update(@RequestBody Prediction pred, @PathVariable("id") int predID) {
		
		Prediction oldPrediction = this.predictionService.readInstance(predID);
		if (oldPrediction == null) {
			logger.info("Prediction Instance Resource of ID: {}, not found.", predID);
			return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
		}
		
		// set the null of Prediction with oldPrediction

		Boolean res = this.predictionService.updateInstance(pred);
		if (res)
			return new ResponseEntity<Boolean>(res, HttpStatus.OK);
		else
			return new ResponseEntity<Boolean>(res, HttpStatus.CONFLICT);
	}

	// -------------------- Delete a Prediction Instance Resource ------------------
	@RequestMapping(value = "/predictions/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> delete(@PathVariable("id") int predID) {
		logger.info("Reading & Deleting Prediction Instance Resource of ID: {} ...", predID);
		Prediction pred = this.predictionService.readInstance(predID);
		if (pred == null) {
			logger.info("Unable to delete. Prediction Instance Resource of ID: {}, not found.", predID);
			return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
		}
		Boolean res = this.predictionService.deleteInstance(predID);
		if (res)
			return new ResponseEntity<Boolean>(res, HttpStatus.OK);
		else
			return new ResponseEntity<Boolean>(res, HttpStatus.CONFLICT); 
	}

	// -------------------- Search for Prediction Resource --------------------
	@RequestMapping(value = "/predictions", method = RequestMethod.POST)
	public ResponseEntity<List<Prediction>> search(@RequestBody Map<String, List<String>> reqMap) {
		logger.info("Searching Prediction Resource ...");

		PropertyDescriptor[] params = BeanUtils.getPropertyDescriptors(Prediction.class);
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
		List<Prediction> predList = this.predictionService.listSearch(map);
		if (predList.isEmpty() || predList == null)
			return new ResponseEntity<List<Prediction>>(predList, HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<List<Prediction>>(predList, HttpStatus.OK);
	}

}
