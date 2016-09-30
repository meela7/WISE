package org.cilab.m4.service;

import java.util.List;
import java.util.Map;

import org.cilab.m4.model.Site;

public interface SiteService {
	
	/**
	 * Class Name:	SiteService.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.14
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	public void newInstance(Site site) throws Exception;
	public Site readInstance(int siteID) throws Exception;
	public void updateInstance(Site site) throws Exception;
	public void deleteInstance(int siteID) throws Exception;	
	public List<Site> readCollection() throws Exception;
	
	public boolean isInstanceExist(String name) throws Exception;
	public List<Site> search(Map<String, String> map) throws Exception;
	public List<Site> listSearch(Map<String, List<String>> map) throws Exception;

}
