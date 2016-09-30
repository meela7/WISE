package org.cilab.s4rm.upload;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.cilab.s4rm.model.Value;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.InfluxDB.ConsistencyLevel;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyDB {

	/**
	 * Class Name: MyDB.java
	 * 
	 * Description:
	 * 
	 * @author Meilan Jiang
	 * @since 2016.01.30
	 * @version 1.0
	 * 
	 *          Copyright(c) 2016 by CILAB All right reserved.
	 */

	private static final Logger logger = LoggerFactory.getLogger(MyDB.class);

	public static void createValue(Value value) throws ParseException {
		logger.debug(" ===== StreamID: {}, DateTime: {}, Value: {} ===== ", value.getStreamID(), value.getDateTime(),
				value.getValue());
		InfluxDB influxDB = InfluxDBFactory.connect("http://52.197.130.88:8086", "root", "cir@817!");

		BatchPoints batchPoints = BatchPoints.database("db_wise").tag("async", "true").retentionPolicy("default")
				.consistency(ConsistencyLevel.ALL).build();

//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat format = new SimpleDateFormat("yy. MM. dd");
		long dateTime = format.parse(value.getDateTime()).getTime();
		
		Point point = Point.measurement("DataValue").time(dateTime, TimeUnit.MILLISECONDS)
				.addField("StreamID", value.getStreamID()).addField("DateTime", value.getDateTime())
				.addField("Value", value.getValue()).build();

		batchPoints.point(point);

		influxDB.write(batchPoints);

		logger.info("Add SensorData to Infux DB, StreamID: {}, DateTime:{}, Value: {}", value.getStreamID(),
				value.getDateTime(), value.getValue());
	}
	
//	public static void createValueSet(List<Value> values) throws ParseException {
//		InfluxDB influxDB = InfluxDBFactory.connect("http://52.197.130.88:8086", "root", "cir@817!");
//
//		// Flush every 2000 Points, at least every 100ms
//		influxDB.enableBatch(2000, 100, TimeUnit.MILLISECONDS);
//		
//		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		SimpleDateFormat format = new SimpleDateFormat("yy. MM. dd");
//		
//		// can add maximum 5000 points
//		for (Value value : values) {
//
//			long dateTime = format.parse(value.getDateTime()).getTime();
//			// change UTC timestamp to Seoul Timezone UTC +9
//			long timeStamp = dateTime + 9 * 60 * 60 * 1000;
//			
//			Date date = new Date(dateTime);
//		    String dateText = dtFormat.format(date);
//			Point point = Point.measurement("DataValue").time(timeStamp, TimeUnit.MILLISECONDS)
//					.addField("StreamID", value.getStreamID()).addField("DateTime", dateText)
//					.addField("Value", value.getValue()).build();
//			logger.debug("Add SensorData to Infux DB, StreamID: {}, DateTime:{}, Value: {}", value.getStreamID(),
//					value.getDateTime(), value.getValue());
//			
//			influxDB.write("wise", "default", point);
//		}		
//	}

	public static void createValueSet(List<Value> values) throws ParseException {
		InfluxDB influxDB = InfluxDBFactory.connect("http://52.197.130.88:8086", "root", "cir@817!");

		BatchPoints batchPoints = BatchPoints.database("db_wise").tag("async", "true")
				.tag("year", "2014").tag("variable", "모기").tag("site", "윤중초등학교")
				.retentionPolicy("default")
				.consistency(ConsistencyLevel.ALL).build();

//		// Flush every 2000 Points, at least every 100ms
//		influxDB.enableBatch(2000, 100, TimeUnit.MILLISECONDS);
		
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat format = new SimpleDateFormat("yy. MM. dd");
		
		// can add maximum 5000 points
		for (Value value : values) {

			long dateTime = format.parse(value.getDateTime()).getTime();
			// change UTC timestamp to Seoul Timezone UTC +9
			long timeStamp = dateTime + 9 * 60 * 60 * 1000; // 이렇게 하면 InfluxDB의 실시간 Streaming Data와 9시간의 충돌이 일어난다. 타임 조작하지 않고 그대로 UTC로 저장하는것이 타당하다.
			
			Date date = new Date(dateTime);
		    String dateText = dtFormat.format(date);
			Point point = Point.measurement("DataValue").time(timeStamp, TimeUnit.MILLISECONDS)
					.addField("StreamID", value.getStreamID()).addField("DateTime", dateText)
					.addField("Value", value.getValue()).build();
			logger.debug("Add SensorData to Infux DB, StreamID: {}, DateTime:{}, Value: {}", value.getStreamID(),
					value.getDateTime(), value.getValue());
			batchPoints.point(point);
//			influxDB.write("wisedb", "default", point);
		}
		influxDB.write(batchPoints);		
	}

	
//	public static void createWaterQualityValueSet(List<Value> values) throws ParseException {
//		InfluxDB influxDB = InfluxDBFactory.connect("http://52.197.130.88:8086", "root", "cir@817!");
//
//		
//		// Flush every 2000 Points, at least every 100ms
//		influxDB.enableBatch(2000, 100, TimeUnit.MILLISECONDS);
//		
//		SimpleDateFormat inFormat = new SimpleDateFormat("yy. MM. dd HH:mm:ss");
//		SimpleDateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		
//		// can add maximum 5000 points
//		for (Value value : values) {
//
//			long dateTime = inFormat.parse(value.getDateTime()).getTime();
//			// change UTC timestamp to Seoul Timezone UTC +9
//			long timeStamp = dateTime + 9 * 60 * 60 * 1000;
//			
//			Date date = new Date(dateTime);
//		    String dateText = outFormat.format(date);
//			Point point = Point.measurement("DataValue").time(timeStamp, TimeUnit.MILLISECONDS)
//					.addField("StreamID", value.getStreamID()).addField("DateTime", dateText)
//					.addField("Value", value.getValue()).build();
//			logger.info("Add SensorData to Infux DB, StreamID: {}, DateTime:{}, Value: {}", value.getStreamID(),
//					value.getDateTime(), value.getValue());
//			
//			influxDB.write("db_wise", "156w", point);	//autogen
//		}	
//	}
	
	public static void createWaterQualityValueSet(List<Value> values) throws ParseException {
		
		// don't use influxdb-java.
		
		
		
		InfluxDB influxDB = InfluxDBFactory.connect("http://52.197.130.88:8086", "root", "cir@817!");

		BatchPoints batchPoints = BatchPoints.database("db_wise").tag("async", "true").tag("year", "2014").tag("variable", "LDO(%)").tag("site", "너부대교").retentionPolicy("default")
				.consistency(ConsistencyLevel.ALL).build();
		
//		// Flush every 2000 Points, at least every 100ms
//		influxDB.enableBatch(2000, 100, TimeUnit.MILLISECONDS);
	
		
		SimpleDateFormat inFormat = new SimpleDateFormat("yy. MM. dd HH:mm:ss");
		SimpleDateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		// can add maximum 5000 points
		for (Value value : values) {

			long dateTime = inFormat.parse(value.getDateTime()).getTime();
			// change UTC timestamp to Seoul Timezone UTC +9
			long timeStamp = dateTime + 9 * 60 * 60 * 1000;
			
			Date date = new Date(dateTime);
		    String dateText = outFormat.format(date);
			Point point = Point.measurement("DataValue").time(timeStamp, TimeUnit.MILLISECONDS)
					.addField("StreamID", value.getStreamID()).addField("DateTime", dateText)
					.addField("Value", value.getValue()).build();
			logger.info("Add SensorData to Infux DB, StreamID: {}, DateTime:{}, Value: {}", value.getStreamID(),
					value.getDateTime(), value.getValue());
			batchPoints.point(point);
//			influxDB.write("wisedb", "default", point);	//autogen
		}
		influxDB.write(batchPoints);	
	}

}
