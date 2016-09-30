package org.cilab.s4rm.model;

public class Metadata {

	/**
	 * Class Name: Metadata.java 
	 * Description: 
	 * 
	 * @author Meilan Jiang
	 * @since 2016.06.10
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	
	private int MetadataID;
	private String Key;
	private String Value;
	
	public int getMetadataID() {
		return MetadataID;
	}
	public void setMetadataID(int metadataID) {
		MetadataID = metadataID;
	}
	public String getKey() {
		return Key;
	}
	public void setKey(String key) {
		Key = key;
	}
	public String getValue() {
		return Value;
	}
	public void setValue(String value) {
		Value = value;
	}
	
}
