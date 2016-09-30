package org.cilab.s4rm.service;

import java.util.List;
import java.util.Map;

import org.cilab.s4rm.model.Metadata;

public interface MetaService {
	
	/**
	 * Class Name:	MetaService.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.06.16
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	public boolean newInstance(Metadata meta);
	public Metadata readInstance(int metaID);
	public boolean updateInstance(Metadata meta);
	public boolean deleteInstance(int metaID);	
	public List<Metadata> readCollection();
	
	public boolean isInstanceExist(String key, String value);
	public List<Metadata> search(Map<String, String> map);
	public List<Metadata> listSearch(Map<String, List<String>> map);
}
