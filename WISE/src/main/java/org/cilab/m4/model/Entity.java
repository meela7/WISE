package org.cilab.m4.model;

public class Entity {
	
	/**
	 * Class Name: Entity.java 
	 * Description: Hibernate Mapping pojo File
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.14
	 * @version 1.2
	 * 
	 * @modified 2016.05.13 - hibernate joined sub-class inheritance strategy
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	private int EntityID;
	private String EntityType;
	private String EntityName;
	
	public int getEntityID() {
		return EntityID;
	}
	public void setEntityID(int entityID) {
		EntityID = entityID;
	}
	public String getEntityType() {
		return EntityType;
	}
	public void setEntityType(String entityType) {
		EntityType = entityType;
	}
	public String getEntityName() {
		return EntityName;
	}
	public void setEntityName(String entityName) {
		EntityName = entityName;
	}

	
}
