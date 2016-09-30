package org.cilab.s4rm.model;

import java.util.Set;

public class Stream {

	/**
	 * Class Name: Stream.java 
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
	private String CreatedBy;
	private String CreatedAt;
	private int Public;
	private int Status;
	private int Subs;
	private Set<Stream_Meta> Metas;
	private Set<Stream_Tag> Tags;
	private String SensorID;
	private int Persistence;
	private String PersistenceStartedAt;
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
	public String getCreatedBy() {
		return CreatedBy;
	}
	public void setCreatedBy(String user) {
		CreatedBy = user;
	}
	public String getCreatedAt() {
		return CreatedAt;
	}
	public void setCreatedAt(String createdAt) {
		CreatedAt = createdAt;
	}
	public int getPublic() {
		return Public;
	}
	public void setPublic(int pub) {
		Public = pub;
	}
	public String getDesc() {
		return Desc;
	}
	public void setDesc(String desc) {
		Desc = desc;
	}
	public Set<Stream_Meta> getMetas() {
		return Metas;
	}
	public void setMetas(Set<Stream_Meta> metas) {
		Metas = metas;
	}
	public Set<Stream_Tag> getTags() {
		return Tags;
	}
	public void setTags(Set<Stream_Tag> tags) {
		Tags = tags;
	}
	public int getStatus() {
		return Status;
	}
	public void setStatus(int status) {
		Status = status;
	}
	public int getSubs() {
		return Subs;
	}
	public void setSubs(int subs) {
		Subs = subs;
	}
	public String getSensorID() {
		return SensorID;
	}
	public void setSensorID(String sensorID) {
		SensorID = sensorID;
	}
	public int getPersistence() {
		return Persistence;
	}
	public void setPersistence(int persistence) {
		Persistence = persistence;
	}
	public String getPersistenceStartedAt() {
		return PersistenceStartedAt;
	}
	public void setPersistenceStartedAt(String persistenceStartedAt) {
		PersistenceStartedAt = persistenceStartedAt;
	}
	
}
