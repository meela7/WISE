package org.cilab.wise.forecastsub;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class DailySubscriber extends TimerTask {

	private final static Logger logger = LoggerFactory.getLogger(DailySubscriber.class);
	

	public void run(){
		
		ForecastGribReader forecaster = new ForecastGribReader();
		DataPublisher publisher = new DataPublisher();
		
		try{
			// Current Time
			Calendar calendar = Calendar.getInstance();
			Date date = calendar.getTime();
						
			long currTime = date.getTime();
			
			Resource resource = new ClassPathResource("META-INF/forecast.properties");
			Properties props = PropertiesLoaderUtils.loadProperties(resource);
			
			int siteNum = Integer.parseInt(props.getProperty("site.num"));
			
			for (int i = 0; i < 24; i++){
				
				Item reqItem = new Item();
				
				long exeTime = currTime - i * 60 * 60 * 1000;
				reqItem.setBaseDate( new SimpleDateFormat("yyyyMMdd").format(exeTime) );
				reqItem.setBaseTime( new SimpleDateFormat("HH").format(exeTime) + "00" );
				logger.debug("=====  ===== Hourly Subscribe date: {}, time: {} ===== ===== ", reqItem.getBaseDate(), reqItem.getBaseTime());
				
				for(int j = 0; j < siteNum; j++){
					String grid = props.getProperty("site.grid."+(j+1));
					reqItem.setNx(grid.split(",")[0]);
					reqItem.setNy(grid.split(",")[1]);
					List<Item> resItem = forecaster.HourlySubscribe(reqItem);
					
					for(Item item: resItem){
						if (item.getCategory().equals("REH") || item.getCategory().equals("T1H") || item.getCategory().equals("RN1")){
							
							publisher.PublishData(item);
						}
					}
				}
				
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
