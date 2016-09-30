package org.cilab.s4rm.model;

import java.util.List;

public class Sensor {
	
	/**
	 * Class Name: CSNSensor.java 
	 * Description: 
	 * 
	 * @author Meilan Jiang
	 * @since 2016.06.10
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	
	private String Id;
	private String Name;
	private String CreatedAt;
//	private User CreatedBy;
	private String CreatedBy;
	private int Status;
//	private Stream Stream;
	private String StreamID;
	private int Members;
	private List<Sensor_Meta> Metas;
	private List<Sensor_Tag> Tags;
	private String Desc;
	
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getCreatedAt() {
		return CreatedAt;
	}
	public void setCreatedAt(String createdAt) {
		CreatedAt = createdAt;
	}
	public String getCreatedBy() {
		return CreatedBy;
	}
	public void setCreatedBy(String user) {
		CreatedBy = user;
	}
	public int getStatus() {
		return Status;
	}
	public void setStatus(int status) {
		Status = status;
	}
	public String getStreamID() {
		return StreamID;
	}
	public void setStreamID(String streamID) {
		StreamID = streamID;
	}
	public int getMembers() {
		return Members;
	}
	public void setMembers(int members) {
		Members = members;
	}
	
	public String getDesc() {
		return Desc;
	}
	public void setDesc(String desc) {
		Desc = desc;
	}
	public List<Sensor_Meta> getMetas() {
		return Metas;
	}
	public void setMetas(List<Sensor_Meta> metas) {
		Metas = metas;
	}
	public void addMetadata(Sensor_Meta meta){
		this.Metas.add(meta);
	}
	public List<Sensor_Tag> getTags() {
		return Tags;
	}
	public void setTags(List<Sensor_Tag> tags) {
		Tags = tags;
	}
	
	
}
