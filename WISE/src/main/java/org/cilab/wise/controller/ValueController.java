package org.cilab.wise.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.cilab.s4rm.model.ErrorResponse;
import org.cilab.s4rm.model.SensorData;
import org.cilab.s4rm.model.Value;
import org.cilab.s4rm.service.ValueService;
import org.cilab.s4rm.service.impl.ValueServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@Api(value = "values")
@RestController
public class ValueController {

	/**
	 * Class Name: ValueController.java Description: CRUD, Service
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.23
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */

	private ValueService valueService = new ValueServiceImpl();

	private static final Logger logger = LoggerFactory.getLogger(ValueController.class);

	// -------------------- Create a Value Instance Resource ------------------
	@RequestMapping(value = "/values/new", method = RequestMethod.POST)
	public ResponseEntity<? extends Object> create(@RequestBody List<SensorData> sDataList) {

		// insert into influx DB installed in the Amazon EC2 - using HTTP API
		// http://52.193.230.38:8086/write?db=wise
		// DB - wise, Measurement - value]
		boolean flag = false;
		for (SensorData sData : sDataList) {
			logger.info("StreamID: {}, Value: {}", sData.getId(), sData.getValue());
			Value val = new Value();
			val.setStreamID(sData.getId());
			val.setDateTime(sData.getTimestamp());
			val.setValue(sData.getValue());
			try {
				flag = this.valueService.newInstance(val);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return new ResponseEntity<Boolean>(flag, HttpStatus.CREATED);
	}

	// -------------------- Search Value Instance Resource ------------------
	@RequestMapping(value = "/values", method = RequestMethod.POST)
	public ResponseEntity<? extends Object> searchByPost(@RequestBody Map<String, String> reqMap) {

		String streamID = "";
		String startDate = "";
		String endDate ="";
		List<Value> values = new ArrayList<Value>();
		
		for (String key : reqMap.keySet()) {
			if(key.equals("streamID"))
				streamID = reqMap.get(key);
			else if(key.equals("startDate"))
				startDate = reqMap.get(key);
			else if(key.equals("endDate"))
				endDate = reqMap.get(key);
			else
				logger.info("Unexpected Parameter :{} has been removed.", key);
		}
		try {
			values = valueService.search(streamID, startDate, endDate);
			if(values == null || values.isEmpty())
				return new ResponseEntity<ErrorResponse>(
						new ErrorResponse(8, "No matching result found!", "", ""), HttpStatus.NOT_FOUND);
			else
				return new ResponseEntity<List<Value>>(values, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<ErrorResponse>(
					new ErrorResponse(5, e.getLocalizedMessage(), Arrays.toString(e.getStackTrace()), ""),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// -------------------- Search Value Instance Resource ------------------
	@RequestMapping(value = "/values", method = RequestMethod.GET)
	public ResponseEntity<? extends Object> searchByGet(@RequestParam(required = false, value = "streamID") String streamID,
			@RequestParam(required = false, value = "startDate") String startDate,
			@RequestParam(required = false, value = "endDate") String endDate) {
		
		List<Value> values = new ArrayList<Value>();

		try {
			values = valueService.search(streamID, startDate, endDate);
			if(values == null || values.isEmpty())
				return new ResponseEntity<ErrorResponse>(
						new ErrorResponse(8, "No matching result found!", "", ""), HttpStatus.NOT_FOUND);
			else
				return new ResponseEntity<List<Value>>(values, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<ErrorResponse>(
					new ErrorResponse(5, e.getLocalizedMessage(), Arrays.toString(e.getStackTrace()), ""),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@RequestMapping(produces = "text/csv", value = "/values.csv", params = {"streamID", "startDate", "endDate"}, method = RequestMethod.GET)
	public ResponseEntity<? extends Object> searchCSV(@RequestParam(required = false, value = "streamID") String streamID,
			@RequestParam(required = false, value = "startDate") String startDate,
			@RequestParam(required = false, value = "endDate") String endDate) {
		StringBuffer result = new StringBuffer();

		try {
			List<Value> values = valueService.search(streamID, startDate, endDate);
			if(values == null || values.isEmpty())
				return new ResponseEntity<ErrorResponse>(
						new ErrorResponse(8, "No matching result found!", "", ""), HttpStatus.NOT_FOUND);
			else{
				for (Value dv: values){
					result.append(dv.getDateTime());
					result.append(", ");
					result.append(dv.getValue());
					result.append("\n");
				}
				return new ResponseEntity<String>(result.toString(), HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<ErrorResponse>(
					new ErrorResponse(5, e.getLocalizedMessage(), Arrays.toString(e.getStackTrace()), ""),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

}
