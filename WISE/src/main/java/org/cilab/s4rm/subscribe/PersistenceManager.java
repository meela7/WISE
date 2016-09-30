package org.cilab.s4rm.subscribe;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

	

public class PersistenceManager {

	private static final Logger logger = LoggerFactory.getLogger(PersistenceManager.class);	
	
	private String url = null;
	private String user = null;
	private String password = null;
	
	private String dbUrl = null;
	private String dbUser = null;
	private String dbPassword = null;
	
	private static final ConcurrentHashMap<String, BlockingQueue<String>> subscriberMap = new ConcurrentHashMap<String, BlockingQueue<String>>();
	
	


	// designed as singleton class
	private static PersistenceManager instance;
		
	private PersistenceManager() {
		
		init();
	}
	
	public static PersistenceManager getInstance (){
		if ( instance == null )
			instance = new PersistenceManager();
		return instance;		
	}
	
	public void init(){
		try {
			Resource resource = new ClassPathResource("META-INF/config.properties");
			Properties props = null;
		
			props = PropertiesLoaderUtils.loadProperties(resource);
			url = props.getProperty("sri.url");
			user = props.getProperty("sri.username");
			password = props.getProperty("sri.password");
			
			dbUrl = props.getProperty("jdbc.databaseurl");
			dbUser = props.getProperty("jdbc.username");
			dbPassword = props.getProperty("jdbc.password");
				
		
			
			// error occurs when using Service or DAO
			logger.info(" ========== Persistence Manager Initiates ... ==========");
			
	        Connection con = null;
	       
	        Class.forName("com.mysql.jdbc.Driver");
	        logger.debug(" ========== load Driver!  ========== ");
	        con = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
	        
	        String sql = "SELECT * FROM Stream;";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
	        while(rs.next()){
	        	if(rs.getInt("Persistence") == 1){
	        		logger.debug(" ========== Subscribe Stream: {} ... ==========", rs.getString("StreamID"));
	        		startSubscribe(rs.getString("StreamID"));
	        	}
	        }
	        con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
            logger.debug(" ========== Error:  ========== ");
            e.printStackTrace();
        } catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void startSubscribe(String streamID){
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
		
		SubscriberRunnable subscriber = new SubscriberRunnable(queue, streamID);
		subscriber.setConnection(url, user, password, streamID);
		MyMessageCallback mc = new MyMessageCallback();
		subscriber.setMessageCallback(mc);
		subscriber.subscribe();
		subscriberMap.put(streamID, queue);
	}
	
	public void stopSubscribe(String streamID){
		try {
			subscriberMap.get(streamID).put("STOP");
				
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	public Set<String> getSubscriberList(){
		
		return subscriberMap.keySet();
	}

}
