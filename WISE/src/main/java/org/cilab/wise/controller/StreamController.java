package org.cilab.wise.controller;

import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.cilab.s4rm.model.ErrorResponse;
import org.cilab.s4rm.model.Log;
import org.cilab.s4rm.model.Stream;
import org.cilab.s4rm.model.Stream_Meta;
import org.cilab.s4rm.model.Stream_Tag;
import org.cilab.s4rm.service.LogService;
import org.cilab.s4rm.service.StreamService;
import org.cilab.s4rm.subscribe.PersistenceManager;
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

@Api(value = "streams")
@RestController
public class StreamController {

	/**
	 * Class Name: StreamController.java Description: CRUD, Service
	 * 
	 * @author Meilan Jiang
	 * @since 2016.06.16
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	private static final Logger logger = LoggerFactory.getLogger(StreamController.class);

	@Autowired
	private StreamService streamService;
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private PersistenceManager persistenceManager;

	// -------------------- Read and Search Stream Collection Resource
	// --------------------
	@RequestMapping(value = "/streams", method = RequestMethod.GET)
	public ResponseEntity<? extends Object> list(@RequestParam(required = false) MultiValueMap<String, String> params) {
		// read Stream collection resource when there is no parameter.
		try {
			if (params.isEmpty()) {
				logger.info(" ========== Reading Stream Collection Resource ... ========== ");

				List<Stream> streams = streamService.readCollection();
				if (streams.isEmpty()) {
					logger.info("No Streams found.");
					return new ResponseEntity<List<Stream>>(HttpStatus.NO_CONTENT);
				}
				return new ResponseEntity<List<Stream>>(streams, HttpStatus.OK);
			} else {
				// search Stream collection resource with parameters.
				logger.info(" ========== Searching Stream Resource ... ========== ");

				PropertyDescriptor[] props = BeanUtils.getPropertyDescriptors(Stream.class);
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
						for (String value : params.get(key)) {
							// decode parameters
							try {
								values.add(new String(value.getBytes("8859_1"), "UTF-8"));
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
						}
						map.put(param, values);
					} else {
						logger.info(" ========== Unexpected Parameter :{} has been removed. ========== ", key);
						return new ResponseEntity<ErrorResponse>(new ErrorResponse(7, "Unexpected parameter: " + key,
								"Expected Parameters: " + variables.toString(), ""), HttpStatus.BAD_REQUEST);
					}
				}
				if (map.keySet().size() == 0) {
					return new ResponseEntity<ErrorResponse>(new ErrorResponse(8, "No matching result found!", "", ""),
							HttpStatus.BAD_REQUEST);
				} else {
					List<Stream> streams = this.streamService.listSearch(map);
					if (streams.isEmpty() || streams == null)
						return new ResponseEntity<ErrorResponse>(
								new ErrorResponse(8, "No matching result found!", "", ""), HttpStatus.NOT_FOUND);
					else
						return new ResponseEntity<List<Stream>>(streams, HttpStatus.OK);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<ErrorResponse>(
					new ErrorResponse(5, e.getLocalizedMessage(), Arrays.toString(e.getStackTrace()), ""),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// -------------------- Create a Stream Instance Resource ------------------
	@RequestMapping(value = "/streams/new", method = RequestMethod.POST)
	public ResponseEntity<? extends Object> create(@RequestBody Stream stream) {
		try {
			logger.info(" ========== Creating Stream Instance Resource of Name: {} ... ========== " + stream.getName());
			// check if stream contains the Not Null field in the database.
			if (stream.getName() == null)
				return new ResponseEntity<ErrorResponse>(new ErrorResponse(4, "Required fields are missing !", "", ""),
						HttpStatus.BAD_REQUEST);

			if (streamService.isInstanceExist(stream.getCreatedAt(), stream.getSensorID())) {
				logger.info(" ========== A Stream with name {} already exist. ========== ", stream.getName());
				return new ResponseEntity<ErrorResponse>(
						new ErrorResponse(4, "Resource already exist!", stream.getName(), ""), HttpStatus.CONFLICT);
			} else {
				streamService.newInstance(stream);
				if(stream.getPersistence() == 1){
					//Subscribe Stream
					persistenceManager.startSubscribe(stream.getId());
					
					//Create Log
					Log log = new Log();
					log.setStreamID(stream.getId());
					
					SimpleDateFormat formatter = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss", Locale.KOREA );
					Calendar cal = Calendar.getInstance(); 
					log.setStartedAt(formatter.format(cal.getTime()));
					
					logService.newInstance(log);
				}
				return new ResponseEntity<Stream>(stream, HttpStatus.CREATED);
			}
		} catch (Exception e) {
			return new ResponseEntity<ErrorResponse>(
					new ErrorResponse(5, e.getLocalizedMessage(), Arrays.toString(e.getStackTrace()), ""),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// -------------------- Read a Stream Instance Resource --------------------
	@RequestMapping(value = "/streams/{id}", method = RequestMethod.GET)
	public ResponseEntity<? extends Object> read(@PathVariable("id") String streamID) {
		try {
			logger.info(" ========== Reading Stream Instance Resource of ID: {} ... ========== ", streamID);
			Stream stream = this.streamService.readInstance(streamID);
			if (stream == null) {
				logger.info(" ========== Stream Instance Resource of ID: {}, not found. ========== ", streamID);
				return new ResponseEntity<ErrorResponse>(new ErrorResponse(3, "Resource not found!", "", ""),
						HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<Stream>(stream, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<ErrorResponse>(
					new ErrorResponse(5, e.getLocalizedMessage(), Arrays.toString(e.getStackTrace()), ""),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// -------------------- Subscribe a Stream Instance Resource --------------------
	@RequestMapping(value = "/streams/{id}/subscribe", method = RequestMethod.GET)
	public ResponseEntity<? extends Object> subscribe(@PathVariable("id") String streamID) {
		try {
			logger.info(" ========== Subscribe Stream Instance Resource of ID: {} ... ========== ", streamID);
			Stream stream = this.streamService.readInstance(streamID);
			if (stream == null) {
				logger.info(" ========== Stream Instance Resource of ID: {}, not found. ========== ", streamID);
				return new ResponseEntity<ErrorResponse>(new ErrorResponse(3, "Resource not found!", "", ""),
						HttpStatus.NOT_FOUND);
			}else if(stream.getPersistence() == 1) {
				if(persistenceManager.getSubscriberList().contains(streamID)){
					logger.info(" ========== Stream Instance Resource of ID: {}, Subscriber exist. ========== ", streamID);
					return new ResponseEntity<ErrorResponse>(new ErrorResponse(3, "Stream is already in Subscribe!", "", ""),
							HttpStatus.BAD_REQUEST);
				}else{
					logger.info(" ========== Stream Instance Resource of ID: {}, Start Subscriber. ========== ", streamID);
					persistenceManager.startSubscribe(streamID);
					return new ResponseEntity<Boolean>(true, HttpStatus.OK);
				}
				
			}else{
				//Subscribe Stream
				persistenceManager.startSubscribe(stream.getId());
				
				SimpleDateFormat formatter = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss", Locale.KOREA );
				Calendar cal = Calendar.getInstance(); 
				String now =  formatter.format(cal.getTime());
				
				//Update Stream
				stream.setPersistenceStartedAt(now);
				streamService.updateInstance(stream);
				
				//Create Log
				Log log = new Log();
				log.setStreamID(stream.getId());
				
				log.setStartedAt(now);
				
				logService.newInstance(log);
				return new ResponseEntity<Boolean>(true, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<ErrorResponse>(
					new ErrorResponse(5, e.getLocalizedMessage(), Arrays.toString(e.getStackTrace()), ""),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// -------------------- UnSubscribe a Stream Instance Resource --------------------
	@RequestMapping(value = "/streams/{id}/unsubscribe", method = RequestMethod.GET)
	public ResponseEntity<? extends Object> unSubscribe(@PathVariable("id") String streamID) {
		try {
			logger.info(" ========== UnSubscribe Stream Instance Resource of ID: {} ... ========== ", streamID);
			Stream stream = this.streamService.readInstance(streamID);
			if (stream == null) {
				logger.info(" ========== Stream Instance Resource of ID: {}, not found. ========== ", streamID);
				return new ResponseEntity<ErrorResponse>(new ErrorResponse(3, "Resource not found!", "", ""),
						HttpStatus.NOT_FOUND);
			}else if(stream.getPersistence() == 0) {
				if(persistenceManager.getSubscriberList().contains(streamID)){
					logger.info(" ========== Stream Instance Resource of ID: {}, Stop Subscriber. ========== ", streamID);
					persistenceManager.startSubscribe(streamID);
					return new ResponseEntity<Boolean>(true, HttpStatus.OK);
					
				}else{
					logger.info(" ========== Stream Instance Resource of ID: {}, NOT in Subscribe. ========== ", streamID);
					return new ResponseEntity<ErrorResponse>(new ErrorResponse(3, "Stream is NOT in Subscribe!", "", ""),
							HttpStatus.BAD_REQUEST);
				}
				
			}else{
				//UnSubscribe Stream
				logger.info(" ========== Stream Instance Resource of ID: {}, Stop Subscriber. ========== ", streamID);
				persistenceManager.stopSubscribe(stream.getId());
				
				//Update Log
				Map map = new HashMap();
				map.put("StreamID", streamID);
				map.put("StartedAt", stream.getPersistenceStartedAt());
				Log log = logService.search(map).get(0);
				
				SimpleDateFormat formatter = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss", Locale.KOREA );
				Calendar cal = Calendar.getInstance(); 
				log.setStopedAt(formatter.format(cal.getTime()));
				
				logger.info(" ========== Update Persistence Log ID: {}, StartedAt: {}, StopedAt: {} . ========== ", streamID, log.getStartedAt(), log.getStopedAt());
				
				logService.updateInstance(log);
				return new ResponseEntity<Boolean>(true, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<ErrorResponse>(
					new ErrorResponse(5, e.getLocalizedMessage(), Arrays.toString(e.getStackTrace()), ""),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// -------------------- Update a Stream Instance Resource ------------------
	@RequestMapping(value = "/streams/{id}", method = RequestMethod.PUT)
	public ResponseEntity<? extends Object> update(@RequestBody Stream stream, @PathVariable("id") String streamID) {
		try {
			logger.info("Updating Stream Instance Resource of ID: {} ...", stream.getId());

			if (streamID.equals(stream.getId())) {
				Set<Stream_Meta> metaSet = new HashSet<Stream_Meta>();
				for (Stream_Meta meta : stream.getMetas()) {
					meta.setStreamID(stream.getId());
					metaSet.add(meta);
				}
				stream.setMetas(metaSet);
				Set<Stream_Tag> tagSet = new HashSet<Stream_Tag>();
				for (Stream_Tag tag : stream.getTags()) {
					tag.setStreamID(stream.getId());
					tagSet.add(tag);
				}
				stream.setTags(tagSet);

				streamService.updateInstance(stream);
				return new ResponseEntity<Stream>(stream, HttpStatus.OK);
			} else {
				logger.info(" ========== Stream Instance Resource of ID: {} , {} doesn't match. ========== ", streamID,
						stream.getId());
				return new ResponseEntity<ErrorResponse>(new ErrorResponse(5, "Resource doesn't match!", "", ""),
						HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<ErrorResponse>(
					new ErrorResponse(5, e.getLocalizedMessage(), Arrays.toString(e.getStackTrace()), ""),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// -------------------- Delete a Stream Instance Resource ------------------
	@RequestMapping(value = "/streams/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<? extends Object> delete(@PathVariable("id") String streamID) {
		try {
			logger.info(" ========== Reading & Deleting Stream Instance Resource of ID: {} ... ========== ", streamID);
			Stream stream = this.streamService.readInstance(streamID);
			if (stream == null) {
				logger.info(" ========== Unable to delete. Stream Instance Resource of ID: {}, not found. ========== ",
						streamID);
				return new ResponseEntity<ErrorResponse>(new ErrorResponse(3, "Resource doesn't exist!", "", ""),
						HttpStatus.NOT_FOUND);
			}
			streamService.deleteInstance(streamID);
			return new ResponseEntity<Stream>(stream, HttpStatus.CONFLICT);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<ErrorResponse>(
					new ErrorResponse(5, e.getLocalizedMessage(), Arrays.toString(e.getStackTrace()), ""),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// -------------------- Search for Stream Resource --------------------
	@RequestMapping(value = "/streams", method = RequestMethod.POST)
	public ResponseEntity<? extends Object> search(@RequestBody Map<String, List<String>> reqMap) {
		try {
			logger.info(" ========== Searching Stream Resource ... ========== ");

			// remove the parameters which doesn't match with column in the list
			PropertyDescriptor[] params = BeanUtils.getPropertyDescriptors(Stream.class);
			List<String> variables = new ArrayList<String>();
			for (PropertyDescriptor desc : params) {
				variables.add(desc.getName());
			}

			Map<String, List<String>> map = new HashMap<String, List<String>>();
			for (String key : reqMap.keySet()) {
				if (variables.contains(key)) {
					String param = key.substring(0, 1).toUpperCase();
					param = param + key.substring(1);
					map.put(param, reqMap.get(key));
				} else {
					logger.info(" ========== Unexpected Parameter :{} has been removed. ========== ", key);
					return new ResponseEntity<ErrorResponse>(
							new ErrorResponse(3, "Unexpected Parameters included!", "", ""), HttpStatus.BAD_REQUEST);
				}
			}
			List<Stream> streams = streamService.listSearch(map);
			if (streams.isEmpty() || streams == null)
				return new ResponseEntity<ErrorResponse>(new ErrorResponse(4, "No matching result found!", "", ""),
						HttpStatus.NOT_FOUND);
			else
				return new ResponseEntity<List<Stream>>(streams, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<ErrorResponse>(
					new ErrorResponse(5, e.getLocalizedMessage(), Arrays.toString(e.getStackTrace()), ""),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
