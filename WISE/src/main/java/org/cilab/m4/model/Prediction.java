package org.cilab.m4.model;

public class Prediction extends Method{
	/**
	 * Class Name: Prediction.java 
	 * Description: Hibernate Mapping pojo File
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.09
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */

	private String ModelingMethod;
	private String ExecuteEnvironment;
	private String CreatedAt;
	private String CreatedBy;
	private String Description;

	public String getModelingMethod() {
		return ModelingMethod;
	}
	public void setModelingMethod(String modelingMethod) {
		ModelingMethod = modelingMethod;
	}
	public String getExecuteEnvironment() {
		return ExecuteEnvironment;
	}
	public void setExecuteEnvironment(String executeEnvironment) {
		ExecuteEnvironment = executeEnvironment;
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
	public void setCreatedBy(String createdBy) {
		CreatedBy = createdBy;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}

	
}
