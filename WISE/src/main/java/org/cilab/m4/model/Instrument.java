package org.cilab.m4.model;

public class Instrument extends Method{
	/**
	 * Class Name: Method.java 
	 * Description: Hibernate Mapping pojo File
	 * 
	 * @author Meilan Jiang
	 * @since 2016.04.28
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */

	private String Manufacturer;
	private String ProductModel;
	private String MeasureType;
	private String Description;
	
	public String getMeasureType() {
		return MeasureType;
	}
	public void setMeasureType(String measureType) {
		MeasureType = measureType;
	}
	public String getManufacturer() {
		return Manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		Manufacturer = manufacturer;
	}
	public String getProductModel() {
		return ProductModel;
	}
	public void setProductModel(String productModel) {
		ProductModel = productModel;
	}
	
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}	
	
}
