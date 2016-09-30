package org.cilab.m4.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.cilab.m4.model.Site;


public interface SiteDAO {
	
	/**
	 * Class Name:	SiteDAO.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.13
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	public void create(Site site) throws SQLException;
	public Site read(int siteID) throws SQLException;
	public void update(Site site) throws SQLException;
	public void delete(int siteID) throws SQLException;
	
	public List<Site> list() throws SQLException;
	public Site getByUniqueKey(String name) throws SQLException;
	public List<Site> search(Map<String, String> map) throws SQLException;
	public List<Site> listSearch(Map<String, List<String>> map) throws SQLException;

}
