package org.cilab.agabus.messaging.pub;

import java.util.Map;

public interface TopicPublisher {
	public void setConnection(String domain, String id, String passwd, String topicId);
	
	public void connect();
	
	public void close();
	
	public void publish(Map<String,Object> data);
	
	public void publish(Object data);
}
