package org.cilab.m4.model;

public class Source {
	
	/**
	 * Class Name:	Source.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.14
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 * 
	 * CREATE TABLE `Source` (
	 * `SourceID` int(11) NOT NULL AUTO_INCREMENT,
	 * `Institution` varchar(50) NOT NULL,
	 * `ContactName` varchar(50) DEFAULT NULL,
	 * `ContactPhone` varchar(11) DEFAULT NULL,
	 * `ContactEmail` varchar(50) DEFAULT NULL,
	 * `Description` varchar(50) DEFAULT NULL,
	 * PRIMARY KEY (`SourceID`)
	 * ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
	 */

	private int SourceID;
	private String Institution;
	private String ContactName;
	private String ContactPhone;
	private String ContactEmail;
	private String Description;
	
	public int getSourceID() {
		return SourceID;
	}
	public void setSourceID(int sourceID) {
		SourceID = sourceID;
	}
	public String getInstitution() {
		return Institution;
	}
	public void setInstitution(String institution) {
		Institution = institution;
	}
	public String getContactName() {
		return ContactName;
	}
	public void setContactName(String contactName) {
		ContactName = contactName;
	}
	public String getContactPhone() {
		return ContactPhone;
	}
	public void setContactPhone(String contactPhone) {
		ContactPhone = contactPhone;
	}
	public String getContactEmail() {
		return ContactEmail;
	}
	public void setContactEmail(String contactEmail) {
		ContactEmail = contactEmail;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	@Override
	public String toString() {
		return "Source [SourceID=" + SourceID + ", Institution=" + Institution + ", ContactName=" + ContactName
				+ ", ContactPhone=" + ContactPhone + ", ContactEmail=" + ContactEmail + ", Description=" + Description
				+ "]";
	}
	
}
