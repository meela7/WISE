package org.cilab.wise.controller;

import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cilab.s4rm.model.Sensor;
import org.cilab.s4rm.service.SensorService;
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

@Api(value = "sensors")
@RestController
public class SensorController {

	/**
	 * Class Name: SensorController.java 
	 * Description: CRUD, Service
	 * 
	 * @author Meilan Jiang
	 * @since 2016.06.16
	 * @version 1.2
	 * 
	 *          Copyright(c) 2016 by CILAB All right reserved.
	 */
	private static final Logger logger = LoggerFactory.getLogger(SensorController.class);

	@Autowired
	private SensorService sensorService;

	// -------------------- Read and Search Sensor Collection Resource
	// --------------------
	@RequestMapping(value = "/sensors", method = RequestMethod.GET)
	public ResponseEntity<List<Sensor>> list(@RequestParam(required = false) MultiValueMap<String, String> params) {
		// read Sensor collection resource when there is no parameter.
		if (params.isEmpty()) {
			logger.info("Reading Sensor Collection Resource ...");
			List<Sensor> sensors = sensorService.readCollection();
			if (sensors.isEmpty()) {
				logger.info("No Sensors found.");
				return new ResponseEntity<List<Sensor>>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<List<Sensor>>(sensors, HttpStatus.OK);
		}
		// search Sensor collection resource with parameters.
		else {
			logger.info("Searching Sensor Resource ...");

			PropertyDescriptor[] props = BeanUtils.getPropertyDescriptors(Sensor.class);
			List<String> variables = new ArrayList<String>();
			for (PropertyDescriptor desc : props) {
				variables.add(desc.getName());
			}
			Map<String, List<String>> map = new HashMap<String, List<String>>();
			for (String key : params.keySet()) {
				if (variables.contains(key)) {
					// uppercase first letter of property name
					String param = key.substring(0, 1).toUpperCase();
					param = param + key.substring(1);

					List<String> values = new ArrayList<String>();
					for(String value: params.get(key)){
						// decode parameters
						try {
							values.add(new String(value.getBytes("8859_1"), "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
					map.put(param, values);
				} else
					logger.info("Unexpected Parameter :{} has been removed.", key);
			}
			if (map.keySet().size() == 0) {
				return new ResponseEntity<List<Sensor>>(HttpStatus.BAD_REQUEST);
			} else {
				List<Sensor> sensors = this.sensorService.listSearch(map);
				if (sensors.isEmpty() || sensors == null)
					return new ResponseEntity<List<Sensor>>(HttpStatus.NOT_FOUND);
				else
					return new ResponseEntity<List<Sensor>>(sensors, HttpStatus.OK);
			}
		}
	}

	// -------------------- Create a Sensor Instance Resource ------------------
	@RequestMapping(value = "/sensors/new", method = RequestMethod.POST)
	public ResponseEntity<Boolean> create(@RequestBody Sensor sensor) {
		
		// check if sensor contains the Not Null field in the database.
		
		if (sensorService.isInstanceExist(sensor.getStreamID(), sensor.getCreatedAt())) {
			logger.info("A Sensor with name {} already exist.", sensor.getName());
			return new ResponseEntity<Boolean>(HttpStatus.CONFLICT);
		}else{
			logger.info("Creating Sensor Instance Resource of StreamID: {} ..." + sensor.getStreamID());
		}

		boolean createRes = sensorService.newInstance(sensor);
//		boolean createRes = true;
		return new ResponseEntity<Boolean>(createRes, HttpStatus.CREATED);
	}

	// -------------------- Read a Sensor Instance Resource --------------------
	@RequestMapping(value = "/sensors/{id}", method = RequestMethod.GET)
	public ResponseEntity<Sensor> read(@PathVariable("id") String sensorID) {
		logger.info("Reading Sensor Instance Resource of ID: {} ...", sensorID);
		Sensor sensor = this.sensorService.readInstance(sensorID);
		if (sensor == null) {
			logger.info("Sensor Instance Resource of ID: {}, not found.", sensorID);
			return new ResponseEntity<Sensor>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Sensor>(sensor, HttpStatus.OK);
	}

	// -------------------- Update a Sensor Instance Resource ------------------
	@RequestMapping(value = "/sensors/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Boolean> update(@RequestBody Sensor sensor, @PathVariable("id") String sensorID) {
		logger.info("Updating Sensor Instance Resource of ID: {} ...", sensor.getId());

		if (!sensorID.equals(sensor.getId())) {
			logger.info("Sensor Instance Resource of ID: {} , {} doesn't match.", sensorID, sensor.getId());
			return new ResponseEntity<Boolean>(false, HttpStatus.BAD_REQUEST);
		} else {
//			Sensor oldSensor = this.sensorService.readInstance(sensorID);
//			if (oldSensor == null) {
//				logger.info("Sensor Instance Resource of ID: {}, not found.", sensorID);
//				return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
//			}
		
			// set the null of Sensor with oldSensor
	
			Boolean res = this.sensorService.updateInstance(sensor);
			if (res)
				return new ResponseEntity<Boolean>(res, HttpStatus.OK);
			else
				return new ResponseEntity<Boolean>(res, HttpStatus.CONFLICT);
		}
	}

	// -------------------- Delete a Sensor Instance Resource ------------------
	@RequestMapping(value = "/sensors/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> delete(@PathVariable("id") String sensorID) {
		logger.info("Reading & Deleting Sensor Instance Resource of ID: {} ...", sensorID);
		Sensor sensor = this.sensorService.readInstance(sensorID);
		if (sensor == null) {
			logger.info("Unable to delete. Sensor Instance Resource of ID: {}, not found.", sensorID);
			return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
		}
		Boolean res = this.sensorService.deleteInstance(sensorID);
		if (res)
			return new ResponseEntity<Boolean>(res, HttpStatus.OK);
		else
			return new ResponseEntity<Boolean>(res, HttpStatus.CONFLICT); // when Sensor has existing related Sites
	}

	// -------------------- Search for Sensor Resource --------------------
	@RequestMapping(value = "/sensors", method = RequestMethod.POST)
	public ResponseEntity<List<Sensor>> search(@RequestBody Map<String, List<String>> reqMap) {
		logger.info("Searching Sensor Resource ...");

		// remove the parameters which doesn't match with column in the list
		PropertyDescriptor[] params = BeanUtils.getPropertyDescriptors(Sensor.class);
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
		List<Sensor> sensors = this.sensorService.listSearch(map);
		if (sensors.isEmpty() || sensors == null)
			return new ResponseEntity<List<Sensor>>(HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<List<Sensor>>(sensors, HttpStatus.OK);
	}

}
