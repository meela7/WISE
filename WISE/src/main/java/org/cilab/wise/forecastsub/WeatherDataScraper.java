package org.cilab.wise.forecastsub;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class WeatherDataScraper {
	
	private final static Logger logger = LoggerFactory.getLogger(WeatherDataScraper.class);
	
	public static void main(String[] args) {		
		
		try{
			Resource resource = new ClassPathResource("META-INF/forecast.properties");
			Properties props = PropertiesLoaderUtils.loadProperties(resource);
			
			String exeTime = props.getProperty("forecast.exetime");
			String period = props.getProperty("forecast.period");
			
			Calendar calendar = Calendar.getInstance();
			Date date = calendar.getTime();
			String currHour = new SimpleDateFormat("HH").format(date);
			if(Integer.parseInt(currHour) >= Integer.parseInt(exeTime)){
				calendar.add(Calendar.DATE, 1);
				exeTime = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()) + " 00:45";
				date = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(exeTime);
			}
			DailySubscriber job = new DailySubscriber();
					
			Timer jobScheduler = new Timer();
	
//			date = new Date();
			logger.debug("===== Start Daily Subscribe At {}, Period: {} ===== ", exeTime, period);
			
			jobScheduler.scheduleAtFixedRate(job, date, getTimePrecision(period));	
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}

	public static long getTimePrecision(String value) throws Exception {
		long l = 0;
		String val = "";
		try {
			if (value.endsWith("d") || value.endsWith("D")) {
				val = value.substring(0, value.length() - 1);
				l = Long.parseLong(val) * 24 * 60 * 60 * 1000;
			}

			else if (value.endsWith("h") || value.endsWith("H")) {

				val = value.substring(0, value.length() - 1);
				l = Long.parseLong(val) * 60 * 60 * 1000;

			} else if (value.endsWith("m") || value.endsWith("M")) {
				val = value.substring(0, value.length() - 1);
				l = Long.parseLong(val) * 60 * 1000;
			} else if (value.endsWith("s") || value.endsWith("S")) {

				val = value.substring(0, value.length() - 1);
				l = Long.parseLong(val) * 1000;
			} else {

				l = Long.parseLong(value);
			}

		} catch (Exception e) {

			throw new Exception(e);
		}

		return l;
	}
}