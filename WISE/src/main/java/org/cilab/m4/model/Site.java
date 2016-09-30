package org.cilab.m4.model;

public class Site {
	
	/**
	 * Class Name:	Site.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.14
	 * @version 1.2
	 *  
	 */
	private int SiteID;
	private String SiteName;
	private String Latitude;
	private String Longitude;
	private String Address;
	private String ImageLink;
	private String Description;
	
	public int getSiteID() {
		return SiteID;
	}
	public void setSiteID(int siteID) {
		SiteID = siteID;
	}
	public String getSiteName() {
		return SiteName;
	}
	public void setSiteName(String siteName) {
		SiteName = siteName;
	}
	public String getLatitude() {
		return Latitude;
	}
	public void setLatitude(String latitude) {
		Latitude = latitude;
	}
	public String getLongitude() {
		return Longitude;
	}
	public void setLongitude(String longitude) {
		Longitude = longitude;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
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
