package org.cilab.m4.model;

public class Fish extends Entity{

	/**
	 * Class Name: Fish.java 
	 * Description: Hibernate Mapping pojo File
	 * 
	 * @author Meilan Jiang
	 * @since 2016.06.10
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	
	private String FishClass;
	private String Order;
	private String Family;
	private String ScientificName;
	private String Species;
	private String ToleranceGuild;
	private String TrophicGuild;
	private String HabitatGuild;
	private String InvasiveSpecies;
	private String EndemicSpecies;
	private String EndangeredSpecies;
	private String NaturalMonument;
	private String ImageLink;
	private String Description;
	
	public String getFishClass() {
		return FishClass;
	}
	public void setFishClass(String fishClass) {
		FishClass = fishClass;
	}
	public String getOrder() {
		return Order;
	}
	public void setOrder(String order) {
		Order = order;
	}
	public String getFamily() {
		return Family;
	}
	public void setFamily(String family) {
		Family = family;
	}
	public String getScientificName() {
		return ScientificName;
	}
	public void setScientificName(String scientificName) {
		ScientificName = scientificName;
	}
	public String getSpecies() {
		return Species;
	}
	public void setSpecies(String species) {
		Species = species;
	}
	public String getToleranceGuild() {
		return ToleranceGuild;
	}
	public void setToleranceGuild(String toleranceGuild) {
		ToleranceGuild = toleranceGuild;
	}
	public String getTrophicGuild() {
		return TrophicGuild;
	}
	public void setTrophicGuild(String trophicGuild) {
		TrophicGuild = trophicGuild;
	}
	public String getHabitatGuild() {
		return HabitatGuild;
	}
	public void setHabitatGuild(String habitatGuild) {
		HabitatGuild = habitatGuild;
	}
	public String getInvasiveSpecies() {
		return InvasiveSpecies;
	}
	public void setInvasiveSpecies(String invasiveSpecies) {
		InvasiveSpecies = invasiveSpecies;
	}
	public String getEndemicSpecies() {
		return EndemicSpecies;
	}
	public void setEndemicSpecies(String endemicSpecies) {
		EndemicSpecies = endemicSpecies;
	}
	public String getEndangeredSpecies() {
		return EndangeredSpecies;
	}
	public void setEndangeredSpecies(String endangeredSpecies) {
		EndangeredSpecies = endangeredSpecies;
	}
	public String getNaturalMonument() {
		return NaturalMonument;
	}
	public void setNaturalMonument(String naturalMonument) {
		NaturalMonument = naturalMonument;
	}
	public String getImageLink() {
		return ImageLink;
	}
	public void setImageLink(String imageLink) {
		ImageLink = imageLink;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}

}
