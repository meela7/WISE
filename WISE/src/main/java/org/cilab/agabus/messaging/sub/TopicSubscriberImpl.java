package org.cilab.agabus.messaging.sub;

import java.util.Properties;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public class TopicSubscriberImpl implements MqttCallback, TopicSubscriber {
	private Logger logger = LoggerFactory.getLogger(TopicSubscriberImpl.class);

    private static final boolean MQTT_CLEAN_SESSION_OPT = true;
    private static final int MQTT_KEEP_ALIVE_INTERVAL_OPT = 30;
    private static final int MQTT_QOS_OPT = 1;
    private MqttClient myClient;
    private MessageCallback callback;
    private String brokerURL;
    private String clientId;
    private String topicId;
	private String userId;
	private String password;
	private Client client;
	private Application app;
	
	public TopicSubscriberImpl () {
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
    public void setConnection(String brokerURL, String userId, String password, String topicId) {    	
        this.brokerURL = brokerURL;
        this.clientId = UUID.randomUUID().toString();
        this.userId = userId;
        this.password = password;
        this.topicId = topicId;
    }

    @Override
    public void setMessageCallback(MessageCallback callback) {
        this.callback = callback;
    }

    @Override
    public void subscribe() {
		if (!isAuthenticated(this.userId, this.password)) {
			System.out.println("Fail to authenitcated...");
			System.exit(-1);
		}
    	
        // setup MQTT Client
        MqttConnectOptions connOpt = new MqttConnectOptions();
        connOpt.setCleanSession(MQTT_CLEAN_SESSION_OPT);
        connOpt.setKeepAliveInterval(MQTT_KEEP_ALIVE_INTERVAL_OPT);
        
        // Connect to Broker
        try {
            myClient = new MqttClient(brokerURL, clientId);
            myClient.setCallback(this);
            myClient.connect(connOpt);
        } catch (MqttException e) {
        	logger.error("Fail to connect server...", e);
        	System.exit(-1);
        }
        
        // Subscribe to topic if subscriber
        try {
            System.out.println("Start to subscribe to " + topicId);
            myClient.subscribe(topicId, MQTT_QOS_OPT);
        } catch (MqttException e) {
        	logger.error("Fail to subscribe...", e);
        	System.exit(-1);
        } 
    }

    @Override
    public void unsubscribe() {
        try {
            myClient.disconnect();
            System.out.println("Disconneted to " + topicId);
        } catch (MqttException e) {
        	logger.error("Fail to unsubscribe...", e);
        } finally {
        	try {
        		if (myClient != null) myClient.disconnect();
				if (myClient != null) myClient.close();
			} catch (MqttException e) {
				e.printStackTrace();
			}
        	System.exit(-1);
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
			logger.error("Fail to Authenticate...", ex);
		    System.out.println(ex.getStatus()); // Will output: 400
		    System.out.println(ex.getMessage()); // Will output: "Invalid username or password."
		}
	
		return true;
	}
    
    @Override
    public void connectionLost(Throwable cause) {
        callback.connectionLost(cause);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String data = message.toString();
        callback.messageArrived(data);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        callback.deliveryComplete(token);
    }
}
