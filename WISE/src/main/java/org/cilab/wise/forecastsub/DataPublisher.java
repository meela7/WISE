package org.cilab.wise.forecastsub;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.cilab.agabus.messaging.pub.PublisherFactory;
import org.cilab.agabus.messaging.pub.TopicPublisher;
import org.cilab.s4rm.model.SensorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;


public class DataPublisher {
	
	private final static Logger logger = LoggerFactory.getLogger(DataPublisher.class);

	private Properties props;
	private String url;
	private String username;
	private String password;

	public DataPublisher() {
		try {
			Resource resource = new ClassPathResource("META-INF/config.properties");
			props = PropertiesLoaderUtils.loadProperties(resource);
			url = props.getProperty("sri.url");
			username = props.getProperty("sri.username");
			password = props.getProperty("sri.password");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void PublishData(Item item) {
		try {
			String sri_id = props.getProperty(item.getNx() + item.getNy() + "." + item.getCategory());
			TopicPublisher pub = new PublisherFactory().getTopicPublisher();
			pub.setConnection(url, username, password, sri_id);

			 pub.connect();

			logger.debug("===== Data To Publish: nx:{}, ny:{}, category:{}, value:{}.  ===== ",
					item.getNx(), item.getNy(), item.getCategory(),
					item.getObsrValue());
			Date dateTime = new SimpleDateFormat("yyyyMMddHHmm").parse(item.getBaseDate() + item.getBaseTime());
			String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateTime);
			SensorData sData = new SensorData();
			sData.setId(sri_id);
			sData.setTimestamp(timeStamp);
			sData.setValue(item.getObsrValue());

			 pub.publish(sData);
			 pub.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
