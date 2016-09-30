package org.cilab.s4rm.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.cilab.s4rm.model.Value;
import org.cilab.s4rm.service.ValueService;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.InfluxDB.ConsistencyLevel;
import org.influxdb.InfluxDB.LogLevel;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Pong;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class ValueServiceImpl implements ValueService {

	/**
	 * Class Name: ValueServiceImpl.java 
	 * 
	 * Description:
	 *  
	 * @author Meilan Jiang
	 * @since 2016.05.24
	 * @version 1.2
	 * 
	 *          Copyright(c) 2016 by CILAB All right reserved.
	 */

	private static final Logger logger = LoggerFactory.getLogger(ValueServiceImpl.class);
	
	private String ip;
	private String port;
	private String user;
	private String password;
	private String db;

	
	public ValueServiceImpl(){
		Resource resource = new ClassPathResource("META-INF/config.properties");
		Properties props = null;
		try {
			props = PropertiesLoaderUtils.loadProperties(resource);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.ip = props.getProperty("idbc.databaseIP");
		this.port = props.getProperty("idbc.databasePort");
		this.user = props.getProperty("idbc.username");
		this.password = props.getProperty("idbc.password");
		this.db = props.getProperty("idbc.database");	
		
	}
	
	@Override
	public boolean newInstance(Value value) throws Exception{
		logger.debug(" ===== StreamID: {}, DateTime: {}, Value: {} ===== ", value.getStreamID(), value.getDateTime(),
				value.getValue());
		InfluxDB influxDB = InfluxDBFactory.connect("http://" + ip + ":" + port, user, password);
		
//		boolean influxDBstarted = false;
//		do {
//			Pong response;
//			try {
//				response = influxDB.ping();
//				System.out.println(response);
//				if (!response.getVersion().equalsIgnoreCase("unknown")) {
//					influxDBstarted = true;
//				}
//			} catch (Exception e) {
//				// NOOP intentional
//				e.printStackTrace();
//			}
//			try {
//				Thread.sleep(100L);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} while (!influxDBstarted);
//		influxDB.setLogLevel(LogLevel.FULL);
//		// String logs = CharStreams.toString(new InputStreamReader(containerLogsStream, Charsets.UTF_8));
		logger.debug(" ===== Connected to InfluxDB Version: {} =====", influxDB.version());
				
		BatchPoints batchPoints = BatchPoints.database(db).tag("async", "true").retentionPolicy("default")
				.consistency(ConsistencyLevel.ALL).build();

		Point point = Point.measurement("DataValue").addField("StreamID", value.getStreamID())
				.addField("DateTime", value.getDateTime()).addField("Value", value.getValue()).build();

		batchPoints.point(point);
		influxDB.write(batchPoints);

		logger.info("Add SensorData to Infux DB, StreamID: {}, DateTime:{}, Value: {}", value.getStreamID(),
				value.getDateTime(), value.getValue());

		return true;
	}

	@Override
	public Value readInstance(String valueID) throws Exception{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateInstance(Value value) throws Exception{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteInstance(String valueID) throws Exception{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Value> readCollection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isInstanceExist(String streamID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Value> search(String streamID, String startDate, String endDate) throws Exception{
		
		InfluxDB influxDB = InfluxDBFactory.connect("http://" + ip + ":" + port, user, password);
		
//		boolean influxDBstarted = false;
//		do {
//			Pong response;
//			try {
//				response = influxDB.ping();
//				System.out.println(response);
//				if (!response.getVersion().equalsIgnoreCase("unknown")) {
//					influxDBstarted = true;
//				}
//			} catch (Exception e) {
//				// NOOP intentional
//				e.printStackTrace();
//			}
//			try {
//				Thread.sleep(100L);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} while (!influxDBstarted);
//		influxDB.setLogLevel(LogLevel.FULL);
//		// String logs = CharStreams.toString(new InputStreamReader(containerLogsStream, Charsets.UTF_8));
		logger.debug(" ===== Connected to InfluxDB Version: {} =====", influxDB.version());
		
		List<Value> values = new ArrayList<Value>();

		Query query = new Query(" SELECT * FROM DataValue WHERE StreamID = '" + streamID + "' AND time > '" + startDate + "' AND time < '" + endDate + "' ", db);
		QueryResult result = influxDB.query(query);
		List<List<Object>> valueList = result.getResults().get(0).getSeries().get(0).getValues();
		for(List<Object> val : valueList){
			Value value = new Value();
			value.setDateTime(val.get(1).toString());
			value.setStreamID(val.get(2).toString());
			value.setValue(Double.parseDouble(val.get(3).toString()));
			values.add(value);
		}
		return values;
	}

	@Override
	public Map<String, List<Value>> listSearch(Map<String, List<String>> map) throws Exception{
		Map<String, List<Value>> valueMap = new HashMap<String, List<Value>>();
//		InfluxDB influxDB = InfluxDBFactory.connect(url, user, password);
//
//		Query query = new Query("SELECT * FROM DataValue WHERE StreamID = '" + streamID + "' AND time > '" + startDate + "' AND time < '" + endDate + "'", db);
//		QueryResult result = influxDB.query(query);
//		List<List<Object>> valueList = result.getResults().get(0).getSeries().get(0).getValues();
//		for(List<Object> val : valueList){
//			Value value = new Value();
//			value.setDateTime(val.get(1).toString());
//			value.setStreamID(val.get(2).toString());
//			value.setValue(Double.parseDouble(val.get(3).toString()));
//			values.add(value);
//		}
		return valueMap;
	}

}
