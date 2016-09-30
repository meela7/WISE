package org.cilab.wise.controller;

import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cilab.m4.model.Fish;
import org.cilab.m4.service.FishService;
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
import org.springframework.web.util.UriComponentsBuilder;

import io.swagger.annotations.Api;

@Api(value = "Fishes")
@RestController
public class FishController {

	/**
	 * Class Name: FishController.java 
	 * Description: CRUD, Service
	 * 
	 * @author Meilan Jiang
	 * @since 2016.06.10
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	private static final Logger logger = LoggerFactory.getLogger(FishController.class);

	@Autowired
	private FishService fishService;

	// -------------------- Read and Search Fish Collection Resource --------------------
	@RequestMapping(value = "/fishes", method = RequestMethod.GET)
	public ResponseEntity<List<Fish>> list(@RequestParam(required = false) MultiValueMap<String, String> params) {
		// read Fish collection resource when there is no parameter.
		if (params.isEmpty()) {
			logger.info("Reading Fish Collection Resource ...");
			List<Fish> fishes = fishService.readCollection();
			if (fishes.isEmpty()) {
				logger.info("No Fishs found.");
				return new ResponseEntity<List<Fish>>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<List<Fish>>(fishes, HttpStatus.OK);
		}
		// search Fish collection resource with parameters.
		else {
			logger.info("Searching Fish Resource ...");

			PropertyDescriptor[] props = BeanUtils.getPropertyDescriptors(Fish.class);
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
				else{
					logger.info("Unexpected Parameter :{} has been removed.", key);
				}
			}
			if(map.keySet().size() == 0){
				return new ResponseEntity<List<Fish>>(HttpStatus.BAD_REQUEST);
			}else{
				List<Fish> fishes = this.fishService.listSearch(map);
				if (fishes.isEmpty() || fishes == null)
					return new ResponseEntity<List<Fish>>(HttpStatus.NOT_FOUND);
				else
					return new ResponseEntity<List<Fish>>(fishes, HttpStatus.OK);
			}
		}
	}

	// -------------------- Create a Fish Instance Resource ------------------
	@RequestMapping(value = "/fishes/new", method = RequestMethod.POST)
	public ResponseEntity<Boolean> create(@RequestBody Fish fish, UriComponentsBuilder ucBuilder) {
		logger.info("Creating Fish Instance Resource of Name: {} ..." + fish.getSpecies());
		// check if fish contains the all required fields
		if (fish.getSpecies() == null)
			return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
		fish.setEntityName(fish.getSpecies());
		fish.setEntityType("fish");
		boolean createdID = fishService.newInstance(fish);
		return new ResponseEntity<Boolean>(createdID, HttpStatus.CREATED);
	}

	// -------------------- Read a Fish Instance Resource --------------------
	@RequestMapping(value = "/fishes/{id}", method = RequestMethod.GET)
	public ResponseEntity<Fish> read(@PathVariable("id") int fishID) {
		logger.info("Reading Fish Instance Resource of ID: {} ...", fishID);
		Fish fish = this.fishService.readInstance(fishID);
		if (fish == null) {
			logger.info("Fish Instance Resource of ID: {}, not found.", fishID);
			return new ResponseEntity<Fish>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Fish>(fish, HttpStatus.OK);
	}

	// -------------------- Update a Fish Instance Resource ------------------
	@RequestMapping(value = "/fishes/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Boolean> update(@RequestBody Fish fish, @PathVariable("id") int fishID) {
		logger.info("Updating Fish Instance Resource of ID: {} ...", fish.getEntityID());

		if (fishID != fish.getEntityID()) {
			logger.info("Fish Instance Resource of ID: {} , {} doesn't match.", fishID, fish.getEntityID());
			return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
		} else {
			Fish oldFish = this.fishService.readInstance(fishID);
			if (oldFish == null) {
				logger.info("Fish Instance Resource of ID: {}, not found.", fishID);
				return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
			}
		}
		// set the null of Fish with oldFish

		boolean res = this.fishService.updateInstance(fish);
		if (res)
			return new ResponseEntity<Boolean>(res, HttpStatus.OK);
		else
			return new ResponseEntity<Boolean>(res, HttpStatus.CONFLICT);
	}

	// -------------------- Delete a Fish Instance Resource ------------------
	@RequestMapping(value = "/fishes/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> delete(@PathVariable("id") int fishID) {
		logger.info("Reading & Deleting Fish Instance Resource of ID: {} ...", fishID);
		Fish fish = this.fishService.readInstance(fishID);
		if (fish == null) {
			logger.info("Unable to delete. Fish Instance Resource of ID: {}, not found.", fishID);
			return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
		}
		Boolean res = this.fishService.deleteInstance(fishID);
		if (res)
			return new ResponseEntity<Boolean>(res, HttpStatus.OK);
		else
			return new ResponseEntity<Boolean>(res, HttpStatus.CONFLICT); 
	}

	// -------------------- Search for Fish Resource --------------------
	@RequestMapping(value = "/fishes", method = RequestMethod.POST)
	public ResponseEntity<List<Fish>> search(@RequestBody Map<String, List<String>> reqMap) {
		logger.info("Searching Fish Resource ...");

		PropertyDescriptor[] params = BeanUtils.getPropertyDescriptors(Fish.class);
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
		if(map.keySet().size() == 0){
			return new ResponseEntity<List<Fish>>(HttpStatus.BAD_REQUEST);
		}else{
			List<Fish> fishes = this.fishService.listSearch(map);
		
			if (fishes.isEmpty() || fishes == null)
				return new ResponseEntity<List<Fish>>(HttpStatus.NOT_FOUND);
			else
				return new ResponseEntity<List<Fish>>(fishes, HttpStatus.OK);
		}
	}

}
