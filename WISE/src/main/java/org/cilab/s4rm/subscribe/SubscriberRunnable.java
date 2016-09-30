package org.cilab.s4rm.subscribe;

import java.util.concurrent.BlockingQueue;

import org.cilab.agabus.messaging.sub.TopicSubscriberImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubscriberRunnable extends TopicSubscriberImpl implements Runnable{
	
	private static final Logger logger = LoggerFactory.getLogger(SubscriberRunnable.class);

	private String streamID;
	private BlockingQueue<String> queue;
	
	SubscriberRunnable(BlockingQueue<String> queue, String streamID){
		this.queue = queue;
		this.streamID = streamID;
	}
	
	public void run(){
		try{
			while (!queue.isEmpty()) {
				String msg = queue.take();
				if (msg.equals("STOP")) {
					logger.debug(" ========== Stop Subscriber for: {}. ========== ", streamID);
					unsubscribe();
					break;
				}				
			}
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	
	public String getStreamID(){
		return streamID;
	}

}