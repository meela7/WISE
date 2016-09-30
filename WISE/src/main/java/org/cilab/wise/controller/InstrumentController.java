package org.cilab.wise.controller;

import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cilab.m4.model.Instrument;
import org.cilab.m4.service.InstrumentService;
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

@Api(value = "instruements")
@RestController
public class InstrumentController {

	/**
	 * Class Name: InstrumentController.java 
	 * Description: CRUD, Service
	 * 
	 * @author Meilan Jiang
	 * @since 2016.04.28
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	private static final Logger logger = LoggerFactory.getLogger(InstrumentController.class);

	@Autowired
	private InstrumentService instrumentService;

	// -------------------- Read and Search Instrument Collection Resource --------------------
	@RequestMapping(value = "/instruments", method = RequestMethod.GET)
	public ResponseEntity<List<Instrument>> list(@RequestParam(required = false) MultiValueMap<String, String> params) {
		// read Instrument collection resource when there is no parameter.
		if (params.isEmpty()) {
			logger.info("Reading Instrument Collection Resource ...");
			List<Instrument> Instruments = instrumentService.readCollection();
			if (Instruments.isEmpty()) {
				logger.info("No Instruments found.");
				return new ResponseEntity<List<Instrument>>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<List<Instrument>>(Instruments, HttpStatus.OK);
		}
		// search Instrument collection resource with parameters.
		else {
			logger.info("Searching Instrument Resource ...");

			PropertyDescriptor[] props = BeanUtils.getPropertyDescriptors(Instrument.class);
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
				return new ResponseEntity<List<Instrument>>(HttpStatus.BAD_REQUEST);
			}else{
				List<Instrument> instList = this.instrumentService.listSearch(map);
				if (instList.isEmpty() || instList == null)
					return new ResponseEntity<List<Instrument>>(instList, HttpStatus.NOT_FOUND);
				else
					return new ResponseEntity<List<Instrument>>(instList, HttpStatus.OK);
			}
		}
	}

	// -------------------- Create a Instrument Instance Resource ------------------
	@RequestMapping(value = "/instruments/new", method = RequestMethod.POST)
	public ResponseEntity<Boolean> create(@RequestBody Instrument instrument, UriComponentsBuilder ucBuilder) {
		// check if Instrument contains the Not Null field in the database.
		instrument.setMethodType("instrument");
		boolean createdID = instrumentService.newInstance(instrument);
		return new ResponseEntity<Boolean>(createdID, HttpStatus.CREATED);
	}

	// -------------------- Read a Instrument Instance Resource --------------------
	@RequestMapping(value = "/instruments/{id}", method = RequestMethod.GET)
	public ResponseEntity<Instrument> read(@PathVariable("id") int InstrumentID) {
		logger.info("Reading Instrument Instance Resource of ID: {} ...", InstrumentID);
		Instrument inst = this.instrumentService.readInstance(InstrumentID);
		if (inst == null) {
			logger.info("Instrument Instance Resource of ID: {}, not found.", InstrumentID);
			return new ResponseEntity<Instrument>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Instrument>(inst, HttpStatus.OK);
	}

	// -------------------- Update a Instrument Instance Resource ------------------
	@RequestMapping(value = "/instruments/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Boolean> update(@RequestBody Instrument inst, @PathVariable("id") int instID) {
		
		logger.info("Updating Instrument Instance Resource of ID: {} ...", inst.getMethodID());

		if (instID != inst.getMethodID()) {
			logger.info("Instrument Instance Resource of ID: {} , {} doesn't match.", instID, inst.getMethodID());
			return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
		} else{
			Instrument oldInstrument = this.instrumentService.readInstance(instID);
			if (oldInstrument == null) {
				logger.info("Instrument Instance Resource of ID: {}, not found.", instID);
				return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
			}
		}
		// set the null of Instrument with oldInstrument
		Boolean res = this.instrumentService.updateInstance(inst);
		if (res)
			return new ResponseEntity<Boolean>(res, HttpStatus.OK);
		else
			return new ResponseEntity<Boolean>(res, HttpStatus.CONFLICT);
	}

	// -------------------- Delete a Instrument Instance Resource ------------------
	@RequestMapping(value = "/instruments/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> delete(@PathVariable("id") int instID) {
		logger.info("Reading & Deleting Instrument Instance Resource of ID: {} ...", instID);
		Instrument inst = this.instrumentService.readInstance(instID);
		if (inst == null) {
			logger.info("Unable to delete. Instrument Instance Resource of ID: {}, not found.", instID);
			return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
		}
		Boolean res = this.instrumentService.deleteInstance(instID);
		if (res)
			return new ResponseEntity<Boolean>(res, HttpStatus.OK);
		else
			return new ResponseEntity<Boolean>(res, HttpStatus.CONFLICT); 
	}

	// -------------------- Search for Instrument Resource --------------------
	@RequestMapping(value = "/instruments", method = RequestMethod.POST)
	public ResponseEntity<List<Instrument>> search(@RequestBody Map<String, List<String>> reqMap) {
		logger.info("Searching Instrument Resource ...");

		PropertyDescriptor[] params = BeanUtils.getPropertyDescriptors(Instrument.class);
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
		List<Instrument> instList = this.instrumentService.listSearch(map);
		if (instList.isEmpty() || instList == null)
			return new ResponseEntity<List<Instrument>>(instList, HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<List<Instrument>>(instList, HttpStatus.OK);
	}

}
