package org.cilab.s4rm.model;

public class Log {

	/**
	 * Class Name: PersistenceLog.java 
	 * Description: 
	 * 
	 * @author Meilan Jiang
	 * @since 2016.06.10
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	
	private int LogID;
	private String StreamID;
	private String StartedAt;
	private String StopedAt;
	
	public int getLogID() {
		return LogID;
	}
	public void setLogID(int logID) {
		LogID = logID;
	}
	public String getStreamID() {
		return StreamID;
	}
	public void setStreamID(String streamID) {
		StreamID = streamID;
	}
	public String getStartedAt() {
		return StartedAt;
	}
	public void setStartedAt(String startedAt) {
		StartedAt = startedAt;
	}
	public String getStopedAt() {
		return StopedAt;
	}
	public void setStopedAt(String stopedAt) {
		StopedAt = stopedAt;
	}
	
}
