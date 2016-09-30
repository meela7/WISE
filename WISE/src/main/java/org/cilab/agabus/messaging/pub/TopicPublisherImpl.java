package org.cilab.agabus.messaging.pub;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.cilab.agabus.messaging.pub.TopicPublisher;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.authc.AuthenticationRequest;
import com.stormpath.sdk.authc.AuthenticationResult;
import com.stormpath.sdk.authc.UsernamePasswordRequest;
import com.stormpath.sdk.client.ApiKey;
import com.stormpath.sdk.client.ApiKeys;
import com.stormpath.sdk.client.Client;
import com.stormpath.sdk.client.Clients;
import com.stormpath.sdk.resource.ResourceException;

public class TopicPublisherImpl implements MqttCallback, TopicPublisher {
	private Logger logger = LoggerFactory.getLogger(TopicPublisherImpl.class);

	private static final boolean MQTT_CLEAN_SESSION_OPT = true;
	private static final int MQTT_KEEP_ALIVE_INTERVAL_OPT = 30;
	private static final int MQTT_QOS_OPT = 1;
	private MqttClient myClient;
	private MqttTopic topic;
	private String clientId;
	private String brokerURL;
	private String topicId;
	private String userId;
	private String password;
	private Client client;
	private Application app;

	public TopicPublisherImpl() {
		loadAuthConf();
	}
	
	private void loadAuthConf() {
		Properties properties = new Properties();
		properties.setProperty("apiKey.id", "53SPF2AY27ATFWBMLG9EEGCB5");
		properties.setProperty("apiKey.secret",
				"V9rr/wKDW6Xj7Xz7ozVRaj5OXNLw6YGFDvdMRApqqko");

		ApiKey apiKey = ApiKeys.builder().setProperties(properties).build();
		this.client = Clients.builder().setApiKey(apiKey).build();
		this.app = client
				.getResource(
						"https://api.stormpath.com/v1/applications/zR842jD0zngSegZ1XUkjz",
						Application.class);	
	}
	
	@Override
	public void setConnection(String domain, String id, String passwd, String topicId) {
		this.brokerURL = domain;
		this.clientId = UUID.randomUUID().toString();
		this.userId = id;
		this.password = passwd;
		this.topicId = topicId;
	}

	@Override
	public void connect() {
		if (!isAuthenticated(this.userId,this.password)) {
			System.out.println("Fail to authenitcate User...");
			System.exit(-1);
		}
		
		System.out.println("My ClientId is " + this.clientId);
		// Setup MQTT Client
		MqttConnectOptions connOpt = new MqttConnectOptions();
		connOpt.setCleanSession(MQTT_CLEAN_SESSION_OPT);
		connOpt.setKeepAliveInterval(MQTT_KEEP_ALIVE_INTERVAL_OPT);

		// Connect to Broker
		try {
			myClient = new MqttClient(brokerURL, clientId);
			myClient.setCallback(this);
			myClient.connect(connOpt);
		} catch (MqttException e) {
			System.out.println("Fail to connect MQ server...");
			logger.error("Fail to connect MQ server...",e);
			System.exit(-1);
		}
		// Setup topic
		topic = myClient.getTopic(topicId);
	}

	@Override
	public void close() {
		try {
			if (myClient.isConnected())
				myClient.disconnect();
		} catch (Exception e) {
			logger.error("Fail to close the server...", e);
			e.printStackTrace();
		}
	}
	
	private boolean isAuthenticated(String id, String password) {
		Account acc = null;
		try {
		    AuthenticationRequest authenticationRequest = UsernamePasswordRequest.builder()
		            .setUsernameOrEmail(id)
		            .setPassword(password)
		            .build();
		    AuthenticationResult result = app.authenticateAccount(authenticationRequest);
		    acc = result.getAccount();
		} catch (ResourceException ex) {
			logger.error("Fail to authenticate...", ex);
		    System.out.println(ex.getStatus()); // Will output: 400
		    System.out.println(ex.getMessage()); // Will output: "Invalid username or password."
		}
		
		return true;
	}

	private String convertToString(Object data) {
		ObjectMapper jsonMapper = new ObjectMapper();
		String jsonStr = null;;
		try {
			jsonStr = jsonMapper.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			System.out.println("Can not convert Object to JSON String...");
			logger.error("Fail to convert Object to JOSN String", e);
		}
		return jsonStr;
	}
	
	private void publish(String jsonStr) {
		MqttMessage message = new MqttMessage(jsonStr.getBytes());
		message.setQos(MQTT_QOS_OPT);
		message.setRetained(false);
		MqttDeliveryToken token = null;
		try {
			token = topic.publish(message);
			token.waitForCompletion();
		} catch (Exception e) {
			System.out.println("Can not Publish data...");
			logger.error("Fail to publish data...", e);
			System.exit(-1);
		}
	}

	@Override
	public void publish(Map<String,Object> data) {
		String jsonStr = convertToString(new HashMap<String,Object>());
		publish(jsonStr);
	}

	@Override
	public void publish(Object data) {
		String jsonStr = convertToString(data);
		publish(jsonStr);
	}

	@Override
	public void connectionLost(Throwable arg0) {
		System.out.println("Connection lost!");
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
	}

	@Override
	public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
	}
}

