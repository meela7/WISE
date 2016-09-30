package org.cilab.m4.service;

import java.util.List;
import java.util.Map;

import org.cilab.m4.model.Source;

public interface SourceService {
	
	/**
	 * Class Name:	SourceService.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.05.14
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	public boolean newInstance(Source source);
	public Source readInstance(int sourceID);
	public boolean updateInstance(Source source);
	public boolean deleteInstance(int sourceID);	
	public List<Source> readCollection();
	
	public boolean isInstanceExist(String institution, String name);
	public List<Source> search(Map<String, String> map);
	public List<Source> listSearch(Map<String, List<String>> map);

}
