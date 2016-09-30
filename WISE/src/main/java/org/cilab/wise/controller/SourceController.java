package org.cilab.wise.controller;

import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cilab.m4.model.Source;
import org.cilab.m4.service.SourceService;
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

@Api(value = "source")
@RestController
public class SourceController {

	/**
	 * Class Name: SourceController.java 
	 * Description: CRUD, Service
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.14
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	private static final Logger logger = LoggerFactory.getLogger(SourceController.class);

	@Autowired
	private SourceService sourceService;


	// -------------------- Read and Search Source Collection Resource --------------------
	@RequestMapping(value = "/sources", method = RequestMethod.GET)
	public ResponseEntity<List<Source>> list(@RequestParam(required = false) MultiValueMap<String, String> params) {
		// read Method collection resource when there is no parameter.
		if (params.isEmpty()) {
			logger.info("Reading Source Collection Resource ...");
			List<Source> sourceList = sourceService.readCollection();
			if (sourceList.isEmpty()) {
				logger.info("No Source found.");
				return new ResponseEntity<List<Source>>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<List<Source>>(sourceList, HttpStatus.OK);
		}
		// search Method collection resource with parameters.
		else {
			logger.info("Searching Source Resource ...");

			PropertyDescriptor[] props = BeanUtils.getPropertyDescriptors(Source.class);
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
				return new ResponseEntity<List<Source>>(HttpStatus.BAD_REQUEST);
			}else{
				List<Source> sourceList = this.sourceService.listSearch(map);
				if (sourceList.isEmpty() || sourceList == null)
					return new ResponseEntity<List<Source>>(sourceList, HttpStatus.NOT_FOUND);
				else
					return new ResponseEntity<List<Source>>(sourceList, HttpStatus.OK);
			}
		}
	}

	// -------------------- Create a Source Instance Resource ------------------
	@RequestMapping(value = "/sources/new", method = RequestMethod.POST)
	public ResponseEntity<Boolean> create(@RequestBody Source source){
		logger.info("Creating Source Instance Resource of Name: {} ..." + source.getInstitution());
		
		if (source.getInstitution() == null || source.getContactName() == null ) 
			return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
		
		if (sourceService.isInstanceExist(source.getInstitution(), source.getContactName())) {
			logger.info("A Source with name {} and {} already exist.", source.getInstitution(), source.getContactName());
			return new ResponseEntity<Boolean>(HttpStatus.CONFLICT);
		}
		boolean createdID = sourceService.newInstance(source);
		return new ResponseEntity<Boolean>(createdID, HttpStatus.CREATED);
	}

	// -------------------- Read a Source Instance Resource --------------------
	@RequestMapping(value = "/sources/{id}", method = RequestMethod.GET)
	public ResponseEntity<Source> read(@PathVariable("id") int sourceID) {
		logger.info("Reading Method Instance Resource of ID: {} ...", sourceID);
		Source source = this.sourceService.readInstance(sourceID);
		if (source == null) {
			logger.info("Source Instance Resource of ID: {}, not found.", sourceID);
			return new ResponseEntity<Source>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Source>(source, HttpStatus.OK);
	}

	// -------------------- Update a Source Instance Resource ------------------
	@RequestMapping(value = "/sources/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Boolean> update(@RequestBody Source source, @PathVariable("id") int sourceID) {
		logger.info("Updating Source Instance Resource of ID: {} ...", source.getSourceID());

		if (sourceID != source.getSourceID()) {
			logger.info("Source Instance Resource of ID: {} , {} doesn't match.", sourceID, source.getSourceID());
			return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
		} else {
			Source oldSource = this.sourceService.readInstance(sourceID);
			if (oldSource == null) {
				logger.info("Source Instance Resource of ID: {}, not found.", sourceID);
				return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
			}
		}
		// set the null of Method with oldMethod
		if (this.sourceService.updateInstance(source))
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		else
			return new ResponseEntity<Boolean>(false, HttpStatus.CONFLICT);
	}

	// -------------------- Delete a Source Instance Resource ------------------
	@RequestMapping(value = "/sources/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> delete(@PathVariable("id") int sourceID) {
		logger.info("Reading & Deleting Source Instance Resource of ID: {} ...", sourceID);
		Source source = this.sourceService.readInstance(sourceID);
		if (source == null) {
			logger.info("Unable to delete. Source Instance Resource of ID: {}, not found.", sourceID);
			return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
		}
		if (this.sourceService.deleteInstance(sourceID))
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		else
			return new ResponseEntity<Boolean>(false, HttpStatus.CONFLICT); 
	}

	// -------------------- Search for Source Resource --------------------
	@RequestMapping(value = "/sources", method = RequestMethod.POST)
	public ResponseEntity<List<Source>> search(@RequestBody Map<String, List<String>> reqMap) {
		logger.info("Searching Source Resource ...");

		PropertyDescriptor[] params = BeanUtils.getPropertyDescriptors(Source.class);
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
		List<Source> sourceList = this.sourceService.listSearch(map);
		if (sourceList.isEmpty() || sourceList == null)
			return new ResponseEntity<List<Source>>(sourceList, HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<List<Source>>(sourceList, HttpStatus.OK);
	}

}
