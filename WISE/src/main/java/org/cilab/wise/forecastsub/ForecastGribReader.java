package org.cilab.wise.forecastsub;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ForecastGribReader  {

	private final static Logger logger = LoggerFactory.getLogger(ForecastGribReader.class);

	public List<Item> HourlySubscribe(Item reqItem){

		logger.info(" REST Service Reader Starterd...");

		try {
			/*
			 * read configuration
			 */
			Resource resource = new ClassPathResource("META-INF/forecast.properties");
			Properties props = PropertiesLoaderUtils.loadProperties(resource);
			
			
			String forecastApiBaseUri = props.getProperty("forecast.url");
			String serviceKey = props.getProperty("forecast.servicekey");
			String type = props.getProperty("forecast.resType");

			// String baseTime = "1600";

			URIBuilder builder = new URIBuilder(forecastApiBaseUri);
			builder.addParameter("ServiceKey", URLDecoder.decode(serviceKey, "UTF-8"));
			builder.addParameter("base_date", reqItem.getBaseDate());
			builder.addParameter("base_time", reqItem.getBaseTime());
			builder.addParameter("nx", reqItem.getNx());
			builder.addParameter("ny", reqItem.getNy());
			builder.addParameter("_type", type);

			// Call RESTful API for forecast
			HttpClient client = HttpClients.createDefault();
			String listStubsUri = builder.build().toString();
			HttpGet getStubMethod = new HttpGet(listStubsUri);
			HttpResponse getStubResponse = client.execute(getStubMethod);

			int getStubStatusCode = getStubResponse.getStatusLine().getStatusCode();
			if (getStubStatusCode < 200 || getStubStatusCode >= 300) {
				// Handle non-2xx status code
				return null;
			}
			String responseBody = EntityUtils.toString(getStubResponse.getEntity());

			ObjectMapper mapper = new ObjectMapper();
			JsonNode response = mapper.readTree(responseBody).path("response");
			if (response.path("header").path("resultMsg").asText().equals("OK")) {
				int totalCount = response.path("body").path("totalCount").intValue();
				if (totalCount == 0) {
					logger.error("Response with no Data.");
					throw new RuntimeException(" ===== ForecastGribReader 94 ===== No Result. =====");
				}
				JsonNode readItems = response.path("body").path("items").path("item");
//				logger.debug(" ===== RESPONSE ITEM: {} =====", readItems.toString());

				List<Item> dataList = mapper.readValue(readItems.traverse(),
						mapper.getTypeFactory().constructCollectionType(List.class, Item.class));
				return dataList;
			} else {
				logger.debug(" ===== MSG: {} =====", response.path("header").path("resultMsg").asText());

			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return null;
	}
}
