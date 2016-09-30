package org.cilab.wise.controller;

import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cilab.m4.model.DataSet;
import org.cilab.m4.service.DataSetService;
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

@Api(value = "dataSets")
@RestController
public class DataSetController {

	/**
	 * Class Name: DataSetController.java 
	 * Description: CRUD, Service
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.20
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	private static final Logger logger = LoggerFactory.getLogger(DataSetController.class);

	@Autowired
	private DataSetService dataSetService;
	
	// -------------------- Read and Search DataSet Collection Resource --------------------
	@RequestMapping(value = "/dataSets", method = RequestMethod.GET)
	public ResponseEntity<List<DataSet>> list(@RequestParam(required = false) MultiValueMap<String, String> params) {
		// read DataSet collection resource when there is no parameter.
		if (params.isEmpty()) {
			logger.info("Reading DataSet Collection Resource ...");
			List<DataSet> dataSetList = dataSetService.readCollection();
			if (dataSetList.isEmpty()) {
				logger.info("No DataSets found.");
				return new ResponseEntity<List<DataSet>>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<List<DataSet>>(dataSetList, HttpStatus.OK);
		}
		// search DataSet collection resource with parameters.
		else {
			logger.info("Searching DataSet Resource ...");

			PropertyDescriptor[] props = BeanUtils.getPropertyDescriptors(DataSet.class);
			List<String> dataSets = new ArrayList<String>();
			for (PropertyDescriptor desc : props) {
				dataSets.add(desc.getName());
			}
			
			Map<String, List<String>> map = new HashMap<String, List<String>>();
			for (String key : params.keySet()) {
				if(dataSets.contains(key)){
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
				return new ResponseEntity<List<DataSet>>(HttpStatus.BAD_REQUEST);
			}else{
				List<DataSet> dataSetList = this.dataSetService.listSearch(map);
				if (dataSetList.isEmpty() || dataSetList == null)
					return new ResponseEntity<List<DataSet>>(dataSetList, HttpStatus.NOT_FOUND);
				else
					return new ResponseEntity<List<DataSet>>(dataSetList, HttpStatus.OK);
			}
		}
	}

	// -------------------- Create a DataSet Instance Resource ------------------
	@RequestMapping(value = "/dataSets/new", method = RequestMethod.POST)
	public ResponseEntity<Boolean> create(@RequestBody DataSet dataSet){

		boolean createdID = dataSetService.newInstance(dataSet);		
		
		return new ResponseEntity<Boolean>(createdID, HttpStatus.CREATED);
	}

	// -------------------- Read a DataSet Instance Resource --------------------
	@RequestMapping(value = "/dataSets/{id}", method = RequestMethod.GET)
	public ResponseEntity<DataSet> read(@PathVariable("id") int dataSetID) {
		logger.info("Reading DataSet Instance Resource of ID: {} ...", dataSetID);
		DataSet dataSet = this.dataSetService.readInstance(dataSetID);
		if (dataSet == null) {
			logger.info("DataSet Instance Resource of ID: {}, not found.", dataSetID);
			return new ResponseEntity<DataSet>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<DataSet>(dataSet, HttpStatus.OK);
	}

	// -------------------- Update a DataSet Instance Resource ------------------
	@RequestMapping(value = "/dataSets/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Boolean> update(@RequestBody DataSet dataSet, @PathVariable("id") int dataSetID) {
		logger.info("Updating DataSet Instance Resource of ID: {} ...", dataSet.getDataSetID());

		if (dataSetID != dataSet.getDataSetID()) {
			logger.info("DataSet Instance Resource of ID: {} , {} doesn't match.", dataSetID, dataSet.getDataSetID());
			return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
		} else {
			DataSet oldDataSet = this.dataSetService.readInstance(dataSetID);
			if (oldDataSet == null) {
				logger.info("DataSet Instance Resource of ID: {}, not found.", dataSetID);
				return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
			}else{
				Boolean res = this.dataSetService.updateInstance(dataSet);
				if (res)
					return new ResponseEntity<Boolean>(res, HttpStatus.OK);
				else
					return new ResponseEntity<Boolean>(res, HttpStatus.CONFLICT);
			}
		}		
	}

	// -------------------- Delete a DataSet Instance Resource ------------------
	@RequestMapping(value = "/dataSets/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> delete(@PathVariable("id") int dataSetID) {
		logger.info("Reading & Deleting DataSet Instance Resource of ID: {} ...", dataSetID);
		DataSet dataSet = this.dataSetService.readInstance(dataSetID);
		if (dataSet == null) {
			logger.info("Unable to delete. DataSet Instance Resource of ID: {}, not found.", dataSetID);
			return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
		}
		Boolean res = this.dataSetService.deleteInstance(dataSetID);
		if (res)
			return new ResponseEntity<Boolean>(res, HttpStatus.OK);
		else
			return new ResponseEntity<Boolean>(res, HttpStatus.CONFLICT); 
	}

	// -------------------- Search for DataSet Resource --------------------
	@RequestMapping(value = "/dataSets", method = RequestMethod.POST)
	public ResponseEntity<List<DataSet>> search(@RequestBody Map<String, List<String>> reqMap) {
		logger.info("Searching DataSet Resource ...");

		PropertyDescriptor[] params = BeanUtils.getPropertyDescriptors(DataSet.class);
		List<String> dataSets = new ArrayList<String>();
		for (PropertyDescriptor desc : params) {
			dataSets.add(desc.getName());
		}
			
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for (String key : reqMap.keySet()) {
			if(dataSets.contains(key)){
				String param = key.substring(0,1).toUpperCase();
				param = param + key.substring(1);
				map.put(param, reqMap.get(key));
			}
			else
				logger.info("Unexpected Parameter :{} has been removed.", key);
		}
		List<DataSet> dataSetList = this.dataSetService.listSearch(map);
		if (dataSetList.isEmpty() || dataSetList == null)
			return new ResponseEntity<List<DataSet>>(dataSetList, HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<List<DataSet>>(dataSetList, HttpStatus.OK);
	}

}
