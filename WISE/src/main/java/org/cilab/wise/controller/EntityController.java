package org.cilab.wise.controller;

import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cilab.m4.model.Entity;
import org.cilab.m4.service.EntityService;
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

@Api(value = "entities")
@RestController
public class EntityController {

	/**
	 * Class Name: EntityController.java 
	 * Description: CRUD, Service
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.16
	 * @version 1.0
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	private static final Logger logger = LoggerFactory.getLogger(EntityController.class);

	@Autowired
	private EntityService entityService;
	
	// -------------------- Read and Search Entity Collection Resource --------------------
	@RequestMapping(value = "/entities", method = RequestMethod.GET)
	public ResponseEntity<List<Entity>> list(@RequestParam(required = false) MultiValueMap<String, String> params) {
		// read Entity collection resource when there is no parameter.
		if (params.isEmpty()) {
			logger.info("Reading Entity Collection Resource ...");
			List<Entity> entityList = entityService.readCollection();
			if (entityList.isEmpty()) {
				logger.info("No Entitys found.");
				return new ResponseEntity<List<Entity>>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<List<Entity>>(entityList, HttpStatus.OK);
		}
		// search Entity collection resource with parameters.
		else {
			logger.info("Searching Entity Resource ...");

			PropertyDescriptor[] props = BeanUtils.getPropertyDescriptors(Entity.class);
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
				return new ResponseEntity<List<Entity>>(HttpStatus.BAD_REQUEST);
			}else{
				List<Entity> entityList = this.entityService.listSearch(map);
				if (entityList.isEmpty() || entityList == null)
					return new ResponseEntity<List<Entity>>(entityList, HttpStatus.NOT_FOUND);
				else
					return new ResponseEntity<List<Entity>>(entityList, HttpStatus.OK);
			}
		}
	}

	// -------------------- Create a Entity Instance Resource ------------------
	@RequestMapping(value = "/entities/new", method = RequestMethod.POST)
	public ResponseEntity<Boolean> create(@RequestBody Entity entity){
		
		
		boolean createdID = this.entityService.newInstance(entity);		
		return new ResponseEntity<Boolean>(createdID, HttpStatus.CREATED);
	}

	// -------------------- Read a Entity Instance Resource --------------------
	@RequestMapping(value = "/entities/{id}", method = RequestMethod.GET)
	public ResponseEntity<Entity> read(@PathVariable("id") int entityID) {
		logger.info("Reading Entity Instance Resource of ID: {} ...", entityID);
		Entity entity = this.entityService.readInstance(entityID);
		if (entity == null) {
			logger.info("Entity Instance Resource of ID: {}, not found.", entityID);
			return new ResponseEntity<Entity>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Entity>(entity, HttpStatus.OK);
	}

	// -------------------- Update a Entity Instance Resource ------------------
	@RequestMapping(value = "/entities/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Boolean> update(@RequestBody Entity entity, @PathVariable("id") int entityID) {
		logger.info("Updating Entity Instance Resource of ID: {} ...", entity.getEntityID());

		if (entityID != entity.getEntityID()) {
			logger.info("Entity Instance Resource of ID: {} , {} doesn't match.", entityID, entity.getEntityID());
			return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
		} else {
			Entity oldEntity = this.entityService.readInstance(entityID);
			if (oldEntity == null) {
				logger.info("Entity Instance Resource of ID: {}, not found.", entityID);
				return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
			}
		}
		// set the null of Entity with oldEntity

		Boolean res = this.entityService.updateInstance(entity);
		if (res)
			return new ResponseEntity<Boolean>(res, HttpStatus.OK);
		else
			return new ResponseEntity<Boolean>(res, HttpStatus.CONFLICT);
	}

	// -------------------- Delete a Entity Instance Resource ------------------
	@RequestMapping(value = "/entities/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> delete(@PathVariable("id") int entityID) {
		logger.info("Reading & Deleting Entity Instance Resource of ID: {} ...", entityID);
		Entity entity = this.entityService.readInstance(entityID);
		if (entity == null) {
			logger.info("Unable to delete. Entity Instance Resource of ID: {}, not found.", entityID);
			return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
		}
		Boolean res = this.entityService.deleteInstance(entityID);
		if (res)
			return new ResponseEntity<Boolean>(res, HttpStatus.OK);
		else
			return new ResponseEntity<Boolean>(res, HttpStatus.CONFLICT); 
	}

	// -------------------- Search for Entity Resource --------------------
	@RequestMapping(value = "/entities", method = RequestMethod.POST)
	public ResponseEntity<List<Entity>> search(@RequestBody Map<String, List<String>> reqMap) {
		logger.info("Searching Entity Resource ...");

		PropertyDescriptor[] params = BeanUtils.getPropertyDescriptors(Entity.class);
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
		List<Entity> entityList = this.entityService.listSearch(map);
		if (entityList.isEmpty() || entityList == null)
			return new ResponseEntity<List<Entity>>(entityList, HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<List<Entity>>(entityList, HttpStatus.OK);
	}

}
