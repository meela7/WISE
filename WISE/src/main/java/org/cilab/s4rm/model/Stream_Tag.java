package org.cilab.s4rm.model;

public class Stream_Tag {

	/**
	 * Class Name: Stream_Tag.java 
	 * Description: 
	 * 
	 * @author Meilan Jiang
	 * @since 2016.06.10
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	
	private int TagID;
	private String StreamID;
	private String Name;
	
	public int getTagID() {
		return TagID;
	}
	public void setTagID(int tagID) {
		TagID = tagID;
	}
	public String getStreamID() {
		return StreamID;
	}
	public void setStreamID(String streamID) {
		StreamID = streamID;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	
}
