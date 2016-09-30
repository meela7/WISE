package org.cilab.m4.dao;

import java.util.List;
import java.util.Map;

import org.cilab.m4.model.Source;


public interface SourceDAO {
	
	/**
	 * Class Name:	SourceDAO.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.13
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	public boolean create(Source source);
	public Source read(int sourceID);
	public boolean update(Source source);
	public boolean delete(int sourceID);
	
	public List<Source> list();
	public Source getByUniqueKey(String institution, String name);
	public List<Source> search(Map<String, String> map);
	public List<Source> listSearch(Map<String, List<String>> map);

}
